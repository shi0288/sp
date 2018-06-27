package com.xyauto.qa.util.enum

import com.mcp.fastcloud.util.Code

/**
 * Created by shiqm on 2017-09-14.
 */
enum class SpcpCode constructor(var repCode: Int,var message: String) : Code {
    ERROR(9999, "系统错误"),
    TASK_360_RESPONSE(101, "360响应码错误"),
    RESPONSE_ENCODE_ERROR(8000, "校验签名失败"),
    RESPONSE_FORMAT_ERROR(8001, "数据格式错误"),
    RESPONSE_NO_METHOD_ERROR(8002, "命令不存在"),
    RESPONSE_NO_CHANNEL_ERROR(8003, "交互渠道不存在"),
    CQ01_NO_CONTENT(8004, "内容不能为空"),
    CQ01_CONTENT_KEY(8007, "内容内有敏感词"),
    CQ02_NO_QUESTIONID(8005, "问题ID不能为空"),
    CQ02_NO_CONTENT_OR_ATTACH(8006, "内容和图片不能同时为空"),
    Materiel(1001, "物料处理有误");


    var tag: String? = null

    operator fun invoke(value: String): Code {
        this.tag = value
        return this
    }

    override fun toString(): String {
        if (this.tag == null) {
            return "code:$repCode,message:$message"
        }
        return "code:$repCode,message:$message,tag:$tag"
    }

    override fun getCode(): Int = repCode

    override fun getMsg(): String = message
}

