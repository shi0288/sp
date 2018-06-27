package com.xyauto.qa.controller

import com.alibaba.fastjson.JSONObject
import com.mcp.validate.annotation.Check
import com.xyauto.qa.cloud.QaCloud
import com.xyauto.qa.util.BaseController
import com.xyauto.qa.util.enum.SourceEntry
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * Created by shiqm on 2017-09-18.
 */

@RestController
@RequestMapping("baidu")
class BaiduController : BaseController() {

    @Autowired
    lateinit var qaCloud: QaCloud

    @RequestMapping("sync")
    fun notice(
            @Check(value = "question_id", required = false, defaultValue = StringUtils.EMPTY) question_id: String,
            @Check(value = "title", required = false, defaultValue = StringUtils.EMPTY) title: String,
            @Check(value = "content", required = false, defaultValue = StringUtils.EMPTY) content: String,
            @Check(value = "pictures", required = false, defaultValue = StringUtils.EMPTY) pictures: String,
            @Check(value = "key", required = true, defaultValue = StringUtils.EMPTY) key: String,
            @Check(value = "token", required = true, defaultValue = StringUtils.EMPTY) token: String
    ): Any {
        logger.info("百度问题推送->$question_id")
        val res = JSONObject()
        if (question_id.isEmpty()) {
            res.put("errno", 1)
            res.put("errmsg", "unsuccessful")
        } else {
            val params = HashMap<String, Any>().apply {
                put("uid", Random().nextInt(50000) + 1)
                put("content", if (content.isEmpty()) {
                    title
                } else {
                    content
                })
                put("category_id", 3)
                put("attachs", pictures)
                put("app_id", SourceEntry.Third_Baidu.app_id)
                put("third_id", question_id)
            }
            try {
                logger.info("百度问题推送->success->$question_id")
                qaCloud.add(params)
            } catch (e: Exception) {
                logger.error("百度问题推送服务add错误")
                e.printStackTrace()
            }
            res.put("errno", 0)
            res.put("errmsg", "success")
        }
        return res
    }


    @RequestMapping("answer/sync")
    fun noticeAnswer(
    ): Any {
        val res = JSONObject()
        res.put("errno", 0)
        res.put("errmsg", "success")
        return res
    }


}

