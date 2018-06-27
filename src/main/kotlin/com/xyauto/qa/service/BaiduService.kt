package com.xyauto.qa.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.mapper.qa.AnswerMapper
import com.xyauto.qa.mapper.qa.AttachMapper
import com.xyauto.qa.model.Answer
import com.xyauto.qa.model.Attach
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.HttpClientWrapper
import com.xyauto.qa.util.MD5
import com.xyauto.qa.util.annotation.CallbackJob
import com.xyauto.qa.util.cons.BaiduCons
import com.xyauto.qa.util.enum.SourceEntry
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import javax.annotation.PostConstruct

/**
 * Created by shiqm on 2017-11-06.
 */

@Service
class BaiduService {

    @Autowired
    lateinit var answerMapper: AnswerMapper
    @Autowired
    lateinit var baiduCons: BaiduCons
    @Log
    lateinit var logger: Logger
    @Autowired
    lateinit var attachMapper: AttachMapper
    @Autowired
    lateinit var cache: Cache

    lateinit private var executeThreadPoolExecutor: ThreadPoolExecutor


    @PostConstruct
    private fun init() {
        executeThreadPoolExecutor = Executors.newFixedThreadPool(baiduCons.taskThread.toInt()) as ThreadPoolExecutor
    }

    @Async
    @CallbackJob("百度回复同步")
    fun syncAnswer(task_id: String) {
        val keyLock = "qa:sync:sp:baidu"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 60L)
        val key = "qa:sync:sp:baidu:answer"
        var startValue = cache[key] ?: 0
        val start: Long = startValue.toString().toLong()
        var end = System.currentTimeMillis() / 1000
        logger.info("start:$start   end:$end")
        if (flag && start < end) {
            if (cache.set(key, end.toString())) {
                cache.remove(keyLock)
                val count = answerMapper.getListByCreateAtCountOnFirst(SourceEntry.Third_Baidu.value, start, end)
                logger.error("count:$count")
                if (count > 0) {
                    val pageNum = count / baiduCons.dealSize.toInt() + if (count % baiduCons.dealSize.toInt() == 0) 0 else 1
                    val exe: ExecutorService = executeThreadPoolExecutor
                    for (i in 1..pageNum) {
                        //每一页安排一个线程进息处理
                        exe.execute({
                            //当前处理的页码
                            PageHelper.startPage<Answer>(i, baiduCons.dealSize.toInt())
                            //排序
                            PageHelper.orderBy("ans.created_at asc")
                            val page: Page<Answer> = answerMapper.getListByCreateAtOnFirst(SourceEntry.Third_Baidu.value, start, end)
                            //处理并发送
                            page.forEach { answer ->
                                if (answer.question != null) {
                                    if (answer.deleted_at == 0 || answer.question!!.deleted_at == 0) {
                                        if (answer.content.length >= 2) {
                                            val imagesArr = JSONArray()
                                            if (answer.has_attach == 1.toShort()) {
                                                val listAttach = attachMapper.select(Attach(file_type = 0, target_id = answer.answer_id))
                                                listAttach.forEach {
                                                    val img = it.file.toString().toLowerCase()
                                                    if (img.startsWith("http") || img.startsWith("https")) {
                                                        imagesArr.add(img)
                                                    } else {
                                                        if (img.indexOf("group1") > -1) {
                                                            imagesArr.add("http://img1.qcdqcdn.com/$img")
                                                        } else {
                                                            imagesArr.add("http://img2.qcdqcdn.com/$img")
                                                        }
                                                    }
                                                }
                                            }
                                            val res = HttpClientWrapper.post(
                                                    baiduCons.url,
                                                    mapOf(
                                                            "questionid" to answer.question!!.third_id,
                                                            "pic_urls" to imagesArr.joinToString(","),
                                                            "content" to answer.content,
                                                            "appkey" to baiduCons.key,
                                                            "sign" to MD5.encode("${baiduCons.secret}&${baiduCons.key}&${answer.question!!.third_id}")
                                                    ), block = { request ->
                                                request
                                            })
                                            logger.info(res)
                                            try {
                                                val resObj = JSON.parseObject(res)
                                                val replyid = resObj.getJSONObject("data").getString("replyid")
                                                answerMapper.updateThirdId(replyid, answer.answer_id!!)
                                            } catch (e: Exception) {
                                                logger.error("update third_id error")
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                    while (true) {
                        Thread.sleep(2000)
                        if (executeThreadPoolExecutor.activeCount == 0) {
                            break
                        }
                    }
                }
            } else {
                cache.remove(keyLock)
            }
        } else {
            cache.remove(keyLock)
        }
    }

}

