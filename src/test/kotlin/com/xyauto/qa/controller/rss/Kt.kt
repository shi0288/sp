//package com.xyauto.qa.controller.rss
//
//import com.xyauto.qa.model.Distribute
//import com.xyauto.qa.service.DistributeService
//import org.junit.Test
//import org.postgresql.copy.CopyManager
//import org.postgresql.core.BaseConnection
//import java.io.File
//import java.io.FileInputStream
//import java.io.OutputStream
//import java.io.Writer
//import java.sql.DriverManager
//import java.util.*
//import java.io.FileOutputStream
//import java.io.BufferedWriter
//import java.io.OutputStreamWriter
//import java.util.concurrent.Executors
//import java.util.concurrent.ThreadPoolExecutor
//
//
///**
// * Created by shiqm on 2018-01-26.
// */
//
//
//class Kt {
//
//
//    fun aaa(aa: Int? = 0) {
//        println(aa)
//    }
//
//    @Test
//    fun test() {
//
//        val distribute = Distribute(dealerName = "111", dealerId = 222)
//        println(distribute.toCopyText())
//
////        val distribute=Distribute()
////        distribute.brandIds="11,22,3,3,4"
////        println(distribute.toCopyText())
//
//
////        val url = "jdbc:postgresql://192.168.3.20:5432/ts_qa"
////        val user = "postgres"
////        val password = "fBx!h3Z^MTxHOEQ!"
////        Class.forName("org.postgresql.Driver")
////        var connection = DriverManager.getConnection(url, user, password)
//////
//////
////        val copyManager = CopyManager(connection as BaseConnection)
//////
//////
////        var fileInputStream = FileInputStream("/data/broker_sp_1_2_1_1.txt")
////        fileInputStream.use {
////            copyManager.copyIn("COPY broker_materiel_distribute(broker_id,materiel_id,status,type,task_tag,broker_name,materiel_name,dealer_id,brand_ids,dealer_name,create_time,update_time) FROM STDIN", fileInputStream)
//////            copyManager.copyIn("COPY broker_materiel_distribute FROM STDIN", fileInputStream)
////        }
//
////        var fileOutputStream  = FileOutputStream ("/data/sout.txt")
////        fileOutputStream .use {
//////            copyManager.copyOut("COPY broker_materiel_distribute TO STDOUT  (FORMAT 'CSV',HEADER true)", fileOutputStream)
////            copyManager.copyOut("COPY broker_materiel_distribute TO STDOUT", fileOutputStream)
////        }
//
//
////        (1..100).forEach {
////            val copyManager = CopyManager(connection as BaseConnection)
////            var executeThreadPoolExecutor: ThreadPoolExecutor = Executors.newFixedThreadPool(50) as ThreadPoolExecutor
////            var date = Date()
////            val file = File("/data", "hehe.txt")
////            val os = FileOutputStream(file)
////            val writer = OutputStreamWriter(os)
////            val bw = BufferedWriter(writer)
////            ((it-1)*100+1..it*100).forEach {
////                executeThreadPoolExecutor.execute {
////                    ((it-1)*100+1..it*100).forEach {
////                        val dis = Distribute()
////                        dis.brokerId = it.toLong()
////                        dis.materielId = 34234324
////                        dis.type = 0
////                        dis.status = 1
////                        dis.taskTag = 1
////                        dis.createTime = Date()
////                        bw.write(dis.toCopyText())
////                    }
////                }
////            }
////            while (true) {
////                Thread.sleep(200)
////                if (executeThreadPoolExecutor.activeCount == 0) {
////                    bw.close()
////                    date = Date()
////                    var fileInputStream = FileInputStream("/data/hehe.txt")
////                    fileInputStream.use {
////                        copyManager.copyIn("COPY broker_materiel_distribute FROM STDIN", fileInputStream)
////                    }
////                    println(Date().time - date.time)
////                    break
////                }
////            }
////        }
//
//
////
//
//
//    }
//
//
//}
//
