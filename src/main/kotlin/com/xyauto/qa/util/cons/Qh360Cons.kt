package com.xyauto.qa.util.cons

import com.xyauto.qa.util.annotation.PoKo
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Created by shiqm on 2017-11-01.
 */

@Component
@ConfigurationProperties(prefix = "xyauto.qa.qh360")
@PoKo
data class Qh360Cons(
        var url: String,
        var key: String,
        var secret: String
        )