package com.xyauto.qa.mapper.broker

import com.xyauto.qa.model.MaterielExt
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by shiqm on 2018-01-22.
 */

@Mapper
interface MaterielExtMapper : BaseMapper<MaterielExt> {

    fun updateCount(@Param("materiel_id") materiel_id: Long,@Param("count") count: Int): Int

    fun getSeriesName(@Param("ids") ids: List<String>): List<String>

    fun getSeriesInfo(@Param("ids") ids: List<String>): List<MutableMap<String,Any>>


}