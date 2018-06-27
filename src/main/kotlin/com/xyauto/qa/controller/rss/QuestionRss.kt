package com.xyauto.qa.controller.rss

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.xyauto.qa.cloud.QaCloud
import com.xyauto.qa.util.RssUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Created by shiqm on 2017-08-25.
 */

@RestController
class QuestionRss {

    @Autowired
    lateinit var qaCloud: QaCloud

    @RequestMapping("question.xml")
    fun questionXml() {
        val result = qaCloud.getQuestionList(mapOf(
                "app_id" to "9493876a020e5523ce51d6cae6c684e4"
        ))
        val items = (result.data as JSONObject)["list"] as JSONArray
        val headers = mapOf(
                "title" to "问答",
                "link" to "http://www.qichedaquan.com",
                "description" to "问答最新信息"
        )
        RssUtils.rssQuestionString(headers)(items)
    }


}

