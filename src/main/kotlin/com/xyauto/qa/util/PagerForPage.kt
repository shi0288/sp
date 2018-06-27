package com.xyauto.qa.util


/**
 * Created by shiqm on 2018-02-13.
 */

data class PagerForPage<T>(
        var page: Int? = null,
        var pages: Int? = null,
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