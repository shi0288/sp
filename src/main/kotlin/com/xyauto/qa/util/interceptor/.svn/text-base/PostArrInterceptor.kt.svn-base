//package com.xyauto.qa.util.interceptor
//
//import org.apache.ibatis.executor.Executor
//import org.apache.ibatis.mapping.MappedStatement
//import org.apache.ibatis.plugin.*
//import org.springframework.stereotype.Component
//import java.util.*
//
///**
// * Created by shiqm on 2017-12-01.
// */
//
//@Intercepts(Signature(type = Executor::class, method = "update", args = arrayOf(MappedStatement::class, Any::class)))
//@Component
//class PostArrInterceptor : Interceptor {
//
//    override fun intercept(invocation: Invocation?): Any {
//        if (invocation!!.args[0] is MappedStatement) {
////            val ms = invocation.args[0] as MappedStatement
////            ms.configuration.isCacheEnabled = false
////            val sqlCommandType = ms.sqlCommandType
////            val parameter = invocation.args[1]
////            val clazz = parameter.javaClass
////            //更新时和保存时处理postgre数组格式
////            if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT) {
////                try {
////                    val fields = clazz.declaredFields
////                    fields.forEach {
////                        val hasAnnotation = it.isAnnotationPresent(PostArr::class.java)
////                        if (hasAnnotation) {
////                            it.isAccessible = true
////                            var value = it.get(parameter).toString()
////                            it.set(parameter,value.toPostArrString())
////                        }
////                    }
////                } catch (e: NoSuchFieldException) {
////                }
////            }
//        }
//        return invocation.proceed()
//    }
//
//    override fun setProperties(properties: Properties?) {}
//
//    override fun plugin(target: Any?): Any = Plugin.wrap(target, this)
//}