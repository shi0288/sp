package com.xyauto.qa.mapper.qa

import com.xyauto.qa.model.Question
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * Created by shiqm on 2017-09-14.
 */

@Mapper
interface QuestionMapper : BaseMapper<Question> {

    fun getLastestThirdId(@Param(value = "source") source: Int): String

    fun getByThird(@Param(value = "third_id") third_id: String, @Param(value = "source") source: Int): Question?

    fun add(question: Question)

    fun incAnswerCount(@Param(value = "question_id") question_id: Long)

    fun recountAnswerUserCount(@Param(value = "question_id") question_id: Long)

    fun recountAnswerCount(@Param(value = "uid") uid: Long)


}

