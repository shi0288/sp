package com.xyauto.qa.controller

import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.cloud.QaCloud
import com.xyauto.qa.util.BaseController
import com.xyauto.qa.util.CacheMemory
import com.xyauto.qa.util.annotation.Encryption
import com.xyauto.qa.util.assist.getErrorRep
import com.xyauto.qa.util.enum.SourceEntry
import com.xyauto.qa.util.enum.SpcpCode
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.ReflectionUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.lang.reflect.Method
import java.util.*
import javax.annotation.PostConstruct

/**
 * Created by shiqm on 2017-10-16.
 */


@RestController
@RequestMapping("api")
class ApiController : BaseController() {

    @Autowired
    lateinit var qaCloud: QaCloud


    @PostConstruct
    private fun init() {
        if (CacheMemory.shieldMemory.size == 0) {
            CacheMemory.shieldMemory.add("世界杯")
            CacheMemory.shieldMemory.add("冠军")
            CacheMemory.shieldMemory.add("球")
            CacheMemory.shieldMemory.add("积分")
            CacheMemory.shieldMemory.add("任务")
            CacheMemory.shieldMemory.add("帖子")
            CacheMemory.shieldMemory.add("签到")
        }
    }

    @RequestMapping("addKey")
    fun notice(
            @Check(value = "key", required = false, defaultValue = StringUtils.EMPTY) key: String
    ): Any {
        if (!StringUtils.isEmpty(key)) {
            CacheMemory.shieldMemory.add(key)
        }
        return Result()
    }



    @RequestMapping(value = "interface", method = arrayOf(RequestMethod.POST))
    @Encryption
    fun handle(message: String): Any {
        logger.info("message:$message")
        val msgNode = JSONObject.parseObject(message)
        val headNode = msgNode.getJSONObject("head")
        val bodyNode = msgNode.getJSONObject("body")
        val cmd = headNode.getString("cmd") ?: return getErrorRep(SpcpCode.RESPONSE_FORMAT_ERROR)
        val method: Method = ReflectionUtils.findMethod(this.javaClass, cmd, JSONObject::class.java, JSONObject::class.java) ?: return getErrorRep(SpcpCode.RESPONSE_NO_METHOD_ERROR)
        return try {
            method.invoke(this, headNode, bodyNode)
        } catch (e: Exception) {
            getErrorRep(SpcpCode.ERROR)
        }
    }

    //创建问题
    fun CQ01(headNode: JSONObject, bodyNode: JSONObject): Any {
        if (!bodyNode.containsKey("content")) {
            return getErrorRep(SpcpCode.CQ01_NO_CONTENT)
        }
        val content = bodyNode.getString("content")
        CacheMemory.shieldMemory.forEach {
            if (content.indexOf(it) > -1) {
                return getErrorRep(SpcpCode.CQ01_CONTENT_KEY)
            }
        }

        bodyNode.put("uid", Random().nextInt(50000) + 1)
        bodyNode.put("app_id", SourceEntry.Third_Pingan.app_id)
        bodyNode.put("category_id", 3)
        bodyNode.put("third_id", if (headNode.containsKey("outerid")) headNode.getString("outerid") else null)
        val res = qaCloud.add(bodyNode)
        if (res.code == 10000) {
            try {
                val jsonBody = JSONObject()
                jsonBody.put("question_id", JSONObject.parseObject(res.data.toString()).getString("question_id"))
                res.data = jsonBody
            } catch (e: Exception) {
                res.code = 99999
                res.msg = "操作失败"
                res.data = null
            }
        }
        return setRepBody(headNode, res)
    }

    //创建回复
    fun CQ02(headNode: JSONObject, bodyNode: JSONObject): Any {
        if (!bodyNode.containsKey("question_id")) {
            return getErrorRep(SpcpCode.CQ02_NO_QUESTIONID)
        }
        if (!bodyNode.containsKey("content") && !bodyNode.containsKey("attachs")) {
            return getErrorRep(SpcpCode.CQ02_NO_CONTENT_OR_ATTACH)
        }
        bodyNode.put("uid", Random().nextInt(50000) + 1)
        bodyNode.put("app_id", SourceEntry.Third_Pingan.app_id)
        bodyNode.put("third_id", if (headNode.containsKey("outerid")) headNode.getString("outerid") else null)
        val res = qaCloud.addAnswer(bodyNode)
        if (res.code == 10000) {
            try {
                val jsonBody = JSONObject()
                jsonBody.put("answer_id", JSONObject.parseObject(res.data.toString()).getString("answer_id"))
                res.data = jsonBody
            } catch (e: Exception) {
                res.code = 99999
                res.msg = "操作失败"
                res.data = null
            }
        }
        return setRepBody(headNode, res)
    }

}

