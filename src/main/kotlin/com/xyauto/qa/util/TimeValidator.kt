package com.xyauto.qa.util

import com.mcp.validate.BaseValidator
import com.mcp.validate.BindResult
import com.mcp.validate.annotation.Check
import com.mcp.validate.exception.ValidateException

/**
 * Created by shiqm on 2017-12-15.
 */

class TimeValidator : BaseValidator {

    override fun validate(p0: Check?, p1: String?, p2: Any?) {
        if (p2.toString().length > 10) {
            throw ValidateException(BindResult(p1, p2, "时间长度只允许10位以下的时间戳"))
        }
    }
}