package com.xyauto.qa.service

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.mcp.fastcloud.util.exception.base.ErrorException
import com.xyauto.qa.cloud.QaCloud
import com.xyauto.qa.model.Answer
import com.xyauto.qa.util.HttpClientWrapper
import com.xyauto.qa.util.annotation.CallbackJob
import com.xyauto.qa.util.assist.clear
import com.xyauto.qa.util.assist.paramOf360
import com.xyauto.qa.util.cons.Qh360Cons
import com.xyauto.qa.util.enum.SourceEntry
import com.xyauto.qa.util.enum.SpcpCode
import org.apache.http.Consts
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by shiqm on 2017-09-05.
 */

@Service
class Qh360Service {

    @Autowired
    lateinit var questionService: QuestionService

    @Autowired
    lateinit var answerService: AnswerService

    @Autowired
    lateinit var qaCloud: QaCloud

    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var qh360Cons:Qh360Cons



    @Async
    @CallbackJob("360问题同步")
    fun syncQuestion(task_id: String) {
        val startId = questionService.getLastestThirdId(SourceEntry.Third_360.value)
        logger.info(qh360Cons.url)
        val paramsUrl = paramOf360(qh360Cons.url, qh360Cons.secret,
                "method" to "qa.ask.pullAskByPtr",
                "app" to qh360Cons.key,
                "t" to System.currentTimeMillis().toString(),
                "cid" to "35",
                "status" to "102",
                "start_id" to startId
        )
        val response = HttpClientWrapper.get(url = paramsUrl)
        val result: JSONObject = JSON.parseObject(response)
        val code = result.getIntValue("errno")
        if (code != 0) {
            logger.error(result.toString())
            throw ErrorException(SpcpCode.TASK_360_RESPONSE("360响应code->$code"))
        }
        val data = result.getJSONArray("data")
        data.forEach {
            val question = it as JSONObject
            val thirdId = question.getString("ask_id")
            val old = questionService.getByThird(thirdId, SourceEntry.Third_360.value)
            if (old == null) {
                val title = question.getString("title")
                var content = question.getString("content")
                if (content.isEmpty()) {
                    content = title
                }
                content = content clear "<p>" clear "</p>" clear "<br>" clear "</br>"
                //不再直接操作库 update by shiqm
//                var _new = Question().apply {
//                    this.third_id = third_id
//                    this.category_id = 3
//                    this.content = content
//                    this.source = SourceEntry.Third_360.value
//                    this.status = -99
//                    val uid = (Random().nextInt(20000) + 1).toLong()
//                    this.uid = uid
//                }
//                questionService.add(_new)
                val params = HashMap<String, Any>().apply {
                    put("uid", Random().nextInt(20000) + 1)
                    put("content", content)
                    put("category_id", 3)
                    put("app_id", SourceEntry.Third_360.app_id)
                    put("third_id", thirdId)
                }
                try {
                    qaCloud.add(params)
                } catch (e: Exception) {
                    logger.error("360问题推送服务add错误")
                    e.printStackTrace()
                }

                //todo 不取回复
//                _new = questionService.add(_new) ?: return@forEach
//                val answer_count = question.getIntValue("answer_cnt")
//                if (answer_count > 0) {
//                    //收集回复
//                    try {
//                        syncReply(_new.question_id!!, third_id)
//                    } catch (ex: Exception) {
//                    }
//                }
            }

        }
    }

    @Async
    fun syncReply(question_id: Long, third_id: String) {
        val paramsUrl = paramOf360(qh360Cons.url, qh360Cons.secret,
                "method" to "qa.ask.getAskInfo",
                "ask_id" to third_id,
                "page_size" to "100",
                "user_tag" to "0",
                "app" to qh360Cons.key,
                "t" to System.currentTimeMillis().toString()
        )
        val response = HttpClientWrapper.get(paramsUrl, Consts.UTF_8)
        val result = JSON.parseObject(response)
        val code = result.getIntValue("errno")
        if (code != 0) {
            return
        }
        val list = result.getJSONArray("answer_list") ?: return
        val best = result.getJSONObject("best_answer_info")
        val recommend = result.getJSONObject("recommend_answer_info")
        if (best != null) {
            list.add(best)
        }
        if (recommend != null) {
            list.add(recommend)
        }
        list.forEach {
            val question = it as JSONObject
            var content = question.getString("content")
            content = content clear "<p>" clear "</p>" clear "<br>" clear "</br>"
            val replyThirdId = question.getString("ans_id")
            val old = answerService.getByThird(replyThirdId, SourceEntry.Third_360.value)
            if (old == null) {
                val _new = Answer().apply {
                    this.third_id = replyThirdId
                    this.question_id = question_id
                    this.content = content
                    this.source = SourceEntry.Third_360.value
                    val uid = (Random().nextInt(20000) + 1).toLong()
                    this.uid = uid
                }
                answerService.add(_new)
            }
        }
    }
}

