package com.xyauto.qa.service

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.mapper.qa.UserDistributeLogMapper
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by shiqm on 2017-12-15.
 */

@Service
class UserDistributeLogService {

    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var userDistributeLogMapper: UserDistributeLogMapper

    fun getUserDistributeLostByUids(uids: List<Long>, start: Long, end: Long): JSONArray {
        val result = userDistributeLogMapper.getDistributeCountByUid(uids, start, end)
        val map = JSONArray()
        uids.forEach { uid ->
            val item = JSONObject()
            item.put("uid", uid)
            item.put("count", 0)
            run {
                result.forEachIndexed { index, distributeLog ->
                    if (distributeLog.uid == uid) {
                        item.put("count", distributeLog.count)
                        result.removeAt(index)
                        return@run
                    }
                }
            }
            map.add(item)
        }
        return map
    }

}