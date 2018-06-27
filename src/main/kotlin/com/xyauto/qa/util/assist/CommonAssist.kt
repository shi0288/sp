package com.xyauto.qa.util.assist

import com.alibaba.fastjson.JSONObject
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.xyauto.qa.model.Distribute
import com.xyauto.qa.model.Materiel
import com.xyauto.qa.util.Pager
import com.xyauto.qa.util.PagerForMax
import com.xyauto.qa.util.cons.BrokerCons
import com.xyauto.qa.util.enum.SpcpCode
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*
import java.util.concurrent.ExecutorService

/**
 * Created by shiqm on 2017-11-01.
 */

fun getErrorRep(spcpCode: SpcpCode): JSONObject {
    val rep = JSONObject()
    rep.apply {
        put("head", JSONObject().apply {
            put("cmd", "E01")
        })
        put("body", JSONObject().apply {
            put("code", spcpCode.repCode)
            put("description", spcpCode.message)
        })
    }
    return rep
}


fun businessByPage(exe: ExecutorService, count: Int, limit: Int = 20, block: () -> Unit) {
    if (count > 0) {
        val pageNum = count / limit + if (count % limit == 0) 0 else 1
        for (i in 1..pageNum) {
            exe.execute({
                //当前处理的页码
                PageHelper.startPage<Any>(i, limit, false)
                block()
            })
        }
    }
}

fun businesstByListPage(exe: ExecutorService, brokerIdsList: List<Long>, limit: Int = 20, block: (List<Long>) -> Unit) {
    var count = brokerIdsList.size
    if (count > 0) {
        val pageNum = count / limit + if (count % limit == 0) 0 else 1
        for (i in 0 until pageNum) {
            var lastIndex = (i + 1) * limit
            if (count < lastIndex) {
                lastIndex = count
            }
            exe.execute({
                //当前处理的页码
                block(brokerIdsList.subList(i * limit, lastIndex))
            })
        }
    }
}


fun <T> getPager(map: Map<String, Any>, max: String, limit: Int, block: (Map<String, Any>, String, Int) -> Pager<T>, toOuter: (Pager<T>) -> Pager<T>): Pager<T> =
        toOuter(block(map, max, limit))

fun <T> getPager(map: Map<String, Any>, page: Int, limit: Int, block: (Map<String, Any>, Int, Int) -> Pager<T>, toOuter: (Pager<T>) -> Pager<T>): Pager<T> =
        toOuter(block(map, page, limit))


fun getFileWriter(path: String): BufferedWriter {
    val file = File(path)
    val os = FileOutputStream(file)
    val writer = OutputStreamWriter(os)
    return BufferedWriter(writer)
}