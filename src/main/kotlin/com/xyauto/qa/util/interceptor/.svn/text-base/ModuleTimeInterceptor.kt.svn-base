package com.xyauto.qa.util.interceptor

import org.apache.ibatis.executor.Executor
import org.apache.ibatis.mapping.MappedStatement
import org.apache.ibatis.mapping.SqlCommandType
import org.apache.ibatis.plugin.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by shiqm on 2017-12-01.
 */

@Intercepts(Signature(type = Executor::class, method = "update", args = arrayOf(MappedStatement::class, Any::class)))
@Component
class ModuleTimeInterceptor : Interceptor {

    override fun intercept(invocation: Invocation?): Any {
        if (invocation!!.args[0] is MappedStatement) {
            val ms = invocation.args[0] as MappedStatement
            ms.configuration.isCacheEnabled = false
            val sqlCommandType = ms.sqlCommandType
            val parameter = invocation.args[1]
            val clazz = parameter.javaClass
            val curTime = Integer.parseInt((System.currentTimeMillis() / 1000).toString())
            val date=Date()
            //保存时赋值创建时间
            if (sqlCommandType == SqlCommandType.INSERT) {
                try {
                    var createdAtField = clazz.getDeclaredField("createdAt")
                    createdAtField.isAccessible = true
                    if (createdAtField.get(parameter) == null) {
                        createdAtField.set(parameter, curTime)
                    }
                } catch (e: NoSuchFieldException) {
                }
                try {
                    var createdAtField = clazz.getDeclaredField("createTime")
                    createdAtField.isAccessible = true
                    if (createdAtField.get(parameter) == null) {
                        createdAtField.set(parameter, date)
                    }
                } catch (e: NoSuchFieldException) {
                }
            }
            //更新时和保存时都赋值updateAt
            if (sqlCommandType == SqlCommandType.UPDATE || sqlCommandType == SqlCommandType.INSERT) {
                try {
                    val updateAtField = clazz.getDeclaredField("updateAt")
                    updateAtField.isAccessible = true
                    updateAtField.set(parameter, curTime)
                } catch (e: NoSuchFieldException) {
                }
                try {
                    val updateAtField = clazz.getDeclaredField("updateTime")
                    updateAtField.isAccessible = true
                    updateAtField.set(parameter, date)
                } catch (e: NoSuchFieldException) {
                }
            }
        }
        return invocation.proceed()
    }

    override fun setProperties(properties: Properties?) {}

    override fun plugin(target: Any?): Any = Plugin.wrap(target, this)
}