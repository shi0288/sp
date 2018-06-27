package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import org.apache.commons.lang.StringUtils
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Created by shiqm on 2017-09-15.
 */
@PoKo
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        var category_id: Int,

        var name: String = StringUtils.EMPTY
)