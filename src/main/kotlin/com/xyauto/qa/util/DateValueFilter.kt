package com.xyauto.qa.util

import com.alibaba.fastjson.serializer.ValueFilter
import java.util.*

/**
 * Created by shiqm on 2018-02-26.
 */
class DateValueFilter : ValueFilter {

    override fun process(`object`: Any?, name: String?, value: Any?): Any? {
        if (name == "create_time" || name=="update_time") {
            if(value is Date){
                return value.time/1000
            }
        }
        return value
    }
}