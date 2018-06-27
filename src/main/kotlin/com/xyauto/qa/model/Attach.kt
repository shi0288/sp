package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@PoKo
data class Attach(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var attach_id: Long? = null,

        var file: Any? = null,

        var file_type: Short? = null,

        var target_id: Long? = null
)