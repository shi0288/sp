package com.xyauto.qa.mapper.qa

import com.xyauto.qa.model.Category
import org.apache.ibatis.annotations.Mapper
import com.xyauto.qa.util.BaseMapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Update

/**
 * Created by shiqm on 2017-09-15.
 */

@Mapper
interface CategoryMapper :BaseMapper<Category>{

    @Update("update category c, category p set c.question_count = c.question_count + 1, p.question_count =p.question_count + 1 where p.category_id = c.parent and (c.category_id = #{category_id} or p.parent = #{category_id})")
    fun incQuestionCount(@Param(value = "category_id") category_id:Int)

}