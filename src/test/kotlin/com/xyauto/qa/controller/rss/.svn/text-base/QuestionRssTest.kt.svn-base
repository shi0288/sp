//package com.xyauto.qa.controller.rss
//
//import com.alibaba.fastjson.JSONArray
//import com.alibaba.fastjson.JSONObject
//import com.github.pagehelper.PageHelper
//import com.xyauto.qa.cloud.CarCloud
//import com.xyauto.qa.cloud.QaCloud
//import com.xyauto.qa.mapper.log.DistributeMapper
//import com.xyauto.qa.mapper.broker.MaterielMapper
//import com.xyauto.qa.mapper.qa.*
//import com.xyauto.qa.model.Broker
//import com.xyauto.qa.service.BrokerService
//import com.xyauto.qa.service.DistributeService
//import com.xyauto.qa.service.MaterielService
//import com.xyauto.qa.service.UserDistributeLogService
//import com.xyauto.qa.task.MaterielTask
////import com.xyauto.qa.task.MaterielTask
//import com.xyauto.qa.util.Cache
//import com.xyauto.qa.util.PostgreCopyUtil
//import com.xyauto.qa.util.cons.PinganCons
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.postgresql.copy.CopyManager
//import org.postgresql.core.BaseConnection
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
//import java.io.FileInputStream
//import java.util.*
//import javax.sql.DataSource
//
//
///**
// * Created by shiqm on 2017-09-14.
// */
//
//
//@RunWith(SpringJUnit4ClassRunner::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class QuestionRssTest {
//
//    @Autowired
//    lateinit var questionMapper: QuestionMapper
//
//    @Autowired
//    lateinit var qaCloud: QaCloud
//
//    @Autowired
//    lateinit var answerMapper: AnswerMapper
//
//    @Autowired
//    lateinit var cache: Cache
//
//    @Autowired
//    lateinit var pinganCons: PinganCons
//
//    @Autowired
//    lateinit var attachMapper: AttachMapper
//
//
//    @Autowired
//    lateinit var spiderQuestionMapper: SpiderQuestionMapper
//
//    @Autowired
//    lateinit var userDistributeLogMapper: UserDistributeLogMapper
//
//
//    @Autowired
//    lateinit var userDistributeLogService: UserDistributeLogService
//
//    @Autowired
//    lateinit var carCloud: CarCloud
//
//    @Autowired
//    lateinit var distributeMapper: DistributeMapper
//
//    @Autowired
//    lateinit var materielService: MaterielService
//
//    @Autowired
//    lateinit var materielMapper: MaterielMapper
//
//    @Autowired
//    lateinit var distributeService: DistributeService
//
//    @Autowired
//    lateinit var materielTask: MaterielTask
//
//    @Autowired
//    lateinit var logDataSource: DataSource
//
//    @Autowired
//    lateinit var postgreCopyUtil: PostgreCopyUtil
//
//    @Autowired
//    lateinit var brokerService: BrokerService
//
//
//    @Test
//    fun test() {
//
//        (1..10).forEach {
//            postgreCopyUtil.copyFromFile("/data/aaa.txt", "broker_materiel_distribute")
//            println("ok")
//        }
//
//
////        val brokers = materielMapper.getAllBrokerIds()
////
////        println(brokers.size)
////
////        brokers.keys.forEach{ println(it)}
////
////        println(brokers.containsKey(14))
////        println(brokers.containsKey(127512))
////        println(brokers.containsKey(17))
////        println(brokers.remove(127512))
////        println(brokers.containsKey(17))
////        println(brokers.size)
//
////        cache.removePattern("qcda:sss:bb:*")
//
//
////        val count = materielMapper.getAllUidCount()
////        val limit = 300
////        if (count > 0) {
////            val pageNum = count / limit + if (count % limit == 0) 0 else 1
////            for (i in 1..pageNum) {
////                //当前处理的页码
////                PageHelper.startPage<Any>(i, limit, false)
////        val date = Date()
////        val collectionName = brokerService.getDistributeBrokerQueue(2, 1)
////        brokerService.saveAllToMongo(collectionName)
////        println(Date().time - date.time)
//
////        println(brokerDao.count(Broker(),"distribute_20180413_2_1"))
////        println(brokerDao.count(Broker(),"distribute_20180413_2_1"))
////        println(brokerDao.count(Broker(),"distribute_20180413_2_1"))
////        println(brokerDao.count(Broker(),"distribute_20180413_2_1"))
////        println(brokerDao.count(Broker()))
////        println(brokerDao.count(Broker()))
////        println(brokerDao.count(Broker()))
//
//
//
//
////            }
////        }
//
//
////        materielTask.handleActualNewsTask()
//
//
////        materielService.handleActualQa(Date(1521788123000), Date())
//
////
//////        val jsonObject = JSONObject()
//////        jsonObject.put("point",6)
////
////        val jsonObject = JSONObject()
////        val jsonArray = JSONArray()
////        val json1 = JSONObject()
////        json1.put("point", 545)
////        json1.put("sign", "one")
////        val json2 = JSONObject()
////        json2.put("point", 545)
////        json2.put("sign", "two")
////        jsonArray.add(json1)
////        jsonArray.add(json2)
////        jsonObject.put("appoint", jsonArray)
////
////        println(taskCloud.createAnswerNoPic(mapOf<String, Any>(
////                "plugin" to "disposablePlugin",
////                "apply_condition" to jsonObject,
////                "is_target" to 1,
////                "apply" to "sdfsdf444",
////                "finishPercent" to 23,
////                "name" to "斯蒂芬第四",
////                "description" to "斯蒂芬第四"
////        )))
//
//
////
////        PageHelper.startPage<Any>(1, 20, false)
////        PageHelper.orderBy("create_time desc")
//////        PageHelper.offsetPage<Any>(1,10)
////        distributeMapper.getDistributeIds(1, 22).forEach(::println)
//
//
////        var fileOutputStream  = FileOutputStream ("/data/hehe.txt")
////        fileOutputStream .use {
////            copyManager.copyOut("COPY broker_materiel_distribute TO STDOUT", fileOutputStream)
////        }
//
////        postgreCopyUtil.copyToFile("/data/aaa.txt", "broker_materiel_distribute")
//
//
////        (1..100000).forEach {
////            var date=Date()
////            var distribute = Distribute()
////            distribute.brokerId=it.toLong()
////            distributeMapper.insertSelective(distribute)
////            println(Date().time-date.time)
////        }
//
////        (1..100000).forEach {
////            var date=Date()
////            var materiel = Materiel()
////            materiel.source = 32
////            materiel.askId=it.toLong()
////            materielMapper.insertSelective(materiel)
////            println(Date().time-date.time)
////        }
//
//
////
////        println(distributeMapper.getUserCount(distribute, BrokerCons.DistributeTaskFlag.TIME_9, Date(), Date()))
////
//
////        println(distributeMapper.getUserCountAAA)
//
//
////        println(distributeMapper.selectAll())
//
////        (1..5).forEach {
////            println(it)
////            distributeService.addDistribute(4,1,2,2,3,4,5)
////        }
//
////        (0..10000).forEach {
////            var distribute = Distribute()
////            distribute.materielId = (Random().nextInt(50000) + 1).toLong()
////            distribute.brokerId = (Random().nextInt(50000) + 1).toLong()
////            distribute.status = 1
////            var tableStr = distribute.brokerId.toString()
////            tableStr = tableStr.substring(tableStr.length - 1)
////            distribute.tableName = distribute.tableName + tableStr
////            distributeMapper.insertSelective(distribute)
////        }
//
//
//        //brandService.syncBrand()
//
////        println(carCloud(8))
//
////        println(carCloud.getCar(3859))
////        val distribute = Distribute()
////        distribute.materielId = 222
////        distribute.status = 1
////        distribute.uid = 2
////        distributeMapper.insertSelective(dis tribute)
////        println(distribute)
////
////        val materiel = Materiel()
//////        materiel.id = 1
////        materiel.askId = 322323
//////        materiel.updateTime=DateUtils.parseDate("2017-1-1 23:09:32", arrayOf("yyyy-MM-dd HH:mm:ss"))
//////        materielMapper.updateByPrimaryKeySelective(materiel)
////        materiel.contentType = 1
////        materiel.source = 1
////        materiel.type = 1
////        materiel.originalId = "222"
////        materiel.publishTime = (DateUtils.parseDate("2017-1-1 23:09:32", arrayOf("yyyy-MM-dd HH:mm:ss")).time / 1000).toInt()
////        materiel.taskId = "232"
////        materiel.cityId = 222
////        materiel.isJoinCar = 3
////        materiel.brandIds = "32"
////        materiel.serialIds = "343"
////        materiel.askType = 1
////        materiel.askUid = 343
////        materiel.askId = 343
////        //组装物料扩展
////        val materielExt = MaterielExt()
////        materielExt.content = "sdfsdfd"
////        materielExt.viewCount = 333
////        materielExt.agreeCount = 33
////        materielExt.image = "sdfsdf"
////        materielExt.sourceName = "sdfdsf"
////        materielExt.categoryName = "sdfsdf"
////        materielExt.tag = "2323"
////        materielExt.score = BigDecimal(12)
////        materielExt.cityName = "333"
////        materielService.receive(materiel,materielExt)
//
//
////        println(attachMapper.selectOne(Attach(1)))
////        println(questionMapper.getLastestThirdId(103))
////        println(qaCloud.getUserDesc(1))
////
////        println(answerMapper.getListByCreatedAt(105,0,0))
////
////        PageHelper.startPage<Answer>(0,5)
////        OrderByHelper.orderBy("answer.created_at asc")
////        System.out.println(answerMapper.getListByCreatedAtCount(103,0,0))
//
//
////        val key:String="qa:sync:pingan:answer"
////
////        val kea=cache[key]
////        kea.let {
////            println(it)
////        }
//
////        println(pinganCons.url)
////        val res = HttpClientWrapper.post(
////                pinganCons.url,
////                JSONObject().apply {
////                    put("questionID", "b4WVk0lPWmMO3S8iYILwEap7KZvLmM")
////                    put("images", null)
////                    put("mainText", "123456")
////                    put("userInfo", JSONObject().apply {
////                        put("userID", "5")
////                        put("nickname", "123")
////                        put("avatar", "http://static.qcdqcdn.com/img/profile3.jpg")
////                    })
////                    put("head", JSONObject().apply {
////                        put("outerid", "123")
////                        put("messageid", UUID.randomUUID().toString().replace("-", ""))
////                    })
////                }.toString(), block = { request ->
////            request.addHeader("HCZContent-Key", pinganCons.key)
////            val currentTime = System.currentTimeMillis() / 1000
////            request.addHeader("HCZContent-Sign", "${MD5.encode(pinganCons.key + currentTime + pinganCons.secret)}$currentTime")
////            request.addHeader("cache-control", "no-cache")
////            request.addHeader("content-type", "application/json")
////            println(Arrays.toString(request.allHeaders))
////            request
////        })
////        println(res)
//
////        spiderQuestionMapper.insertSelective(SpiderQuestion(
////                recId = 192,
////                source = 1,
////                content = "{\"code\":10000,\"data\":{\"问答Json\":[{\"answer\":\"现在天气冷了，北京早晚差不多也就0度左右，目前来看在室外快充桩充电时电流最高也就能到70a??电池温度比较低，完全没体现出来电池预加热功能啊！\",\"brand\":\"长安\",\"car_Type\":\"奔奔EV\",\"createTime\":1511861238430,\"issue\":\"关于冬天充电的问题\",\"publisTime\":1510544820000,\"recID\":1,\"resource\":1,\"type1\":\"其他\",\"type2\":\"娱乐\",\"url\":\"https://club.autohome.com.cn/bbs/threadqa-c-4380-68424175-1.html\"}]},\"msg\":\"服务调用成功\",\"sub_code\":1}"
////        ))
//
//
////        println(attachMapper.select(Attach(file_type = 0,target_id = 49252)))
////
////        var list = listOf<Long>(
////                88434,23
////        )
////        println(userDistributeLogMapper.getDistributeCountByUid(list,1505981723,1505983608))
//
//
////        println(userDistributeLogService.getUserDistributeLostByUids(list,1505981723,1505983608))
//
////        println(spiderQuestionMapper.getMaxRecId()?:0)
//
////        cache.remove("qa:sync:sp:spider:recId")
//
//
////        val resObj = JSON.parseObject("{\"errno\":0,\"errmsg\":\"\\u64cd\\u4f5c\\u6210\\u529f\\uff01---\\u60a8\\u63d0\\u4ea4\\u7684\\u53c2\\u6570\\u4e3a:{\\\"questionid\\\":\\\"1180048626704927299\\\",\\\"pic_urls\\\":\\\"\\\",\\\"content\\\":\\\"\\\\u4f60\\\\u7684\\\\u8f66\\\\u8f86\\\\u4fdd\\\\u517b\\\\u624b\\\\u518c\\\\u6709\\\\u5177\\\\u4f53\\\\u65f6\\\\u95f4\\\\uff0c\\\\u6b63\\\\u5e38\\\\u5c31\\\\u662f6\\\\u4e2a\\\\u6708\\\",\\\"appkey\\\":\\\"2018\\\",\\\"sign\\\":\\\"1edff7d8932659c13c641aad98d6d57c\\\"}\",\"data\":{\"commit_errno\":0,\"questionid\":\"1180048626704927299\",\"replyid\":2867726279}}")
////        val replyid = resObj.getJSONObject("data").getString("replyid")
////        answerMapper.updateThirdId(replyid,6)
//
//
////
//
//    }
//}
//
//
