package com.xyauto.qa.util

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest


/**
 * Created by shiqm on 2017-08-28.
 */

@Component
class QaRequestInterceptor : RequestInterceptor {

    override fun apply(input: RequestTemplate) {
        input.query(true, "sign", "abcd")
        input.query(true, "version", "2.1")
    }

}