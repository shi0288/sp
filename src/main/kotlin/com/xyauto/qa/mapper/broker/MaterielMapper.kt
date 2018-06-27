package com.xyauto.qa.mapper.broker

import com.github.pagehelper.Page
import com.xyauto.qa.model.Broker
import com.xyauto.qa.model.Materiel
import com.xyauto.qa.util.BaseMapper
import org.apache.commons.lang.mutable.Mutable
import org.apache.ibatis.annotations.MapKey
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shiqm on 2018-01-22.
 */

@Mapper
interface MaterielMapper : BaseMapper<Materiel> {

    fun getTimerRabbitMateriel(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date): Page<Materiel>

    fun getTimerMasterMateriel(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date): Page<Materiel>

    fun getTimerQaMateriel(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date): Page<Materiel>

    fun getActualQaMateriel(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date): Page<Materiel>

    fun getActualNewsMateriel(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date): Page<Materiel>

    fun getUidBySeriesIdsCount(@Param("serialIds") serialIds: List<String>): Int

    fun getUidByCityIdCount(@Param("cityIds") cityIds: List<String>): Int

    fun getAllUidCount(): Int

    fun getUidBySeriesIds(@Param("serialIds") serialIds: List<String>): List<Broker>

    fun getUidByCityId(@Param("cityIds") cityIds: List<String>): List<Broker>

    fun getAllUid(): List<Broker>

    @MapKey("broker_id")
    fun getAllBrokerIds(): MutableMap<Long,String>

    fun getMaterielByIds(@Param("ids") ids: List<Long>): List<MutableMap<String, Any>>

    fun getMaterielById(@Param("materiel_id") materiel_id:Long):MutableMap<String,Any>?

    fun getCityIdsByParentId(@Param("ids") ids: List<String>): MutableList<String>

    fun getMaterielTemplateIdByTime(@Param("start_time") startTime: Date, @Param("end_time") endTime: Date):List<Materiel>

    fun getDealerIdsByTemplateId(@Param("template_id") templateId: Long):List<Long>

    fun getOriginDistributeCount(@Param("ids") ids: Set<String>): Long



}