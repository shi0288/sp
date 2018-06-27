package com.xyauto.qa.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.cloud.QaCloud
import com.xyauto.qa.mapper.qa.AnswerMapper
import com.xyauto.qa.mapper.qa.AttachMapper
import com.xyauto.qa.model.Answer
import com.xyauto.qa.model.Attach
import com.xyauto.qa.model.User
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.HttpClientWrapper
import com.xyauto.qa.util.MD5
import com.xyauto.qa.util.annotation.CallbackJob
import com.xyauto.qa.util.cons.PinganCons
import com.xyauto.qa.util.enum.SourceEntry
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import javax.annotation.PostConstruct

/**
 * Created by shiqm on 2017-11-06.
 */

@Service
class PinganService {

    @Autowired
    lateinit var answerMapper: AnswerMapper
    @Autowired
    lateinit var pinganCons: PinganCons
    @Autowired
    lateinit var qaCloud: QaCloud
    @Log
    lateinit var logger: Logger
    @Autowired
    lateinit var attachMapper: AttachMapper
    @Autowired
    lateinit var cache: Cache

    lateinit private var executeThreadPoolExecutor: ThreadPoolExecutor


    @PostConstruct
    private fun init() {
        executeThreadPoolExecutor = Executors.newFixedThreadPool(pinganCons.taskThread.toInt()) as ThreadPoolExecutor
    }

    @Async
    @CallbackJob("平安好车主回复同步")
    fun syncAnswer(task_id: String) {
        val keyLock = "qa:sync:sp:pingan"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 60L)
        val key = "qa:sync:sp:pingan:answer"
        var startValue = cache[key] ?: 0
        val start: Long = startValue.toString().toLong()
        var end = System.currentTimeMillis() / 1000
        logger.info("start:$start   end:$end")
        if (flag && start < end) {
            if (cache.set(key, end.toString())) {
                cache.remove(keyLock)
                val count = answerMapper.getListByCreatedAtCount(SourceEntry.Third_Pingan.value, start, end)
                if (count > 0) {
                    val pageNum = count / pinganCons.dealSize.toInt() + if (count % pinganCons.dealSize.toInt() == 0) 0 else 1
                    val exe: ExecutorService = executeThreadPoolExecutor
                    for (i in 1..pageNum) {
                        //每一页安排一个线程进息处理
                        exe.execute({
                            //当前处理的页码
                            PageHelper.startPage<Answer>(i, pinganCons.dealSize.toInt())
                            //排序
                            PageHelper.orderBy("answer.created_at asc")
                            val page: Page<Answer> = answerMapper.getListByCreatedAt(SourceEntry.Third_Pingan.value, start, end)
                            //处理并发送
                            page.forEach { answer ->
                                if (answer.question != null) {
                                    if (answer.deleted_at == 0 || answer.question!!.deleted_at == 0) {
                                        if (answer.content.length >= 2) {
                                            logger.error(answer.toString())
                                            val user = try {
                                                JSON.parseObject(qaCloud.getUserDesc(answer.uid).data.toString(), User::class.java)
                                            } catch (e: Exception) {
                                                User(1, "娃哈哈", "http://static.qcdqcdn.com/img/profile3.jpg")
                                            }
                                            val imagesArr = JSONArray()
                                            if (answer.has_attach == 1.toShort()) {
                                                val listAttach = attachMapper.select(Attach(file_type = 0, target_id = answer.answer_id))
                                                listAttach.forEach {
                                                    val img = it.file.toString()
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
                                            logger.error(HttpClientWrapper.post(
                                                    pinganCons.url,
                                                    JSONObject().apply {
                                                        put("questionID", answer.question!!.third_id)
                                                        put("images", imagesArr)
                                                        put("mainText", answer.content)
                                                        put("userInfo", JSONObject().apply {
                                                            put("userID", user.uid.toString())
                                                            put("nickname", if (user.name == null || user.name == "") {
                                                                "娃哈哈"
                                                            } else {
                                                                user.name
                                                            })
                                                            put("avatar", if (user.avatar == null || user.avatar == "") {
                                                                "http://static.qcdqcdn.com/img/profile3.jpg"
                                                            } else {
                                                                user.avatar
                                                            })
                                                        })
                                                        put("head", JSONObject().apply {
                                                            put("outerid", answer.answer_id.toString())
                                                            put("messageid", UUID.randomUUID().toString().replace("-", ""))
                                                        })
                                                    }.toString(), block = { request ->
                                                request.addHeader("HCZContent-Key", pinganCons.key)
                                                val currentTime = System.currentTimeMillis() / 1000
                                                request.addHeader("HCZContent-Sign", "${MD5.encode(pinganCons.key + currentTime + pinganCons.secret)}$currentTime")
                                                request.addHeader("cache-control", "no-cache")
                                                request.addHeader("content-type", "application/json")
                                                request
                                            }))
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
