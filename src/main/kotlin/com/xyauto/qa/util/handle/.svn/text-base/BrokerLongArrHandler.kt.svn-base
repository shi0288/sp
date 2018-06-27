package com.xyauto.qa.util.handle

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by shiqm on 2018-03-01.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(LongArray::class)
class BrokerLongArrHandler : BaseTypeHandler<LongArray>() {

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): LongArray {
        val str = rs.getString(columnIndex)
        if(str.isEmpty()){
            return LongArray(0)
        }
        return str.split(",").map {
            it.replace("\"","").toLong()
        }.toLongArray()
    }

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): LongArray {
        val str = cs.getString(columnIndex)
        if(str.isEmpty()){
            return LongArray(0)
        }
        return str.split(",").map { it.replace("\"","").toLong() }.toLongArray()
    }

    override fun getNullableResult(rs: ResultSet, columnName: String): LongArray {
        val str = rs.getString(columnName)
        if(str.isEmpty()){
            return LongArray(0)
        }
        return str.split(",").map { it.replace("\"","").toLong() }.toLongArray()
    }

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: LongArray?, jdbcType: JdbcType?) {
        ps.setString(i,parameter!!.joinToString(prefix = "",postfix = "",separator = ","))
    }
}