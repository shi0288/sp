package com.xyauto.qa.util.aop

import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.mcp.fastcloud.util.exception.base.BaseException
import com.xyauto.qa.util.Beanstalkd
import com.xyauto.qa.util.annotation.CallbackJob
import com.xyauto.qa.util.enum.TaskQueue
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
@Aspect
class TaskCallbackAspect {


    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var beanstalkd: Beanstalkd


    @Pointcut("@annotation(com.xyauto.qa.util.annotation.CallbackJob)")
    fun point() {
    }

    @Around("point()")
    fun intercept(pjp: ProceedingJoinPoint) {
        val start_time = System.currentTimeMillis()
        val signature = pjp.signature as MethodSignature
        val method = signature.method
        val annotationInfo = method.getDeclaredAnnotation(CallbackJob::class.java)
        val parameters = signature.parameterNames
        val args = pjp.args
        var task_id: String? = null
        val paramByStr = parameters.foldIndexed(StringBuffer(), { index, acc, parameter ->
            if (parameter == "task_id") {
                task_id = args[index].toString()
            }
            acc.append("$parameter=${args[index]}  ")
        })
        logger.info("任务：${annotationInfo.value} 开启-->$paramByStr")
        try {
            pjp.proceed(args)
            logger.info("任务：${annotationInfo.value} 完成,task_id:$task_id")
            beanstalkd.send(TaskQueue.TASK_CALLBACK.value, JSONObject().apply {
                put("task_id", task_id)
                put("status", 1)
            }.toString())
        } catch (throwable: Throwable) {
            beanstalkd.send(TaskQueue.TASK_CALLBACK.value, JSONObject().apply {
                put("task_id", task_id)
                put("status", 0)
                put("start_time", start_time)
                put("end_time", System.currentTimeMillis())
                put("message", if (throwable is BaseException) {
                    throwable.code.toString()
                } else {
                    throwable.printStackTrace()
                })
            }.toString())
            logger.error("任务：${annotationInfo.value} 失败,task_id:$task_id")
            throwable.printStackTrace()
        }
    }

}