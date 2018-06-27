package com.xyauto.qa.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.mcp.fastcloud.util.Result
import com.xyauto.qa.mapper.qa.SpiderQuestionMapper
import com.xyauto.qa.model.SpiderQuestion
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.HttpClientWrapper
import com.xyauto.qa.util.annotation.CallbackJob
import com.xyauto.qa.util.cons.SpiderCons
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

/**
 * Created by shiqm on 2017-12-01.
 */

@Service
class SpiderService {


    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var cache: Cache

    @Autowired
    lateinit var spiderQuestionMapper: SpiderQuestionMapper

    @Autowired
    lateinit var spiderCons: SpiderCons

    private val keyLock = "qa:sync:sp:spider:lock"

    private val key = "qa:sync:sp:spider:recId"

    fun saveQuestion(): Boolean {
        var startValue = cache[key] ?: 0
        if (startValue == 0) {
            startValue = spiderQuestionMapper.getMaxRecId() ?: 0
        }
        logger.info("startValue:$startValue")
        if (cache.set(key, startValue.toString())) {
            val url="${spiderCons.url}/wenda/data?RecID=$startValue"
            logger.info("url:$url")
            val response = HttpClientWrapper.get(
                    url = url
            )
            val result = JSON.parseObject(response, Result::class.java)
            if (result.code == 10000) {
                val jsonObj = JSON.parseObject(result.data.toString())
                val items = jsonObj.getJSONArray("问答Json")
                if (items.size == 0) {
                    return false
                } else {
                    var recId: Long? = null
                    items.forEach {
                        if (it is JSONObject) {
                            recId = it.getLong("recID")
                            spiderQuestionMapper.insertSelective(SpiderQuestion(
                                    recId = recId,
                                    source = it.getInteger("resource"),
                                    content = it.toString()
                            ))
                        }
                    }
                    if (recId != null) {
                        cache.set(key, recId.toString())
                        return true
                    }
                }
            }
        }
        return false
    }

    @Async
    @CallbackJob("保存多渠道爬虫数据")
    fun syncQuestion(task_id: String) {
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 1800L)
        if (flag) {
            try {
                while (true) {
                    if (!saveQuestion()) {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            cache.remove(keyLock)
        }
    }
}
