package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by shiqm on 2017-12-01.
 */
@PoKo
data class SpiderQuestion(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "rec_id")
        var recId: Long? = null,

        var status: Short? = null,

        var source: Int? = null,

        var content: String? = null,

        @Column(name = "created_at")
        var createdAt: Int? = null,

        @Column(name = "update_at")
        var updateAt: Int? = null
)