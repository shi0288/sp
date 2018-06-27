package com.xyauto.qa.mapper.qa

import com.xyauto.qa.model.UserDistributeLog
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by shiqm on 2017-12-15.
 */

@Mapper
interface UserDistributeLogMapper : BaseMapper<UserDistributeLog> {
    fun getDistributeCountByUid(
            @Param(value = "uids")  uids: List<Long>,
            @Param(value = "start") start: Long,
            @Param(value = "end") end: Long): MutableList<UserDistributeLog>
}