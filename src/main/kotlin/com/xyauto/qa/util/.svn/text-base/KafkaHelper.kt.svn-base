package com.xyauto.qa.util

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Created by shiqm on 2018-03-31.
 */

@Component
class KafkaHelper(var kafkaTemplate: KafkaTemplate<String, Any>) {
//
//    @Log
//    lateinit var logger: Logger

    fun send(topic: String, content: String) {
        val future = kafkaTemplate.send(topic, content)
//        future.addCallback(
//                { o -> logger!!.info("send-消息发送成功：" + content) }
//        ) { throwable -> logger!!.error("消息发送失败：" + content) }
    }

    fun send(topic: String, key: String, content: String) {
        val future = kafkaTemplate.send(topic, key, content)
//        future.addCallback(
//                { o -> logger!!.info("send-消息发送成功：" + content) }
//        ) { throwable -> logger!!.error("消息发送失败：" + content) }
    }


}