package com.xyauto.qa.model

import com.alibaba.fastjson.JSONObject
import com.xyauto.qa.util.annotation.PoKo
import org.apache.commons.lang.StringUtils
import java.util.ArrayList
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Transient


/**
 * Created by shiqm on 2017-09-14.
 */
@PoKo
data class Question(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var question_id: Long? = null,

        var uid: Long = 0,

        var third_id: String = StringUtils.EMPTY,

        var category_id: Short = 0,

        var content: String = StringUtils.EMPTY,

        var has_attach: Short = 0,

        var answer_count: Int = 0,

        var answer_users_count: Int = 0,

        var source: Int = 0,

        var is_good: Short = 0,

        var good_sort: Int = 0,

        var is_top: Short = 0,

        var top_sort: Int = 0,

        var lng: Float = 0.toFloat(),

        var lat: Float = 0.toFloat(),

        var city_id: Int = 0,

        var status: Int = 1,

        var first_answerd_at: Int = 0,

        var created_at: Int = 0,

        var updated_at: Int = 0,

        var deleted_at: Int = 0,

        var key: String = StringUtils.EMPTY,

        var type: Short = 0,

        var agree_count: Int = 0,

        //extension data

        var sort: String = StringUtils.EMPTY,

        var category_name: String = StringUtils.EMPTY,

        var attachs: Any? = null,

        var vote: Any? = null,

        var distribute: Any? = null,

        var activity: Any? = null,

        var city: Any = JSONObject(),

        var user: Any = JSONObject(),

        @Transient
        var cars: List<*> = ArrayList<Any>(),

        var parent_category_id: Short = 0,

        var parent_category_name: String = StringUtils.EMPTY,

        var has_expert_answered: Short = 0,

        var has_favorite: Short = 0,

        var share: Any = JSONObject(),

        var share_url: String = StringUtils.EMPTY,

        var attach_type: Short = 0,

        var enable_share: Short = 0,

        var keywords: String = StringUtils.EMPTY,

        var has_agree: Int = 0


)