package com.xyauto.qa.model

import com.alibaba.fastjson.JSON
import com.xyauto.qa.util.annotation.PoKo
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Transient

/**
 * Created by shiqm on 2017-12-15.
 */

@PoKo
data class UserDistributeLog(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        var question_id: Long? = null,
        var uid: Long? = null,
        var has_answered: Int? = null,
        var created_at: Int = 0,
        @Transient
        var count: Int
)
