package com.xyauto.qa.util

import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.mcp.fastcloud.util.Result
import com.mcp.fastcloud.util.enums.Level
import com.mcp.fastcloud.util.enums.ResultCode
import com.mcp.fastcloud.util.exception.base.BaseException
import com.mcp.validate.exception.ValidateException
import org.apache.commons.lang.exception.ExceptionUtils
import org.slf4j.Logger
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * Created by shiqm on 2017-09-18.
 */
open class BaseController {

    @Log
    lateinit var logger: Logger

    @ExceptionHandler(Exception::class)
    fun handleException(req: HttpServletRequest, ex: Exception): Any {
        val curEx = ExceptionUtils.getRootCause(ex) ?: ex
        ex.printStackTrace()
        val result: Result
        when (curEx) {
            is BaseException -> {
//                when (curEx.level) {
//                    Level.INFO -> logger.info(curEx.code.toString())
//                    Level.WARN -> logger.warn(curEx.code.toString())
//                    Level.DEBUG -> logger.debug(curEx.code.toString())
//                    Level.ERROR -> logger.error(curEx.code.toString())
//                    else -> logger.error(curEx.code.toString())
//                }
                result = Result(curEx.code)
            }
            is MissingServletRequestParameterException -> {
                //this.logger.warn("请求:" + req.requestURI + " 缺少参数：" + curEx.parameterName)
                result = Result(ResultCode.ERROR_PARAMS)
            }
            is NumberFormatException -> {
                curEx.printStackTrace()
                //this.logger.warn("请求:" + req.requestURI + " 参数类型错误" )
                result = Result(9999,"传入的数据类型有误")
            }
            is ValidateException -> {
                val bindResult = curEx.bindResult
                //this.logger.warn("请求:" + req.requestURI + " 缺少参数：" + bindResult.field)
                result = Result(9999, bindResult.message)
            }
            else -> {
                //this.logger.error(ExceptionUtils.getStackTrace(curEx))
                result = Result(ResultCode.OVER)
            }
        }
        return result
    }


    fun setRepBody(headNode: JSONObject, body: Result): JSONObject {
        val reqHead = JSONObject()
        reqHead.apply {
            put("messageid", headNode.getString("messageid"))
            put("cmd", headNode.getString("cmd"))
            put("channel", headNode.getString("channel"))
            put("outerid", headNode.getString("outerid"))
            put("version ", "1.0.00")
            put("timestamp", SimpleDateFormat("YYYYMMddHHmmss").format(Date()))
        }
        val rep = JSONObject()
        rep.apply {
            put("head", reqHead)
            put("body", body)
        }
        return rep
    }


}

