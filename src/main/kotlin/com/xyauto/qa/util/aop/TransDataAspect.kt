package com.xyauto.qa.util.aop

import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.parser.Feature
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.util.MD5
import com.xyauto.qa.util.annotation.Encryption
import com.xyauto.qa.util.assist.getErrorRep
import com.xyauto.qa.util.cons.InterfaceSource
import com.xyauto.qa.util.enum.SpcpCode
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by shiqm on 2017-10-16.
 */

@Component
@Aspect
class TransDataAspect {


    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var interfaceSource: InterfaceSource


    @Pointcut("@annotation(com.xyauto.qa.util.annotation.Encryption)")
    fun point() {
    }

    @Around("point()")
    fun intercept(pjp: ProceedingJoinPoint): Any {
        try {
            val signature = pjp.signature as MethodSignature
            val parameters = signature.parameterNames
            val args = pjp.args
            val index = parameters.indices.find { parameters[it] == "message" }
            if (index != null) {
                val message = args[index]
                logger.error("message:" + message.toString())
                val msgNode = JSONObject.parseObject(message.toString(),Feature.OrderedField)
                val head = msgNode.getJSONObject("head")
                val body = msgNode.getString("body")
                val timestamp = head["timestamp"]
                val source = head.getString("channel") ?: return getErrorRep(SpcpCode.RESPONSE_NO_CHANNEL_ERROR)
                val key = interfaceSource.source[source] ?: return getErrorRep(SpcpCode.RESPONSE_NO_CHANNEL_ERROR)
                val outerid = head.getString("outerid") ?: return getErrorRep(SpcpCode.RESPONSE_FORMAT_ERROR)
                val decodeStr = MD5.encode(body + timestamp + key + outerid)
                logger.error("decodeStr:" + decodeStr)
                return if (decodeStr == head["digest"]) {
                    pjp.proceed(args)
                } else {
                    getErrorRep(SpcpCode.RESPONSE_ENCODE_ERROR)
                }
            }
            return getErrorRep(SpcpCode.RESPONSE_FORMAT_ERROR)
        } catch (e: Exception) {
            e.printStackTrace()
            return getErrorRep(SpcpCode.RESPONSE_FORMAT_ERROR)
        }
    }

}


