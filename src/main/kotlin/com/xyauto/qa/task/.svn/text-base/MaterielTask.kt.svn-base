package com.xyauto.qa.task

import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.service.MaterielService
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.cons.BrokerCons
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Scheduled
import java.text.SimpleDateFormat
import java.util.*
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger
import org.springframework.stereotype.Component


/**
 * Created by shiqm on 2018-01-25.
 */
@Component
class MaterielTask : CommandLineRunner {

    @Autowired
    lateinit var materielService: MaterielService

    @Autowired
    lateinit var threadPoolTaskScheduler: ThreadPoolTaskScheduler

    @Autowired
    lateinit var cache: Cache

    @Log
    lateinit var logger: Logger


    override fun run(vararg args: String?) {
        /**
         * 定时  9:00
         */
        //赤兔
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_9, "00:00:00", "09:00:00")
        }, CronTrigger("0 0 9 * * ? "))
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerMasterTask(BrokerCons.DistributeTaskFlag.TIME_9, "00:00:00", "09:00:00")
        }, CronTrigger("0 0 9 * * ? "))
        /**
         * 定时  12:00
         */
        //赤兔
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_12, "09:00:00", "12:00:00")
        }, CronTrigger("0 0 12 * * ? "))
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerMasterTask(BrokerCons.DistributeTaskFlag.TIME_12, "09:00:00", "12:00:00")
        }, CronTrigger("0 0 12 * * ? "))
        /**
         * 定时  15:00
         */
        //赤兔
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_15, "12:00:00", "15:00:00")
        }, CronTrigger("0 0 15 * * ? "))
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerMasterTask(BrokerCons.DistributeTaskFlag.TIME_15, "12:00:00", "15:00:00")
        }, CronTrigger("0 0 15 * * ? "))
        /**
         * 定时  17:30
         */
        //赤兔
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_17_30, "15:00:00", "17:30:00")
        }, CronTrigger("0 0 18 * * ? "))
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerMasterTask(BrokerCons.DistributeTaskFlag.TIME_17_30, "15:00:00", "17:30:00")
        }, CronTrigger("0 0 18 * * ? "))
        /**
         * 定时  20:00
         */
        //赤兔
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_20, "17:30:00", "20:00:00")
        }, CronTrigger("0 0 20 * * ? "))
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleTimerMasterTask(BrokerCons.DistributeTaskFlag.TIME_20, "17:30:00", "20:00:00")
        }, CronTrigger("0 0 20 * * ? "))

        /**
         * 实时
         */
        //大全
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleActualQaTask()
        }, CronTrigger("0 0/5 * * * ? "))
        //行园慧
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleActualNewsTask()
        }, CronTrigger("0 0/5 * * * ? "))
        //定投模板
        threadPoolTaskScheduler.schedule(Runnable {
            this.handleActualTemplateTask()
        }, CronTrigger("0 0/5 * * * ? "))

    }

    /**
     * 赤兔---定时
     */
    fun handleTimerRabbitTask(taskFlag: Short, start: String, end: String) {
        val keyLock = "broker:sync:sp:handleTimerRabbitTask"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 300L)
        if (flag) {
            logger.info("赤兔---定时")
            var timeFormat = SimpleDateFormat("yyyy-MM-dd")
            val day = timeFormat.format(Date())
            val startTime = "$day $start"
            val endTime = "$day $end"
            timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            materielService.handleTimerRabbit(timeFormat.parse(startTime), timeFormat.parse(endTime), taskFlag)
        }
    }

    /**
     * 大全---定时
     */
    fun handleTimerMasterTask(taskFlag: Short, start: String, end: String) {
        val keyLock = "broker:sync:sp:handleTimerMasterTask"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 300L)
        if (flag) {
            logger.info("大全---定时")
            var timeFormat = SimpleDateFormat("yyyy-MM-dd")
            val day = timeFormat.format(Date())
            val startTime = "$day $start"
            val endTime = "$day $end"
            timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            materielService.handleTimerMaster(timeFormat.parse(startTime), timeFormat.parse(endTime), taskFlag)
        }
    }

    /**
     * 大全--问答--实时
     */
    fun handleActualQaTask() {
        val keyLock = "broker:sync:sp:master:actual"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 30L)
        val key = "broker:sync:sp:master:actual:time"
        var startValue = cache[key] ?: 0
        val start: Long = startValue.toString().toLong()
        var end = System.currentTimeMillis()-60000
        logger.info("大全--问答--实时")
        logger.info("start:$start   end:$end")
        if (flag && start < end) {
            if (cache.set(key, end.toString())) {
                cache.remove(keyLock)
                materielService.handleActualQa(Date(start), Date(end))
            }
        } else {
            logger.info("condition error: flag=$flag ,start < end=${start < end}")
        }
    }

    /**
     * 行圆慧--新闻--实时
     */
    fun handleActualNewsTask() {
        val keyLock = "broker:sync:sp:news:actual"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 30L)
        val key = "broker:sync:sp:news:actual:time"
        var startValue = cache[key] ?: 0
        val start: Long = startValue.toString().toLong()
        var end = System.currentTimeMillis()-60000
        logger.info("行圆慧--新闻--实时")
        logger.info("start:$start   end:$end")
        if (flag && start < end) {
            if (cache.set(key, end.toString())) {
                cache.remove(keyLock)
                materielService.handleActualNews(Date(start), Date(end))
            }
        }else {
            logger.info("condition error: flag=$flag ,start < end=${start < end}")
        }
    }


    /**
     * 定投模板---实时
     */
    fun handleActualTemplateTask() {
        val keyLock = "broker:sync:sp:template:actual"
        val flag = cache.setnx(keyLock, System.currentTimeMillis().toString(), 30L)
        val key = "broker:sync:sp:template:actual:time"
        var startValue = cache[key] ?: 0
        val start: Long = startValue.toString().toLong()
        var end = System.currentTimeMillis()-60000
        logger.info("定投模板---实时")
        logger.info("start:$start   end:$end")
        if (flag && start < end) {
            if (cache.set(key, end.toString())) {
                cache.remove(keyLock)
                materielService.handleActualTemplate(Date(start), Date(end))
            }
        }else {
            logger.info("condition error: flag=$flag ,start < end=${start < end}")
        }
    }


}