package com.xyauto.qa.model

import com.alibaba.fastjson.JSONObject
import com.xyauto.qa.util.annotation.PoKo
import org.apache.commons.lang.StringUtils
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Transient

/**
 * Created by shiqm on 2017-09-18.
 */
@PoKo
data class Answer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var answer_id: Long? = null,

        var question_id: Long = 0,

        var uid: Long = 0,

        var content: String = StringUtils.EMPTY,

        var agree_count: Int = 0,

        var has_attach: Short = 0,

        var source: Int = 0,

        var reply_uid: Long = 0,

        var reply_answer_id: Long = 0,

        var city_id: Int = 0,

        var created_at: Int = 0,

        var updated_at: Int = 0,

        var deleted_at: Int = 0,

        var third_id: String = StringUtils.EMPTY,

        //extension data
        var sort: String = StringUtils.EMPTY,

        @Transient
        var question: Question? = null,

        @Transient
        var user: User? = null,

        var reply: Any? = null,

        var attachs: Any? = null,

        var city: Any = JSONObject(),

        var has_agree: Int = 0

)