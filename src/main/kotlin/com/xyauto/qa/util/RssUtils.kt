package com.xyauto.qa.util

import com.alibaba.fastjson.JSONArray
import com.sun.syndication.feed.rss.Channel
import com.sun.syndication.feed.rss.Description
import com.sun.syndication.feed.rss.Item
import com.sun.syndication.io.FeedException
import com.sun.syndication.io.WireFeedOutput
import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.nio.charset.Charset
import java.util.*


/**
 * Created by shiqm on 2017-08-25.
 */


object RssUtils {


    fun rssQuestionString(map: Map<String, String>) = fun(arrays: JSONArray) = rssQuestionString(map, arrays)

    private fun rssQuestionString(map: Map<String, String>, arrays: JSONArray) {
        var channel = Channel("rss_2.0").apply {
            encoding = "UTF-8"
            language = "ZH_CN"
            title = map["title"]
            description = map["description"]
            link = map["link"]
            ttl = 5  //time to live的简写，在刷新前当前RSS在缓存中可以保存多长时间（分钟）
        }
        var items = ArrayList<Item>()
        arrays.forEach {
            var item = Item()
            var contentMap = it as Map<*, *>
            if (!StringUtils.isEmpty(contentMap["content"])) {
                var description = Description()
                description.value = contentMap["content"].toString()
                item.title = description.value
                item.description = description
            }
            if (!StringUtils.isEmpty(contentMap["key"])) {
                item.link = "http://ask.qichedaquan.com/detail/" + contentMap["key"].toString()
            }
            if (!StringUtils.isEmpty(contentMap["created_at"])) {
                item.pubDate = Date(java.lang.Long.valueOf(contentMap["created_at"].toString() + "000"))
            }
            items.add(item)
        }
        channel.items = items
        outChannel(channel)
    }


    private fun outChannel(channel: Channel) {
        val out = WireFeedOutput()
        var rssString = ""
        try {
            rssString = out.outputString(channel)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: FeedException) {
            e.printStackTrace()
        }
        val ra = RequestContextHolder.getRequestAttributes()
        val sra = ra as ServletRequestAttributes
        val outputStream = sra.response.outputStream
        outputStream.write(rssString.toByteArray(Charset.forName("UTF-8")))
    }

}

