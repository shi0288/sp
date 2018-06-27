package com.xyauto.qa.cloud

import com.mcp.fastcloud.annotation.ServerName
import com.mcp.fastcloud.util.Result
import com.xyauto.qa.util.QaRequestInterceptor
import feign.Param
import feign.QueryMap
import feign.RequestLine

/**
 * Created by shiqm on 2017-08-28.
 */

@ServerName(value = "QCDQ-QA-SERVER", applyClass = QaRequestInterceptor::class)
interface QaCloud {

    //获取用户详情
    @RequestLine("GET /v1.0/user/{uid}?app_id=5d81db99484c0019cab240b3d04e9a4a")
    fun getUserDesc(@Param("uid") uid: Long): Result

    //最新问题列表
    @RequestLine("GET /v1.1/question/list")
    fun getQuestionList(@QueryMap params: Map<String, Any>): Result

    //添加问题
    @RequestLine("POST /v1.1/question/add1")
    fun add(@QueryMap params: Map<String, Any>): Result

    //添加回复
    @RequestLine("POST /v1.1/answer/add1")
    fun addAnswer(@QueryMap params: Map<String, Any>): Result


}