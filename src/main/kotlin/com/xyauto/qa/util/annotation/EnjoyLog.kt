package com.xyauto.qa.util.annotation


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnjoyLog(
        val value: String,
        val broker_id: String = "broker_id",
        val dealer_id: String = "",
        val target_id: String
)