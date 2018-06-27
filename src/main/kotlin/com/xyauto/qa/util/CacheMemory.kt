package com.xyauto.qa.util

import java.util.concurrent.ConcurrentHashMap


/**
 * Created by shiqm on 2018-04-16.
 */
object CacheMemory {

    var brokerMemory: MutableMap<String, ConcurrentHashMap<Long,String?>> = ConcurrentHashMap()


    var shieldMemory: ArrayList<String> = ArrayList()



}