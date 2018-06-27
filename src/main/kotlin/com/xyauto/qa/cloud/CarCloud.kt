package com.xyauto.qa.cloud

import com.mcp.fastcloud.annotation.ServerName
import com.mcp.fastcloud.util.Result
import feign.Param
import feign.RequestLine

/**
 * Created by shiqm on 2018-01-22.
 */

@ServerName(value = "QCDQ-CAR-SERVER")
interface CarCloud {

    /**
     * 获取品牌信息
     */
    @RequestLine("GET /carmaster/masterlist?version=1.0&method=car.getallmaster")
    fun getBrand(): Result


    /**
     * 获取子品牌和车型信息
     */
    @RequestLine("GET /carmaster/seriallist/{id}?version=1.0&method=car.getmasterserial")
    fun getSubBrand(@Param("id") id: Long): Result


    /**
     * 获取车款列表
     */
    @RequestLine("GET /carparm/queryCarparam?version=1.0&method=car.getallmaster&serialid={serialid}")
    fun getCar(@Param("serialid") serialid: Long): Result



}