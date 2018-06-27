package com.xyauto.qa.util

import org.postgresql.copy.CopyManager
import org.postgresql.core.BaseConnection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.FileOutputStream
import org.apache.tomcat.jdbc.pool.DataSource

/**
 * Created by shiqm on 2018-02-07.
 */

@Component
class PostgreCopyUtil {

    @Autowired
    lateinit var logDataSource: DataSource


    fun copyFromFile(fileName: String, tableName: String) {
        val pool = logDataSource.getPool()
        val connection = pool.getConnection()
        try {
            val copyManager = CopyManager(connection.metaData.connection as BaseConnection)
            var fileInputStream = FileInputStream(fileName)
            fileInputStream.use {
                copyManager.copyIn("COPY $tableName FROM STDIN", fileInputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.close()
        }

    }

    fun copyToFile(fileName: String, tableName: String) {
        val pool = logDataSource.getPool()
        val connection = pool.getConnection()
        try {
            val copyManager = CopyManager(connection.metaData.connection as BaseConnection)
            var fileOutputStream = FileOutputStream(fileName)
            fileOutputStream.use {
                copyManager.copyOut("COPY $tableName TO STDOUT", fileOutputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.close()
        }
    }


}