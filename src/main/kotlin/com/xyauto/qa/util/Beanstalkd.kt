package com.xyauto.qa.util

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import com.surftools.BeanstalkClientImpl.ClientImpl
import org.springframework.scheduling.annotation.Async


@Service
@ConfigurationProperties(prefix = "xyauto.qa.beanstalkd")
class Beanstalkd {

    lateinit var host: String
    lateinit var port: String

    fun send(queueName: String, message: String) {
        ClientImpl(host, port.toInt()).apply {
            useTube(queueName)
            put(11, 0, 100, message.toByteArray())
        }.close()
    }

    fun send(queueName: String, message: String, times: Long) {
        ClientImpl(host, port.toInt()).apply {
            useTube(queueName)
            put(11, times.toInt(), 100, message.toByteArray())
        }.close()
    }

    @Async
    fun receive(queueName: String, callback: (String) -> Unit) {
        val client = ClientImpl(host, port.toInt())
        client.watch(queueName)
        while (true) {
            val job = client.reserve(10)
            if (job == null) {
                Thread.sleep(1000L)
                continue
            }
            client.delete(job.jobId)
            val data = String(job.data)
            callback(data)
        }
    }


}