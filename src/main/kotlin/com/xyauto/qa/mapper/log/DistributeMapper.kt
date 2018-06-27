package com.xyauto.qa.mapper.log

import com.github.pagehelper.Page
import com.xyauto.qa.model.Distribute
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider
import java.util.*

/**
 * Created by shiqm on 2018-01-22.
 */

@Mapper
interface DistributeMapper : BaseMapper<Distribute> {

    fun getUserCount(@Param("distribute") distribute: Distribute, @Param("task_tag") task_tag: Short?, @Param("start_time") start_time: Date, @Param("end_time") end_time: Date): Int

    fun getDistributeCount(@Param("materiel_id") materiel_id: Long): Int

    fun getDistributeDealerCount(@Param("materiel_id") materiel_id: Long): Int

    fun getDistributeIdsCount(@Param("broker_id") broker_id: Long, @Param("max") max: Long = 0, @Param("source") source: Int? = 0): Int

    fun getDistributeIds(@Param("broker_id") broker_id: Long, @Param("max") max: Long? = 0, @Param("source") source: Int? = 0): Page<Distribute>

    fun del(@Param("broker_id") broker_id: Long, @Param("materiel_id") materiel_id: Long): Int

    fun delByDealer(@Param("dealer_id") dealer_id: Long, @Param("materiel_id") materiel_id: Long): Int


}