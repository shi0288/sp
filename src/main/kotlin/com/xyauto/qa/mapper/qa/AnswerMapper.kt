package com.xyauto.qa.mapper.qa

import com.github.pagehelper.Page
import com.xyauto.qa.model.Answer
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by shiqm on 2017-09-18.
 */

@Mapper
interface AnswerMapper : BaseMapper<Answer> {

    fun getByThird(@Param(value = "third_id") third_id: String, @Param(value = "source") source: Int): Answer?

    fun add(answer: Answer): Long

    fun updateThirdId(@Param(value = "third_id") third_id: String, @Param(value = "answer_id") answer_id: Long)

    fun getListByCreatedAtCount(@Param(value = "source") source: Int, @Param(value = "start") start: Long, @Param(value = "end") end: Long): Int

    fun getListByCreatedAt(@Param(value = "source") source: Int, @Param(value = "start") start: Long, @Param(value = "end") end: Long): Page<Answer>

    fun getListByCreateAtCountOnFirst(@Param(value = "source") source: Int, @Param(value = "start") start: Long, @Param(value = "end") end: Long):Int

    fun getListByCreateAtOnFirst(@Param(value = "source") source: Int, @Param(value = "start") start: Long, @Param(value = "end") end: Long):Page<Answer>


}