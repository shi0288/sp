package com.xyauto.qa.util.aop

import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.mapper.broker.BrokerMapper
import com.xyauto.qa.util.KafkaHelper
import com.xyauto.qa.util.annotation.EnjoyLog
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
class EnjoyLogAspect {


    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var kafkaHelper: KafkaHelper

    @Autowired
    lateinit var brokerMapper: BrokerMapper


    @Pointcut("@annotation(com.xyauto.qa.util.annotation.EnjoyLog)")
    fun point() {
    }

    @Around("point()")
    fun intercept(pjp: ProceedingJoinPoint): Any {
        val signature = pjp.signature as MethodSignature
        val method = signature.method
        val annotationInfo = method.getDeclaredAnnotation(EnjoyLog::class.java)
        val message = annotationInfo.value
        val parameters = signature.parameterNames
        val args = pjp.args
        try {
            var broker_id: String? = null
            var target_id: String? = null
            var dealer_id: String? = null
            parameters.forEachIndexed({ index, parameter ->
                if (parameter == annotationInfo.broker_id) {
                    broker_id = args[index].toString()
                }
                if (parameter == annotationInfo.target_id) {
                    target_id = args[index].toString()
                }
                if (parameter == annotationInfo.dealer_id) {
                    dealer_id = args[index].toString()
                }
            })
            if (dealer_id.isNullOrEmpty()) {
                val broker = brokerMapper.selectByPrimaryKey(broker_id!!.toLong())
                dealer_id = broker.dealerId.toString()
            }
            val json = JSONObject()
            json.put("brokerId",broker_id)
            json.put("targetId",target_id)
            json.put("dealerId",dealer_id)
            json.put("message",message)
            logger.info(json.toString())
            kafkaHelper.send("interact_broker_operation_topic", "broker_materiel_view", json.toString())
        }catch (e:Exception){
            e.printStackTrace()
        }
        return pjp.proceed(args)
    }

}