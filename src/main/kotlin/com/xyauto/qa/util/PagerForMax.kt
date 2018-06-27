package com.xyauto.qa.util

import org.apache.commons.lang.StringUtils

/**
 * Created by shiqm on 2018-02-12.
 */
data class PagerForMax<T>(
        var has_more: Int = 0,
        var next_max: Any? = StringUtils.EMPTY,
        var limit: Int? = null,
        var count: Long? = null,
        var list: List<Any>? = null,
        @Transient
        var params: Map<String, Any>? = null,
        @Transient
        var ids: List<T>? = null
) : Pager<T> {
    override fun putList(list: List<Any>) {
        this.list = list
    }

    override fun takeIds(): List<T>? = ids

    override fun params(): Map<String, Any>? =params

}