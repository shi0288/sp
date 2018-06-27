package com.xyauto.qa.mapper.qa

import com.xyauto.qa.model.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update
import com.xyauto.qa.util.BaseMapper

/**
 * Created by shiqm on 2017-09-15.
 */

@Mapper
interface UserMapper :BaseMapper<User>{

    @Update("update user set question_count = (select count(question_id) from question where uid=#{uid} and deleted_at = 0 and status>0), updated_at = unix_timestamp(now()) where uid = #{uid}")
    fun recountQuestionCount(@Param(value = "uid") uid:Long)

}