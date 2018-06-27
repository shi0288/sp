package com.xyauto.qa.util.handle

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import org.postgresql.jdbc.PgArray
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Created by shiqm on 2018-02-28.
 */

@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(IntArray::class)
class PgArrHandler : BaseTypeHandler<IntArray>() {

    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): IntArray? {
        if (cs.getArray(columnIndex) != null) {
            val array = cs.getArray(columnIndex) as PgArray
            var str = array.toString()
            if (str.isEmpty()) {
                return IntArray(0)
            }
            str = str.replace("{", "").replace("}", "")
            return str.split(",").map { it.replace("\"","").toInt() }.toIntArray()
        }
        return null

    }


    override fun getNullableResult(rs: ResultSet, columnName: String?): IntArray? {
        if (rs.getArray(columnName) != null) {
            val array = rs.getArray(columnName) as PgArray
            var str = array.toString()
            if (str.isEmpty()) {
                return IntArray(0)
            }
            str = str.replace("{", "").replace("}", "")
            return str.split(",").map {
                   it.replace("\"","").toInt()
            }.toIntArray()
        }
        return null
    }

    override fun getNullableResult(rs: ResultSet, columnIndex: Int): IntArray? {
        if (rs.getArray(columnIndex) != null) {
            val array = rs.getArray(columnIndex) as PgArray
            var str = array.toString()
            if (str.isEmpty()) {
                return IntArray(0)
            }
            str = str.replace("{", "").replace("}", "")
            return str.split(",").map { it.replace("\"","").toInt() }.toIntArray()
        }
        return null

    }

    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: IntArray?, jdbcType: JdbcType?) {
        if (parameter != null) {
            val conn = ps.connection
            val array = conn.createArrayOf("integer", parameter.toTypedArray())
            ps.setArray(i, array)
        }
    }
}