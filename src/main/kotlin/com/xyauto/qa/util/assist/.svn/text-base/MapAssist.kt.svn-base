package com.xyauto.qa.util.assist

import com.xyauto.qa.util.MD5
import java.util.ArrayList

/**
 * Created by shiqm on 2017-09-15.
 */

/**
 * 分割http参数
 */
fun paramOf(vararg pairs: kotlin.Pair<String, String>): String {
    var paramsAsString = ArrayList<String>()
    pairs.toMap().toSortedMap().forEach {
        paramsAsString.add(it.toString())
    }
    return paramsAsString.joinToString("&")
}


/**
 * 加密360参数
 */
fun paramOf360(url: String, secret: String, vararg pairs: kotlin.Pair<String, String>): String {
    val paramsAsString = paramOf(*pairs)
    val sign = MD5.encode("$paramsAsString&$secret")
    return "$url?$paramsAsString&sign=$sign"
}

