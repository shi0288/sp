package com.xyauto.qa.mapper.broker

import com.xyauto.qa.model.Broker
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by shiqm on 2018-02-27.
 */
@Mapper
interface BrokerMapper : BaseMapper<Broker>{

    fun getBrokerByIds(@Param("ids") ids: List<Long>):List<Broker>

    fun getSeriesByBrokerId(@Param("broker_id") broker_id: Long):List<Long>

    fun getReal():List<MutableMap<String,Any>>

    fun getBrokerByDealerId(@Param("dealer_id") dealer_id: Long):List<Broker>



}