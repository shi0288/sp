package com.xyauto.qa.controller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.mapper.broker.BrokerMapper
import com.xyauto.qa.model.Distribute
import com.xyauto.qa.model.Materiel
import com.xyauto.qa.model.MaterielExt
import com.xyauto.qa.service.DistributeService
import com.xyauto.qa.service.MaterielService
import com.xyauto.qa.task.MaterielTask
import com.xyauto.qa.util.BaseController
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.PostgreCopyUtil
import com.xyauto.qa.util.annotation.EnjoyLog
import com.xyauto.qa.util.assist.getPager
import com.xyauto.qa.util.cons.BrokerCons
import org.apache.commons.lang.time.DateUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.*

/**
 * Created by shiqm on 2018-01-23.
 */

@RestController
@RequestMapping("materiel")
class MaterielController : BaseController() {

    @Autowired
    lateinit var materielService: MaterielService

    @Autowired
    lateinit var distributeService: DistributeService

    @Autowired
    lateinit var brokerMapper: BrokerMapper

    @Autowired
    lateinit var postgreCopyUtil: PostgreCopyUtil

    @Autowired
    lateinit var cache: Cache

    @Autowired
    lateinit var materielTask: MaterielTask


    @RequestMapping(value = "run", method = arrayOf(RequestMethod.GET))
    fun receive(
            @Check(name = "time´") time: String

    ): Any {
        when (time) {
            "9" -> {
                materielTask.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_9, "00:00:00", "09:00:00")
            }
            "12" -> {
                materielTask.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_12, "09:00:00", "12:00:00")
            }
            "15" -> {
                materielTask.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_15, "12:00:00", "15:00:00")
            }
            "17" -> {
                materielTask.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_17_30, "15:00:00", "17:30:00")
            }
            "20" -> {
                materielTask.handleTimerRabbitTask(BrokerCons.DistributeTaskFlag.TIME_20, "17:30:00", "20:00:00")
            }
        }
        return Result()
    }

//    @RequestMapping(value = "postGre", method = arrayOf(RequestMethod.GET))
//    fun postGre(): Any {
//        var i = 1
//        while (true) {
//            logger.info("start:" + i)
//            PageHelper.startPage<Map<String, Any>>(i, 1000000, false)
//            var list = brokerMapper.getReal()
//            logger.info("size:" + list.size)
//            if (list.size == 0) {
//                logger.info("over:" + i)
//                break
//            }
//            var filename = "/tmp/broker_sp_add$i.txt"
//            var writer = getFileWriter(filename)
//            list.forEach {
//                var serids: String? = null
//                if (it["serial_ids"] != null && !it["serial_ids"].toString().isEmpty()) {
//                    serids = "{${it["serial_ids"].toString()}}"
//                }
//                writer.write("${it["brokerId"] ?: "\\N"}\t${it["materielId"] ?: "\\N"}\t${it["CreateTime"] ?: "\\N"}\t${serids ?: "\\N"}\t${it["source"] ?: "\\N"}\n")
//            }
//            writer.close()
//            logger.info("end:" + i)
//            i++
//        }
//        return Result()
//    }
//
//
//    @RequestMapping(value = "postSet", method = arrayOf(RequestMethod.GET))
//    fun postSet(): Any {
//        (1..4).forEach {
//            val filename = "/tmp/broker_sp_add$it.txt"
//            val file= File(filename)
//            if(file.exists()){
//                postgreCopyUtil.copyFromFile(filename, "broker_materiel_distribute(broker_id,materiel_id,create_time,series_ids,source)")
//            }
//        }
//        return Result()
//    }


    /**
     * 保存物料信息
     */
    @RequestMapping(value = "receive", method = arrayOf(RequestMethod.POST))
    fun receive(
            @Check(name = "物料标题") name: String,
            @Check(name = "物料摘要", required = false, defaultValue = "") summary: String,
            @Check(name = "内容") content: String,
            @Check(name = "阅读数", required = false, defaultValue = "0") viewCount: Int,
            @Check(name = "点赞数", required = false, defaultValue = "0") agreeCount: Int,
            @Check(name = "封面图", required = false, defaultValue = "") image: String,
            @Check(name = "物料文章类型") contentType: Short,
            @Check(name = "物料来源") source: Short,
            @Check(name = "物料类型") type: Short,
            @Check(name = "数据id") originalId: String,
            @Check(name = "发布时间", required = false, defaultValue = "") publishTime: String,
            @Check(name = "来源名称", required = false, defaultValue = "") sourceName: String,
            @Check(name = "物料分类名称", required = false, defaultValue = "") categoryName: String,
            @Check(name = "物料标签", required = false, defaultValue = "") tag: String,
            @Check(name = "物料标识ID", required = false, defaultValue = "") taskId: String,
            @Check(name = "物料文章评分", required = false, defaultValue = "0") score: BigDecimal,
            @Check(name = "城市ID", required = false, defaultValue = "") cityIds: String,
            @Check(name = "省份ID", required = false, defaultValue = "") provinceIds: String,
            @Check(name = "城市名称", required = false, defaultValue = "") cityName: String,
            @Check(name = "是否关联车型", required = false, defaultValue = "0") isJoinCar: Short,
            @Check(name = "关联品牌", required = false, defaultValue = "") brandIds: String,
            @Check(name = "关联车型", required = false, defaultValue = "") serialIds: String,
            @Check(name = "关联经销商ID", required = false, defaultValue = "") dealerIds: String,
            @Check(name = "问答类型", required = false, defaultValue = "0") askType: Short,
            @Check(name = "经纪人ID", required = false, defaultValue = "0") askUid: Long,
            @Check(name = "问答ID", required = false, defaultValue = "0") askId: Long
    ): Any {
        val materielUpdate = materielService.materielMapper.selectOne(Materiel(originalId = originalId, source = source))
        if (materielUpdate != null) {
            val id = materielUpdate.id
            materielService.update(id!!, Materiel(name = name))
            materielService.updateExt(id, MaterielExt(content = content))
            return Result()
        }
        //组装物料信息
        val materiel = Materiel()
        materiel.name = name
        materiel.contentType = contentType
        materiel.source = source
        materiel.type = type
        materiel.originalId = originalId
        if (publishTime.isNotEmpty()) {
            try {
                materiel.publishTime = (DateUtils.parseDate(publishTime, arrayOf("yyyyMMddHHmmss")).time / 1000).toInt()
            } catch (e: Exception) {
                return Result(9999, "publishTime格式错误(yyyyMMddHHmmss)")
            }
        }
        materiel.taskId = taskId
        materiel.cityIds = cityIds
        materiel.isJoinCar = isJoinCar
        materiel.brandIds = brandIds
        materiel.serialIds = serialIds
        materiel.askType = askType
        materiel.askUid = askUid
        materiel.askId = askId
        materiel.provinceIds = provinceIds
        if (dealerIds.isNotEmpty()) {
            materiel.dealerIds = dealerIds.split(",").map { it.toLong() }.toLongArray()
        }
        //组装物料扩展
        val materielExt = MaterielExt()
        materielExt.content = content
        materielExt.summary = summary
        materielExt.viewCount = viewCount + Random().nextInt(50) + 50
        materielExt.agreeCount = agreeCount
        materielExt.image = image
        materielExt.sourceName = sourceName
        materielExt.categoryName = categoryName
        materielExt.tag = tag
        materielExt.score = score
        materielExt.cityName = cityName
        materielService.receive(materiel, materielExt)
        return Result()
    }


    /**
     * 修改物料信息
     */
    @RequestMapping(value = "update", method = arrayOf(RequestMethod.POST))
    fun update(
            @Check(name = "物料ID") id: Long,
            @Check(name = "物料标题", required = false) name: String?,
            @Check(name = "物料摘要", required = false) summary: String?,
            @Check(name = "内容", required = false) content: String?,
            @Check(name = "封面图", required = false) image: String?,
            @Check(name = "定投ID", required = false, defaultValue = "0") templateId: Long?,
            @Check(name = "城市ID", required = false, defaultValue = "") cityIds: String?,
            @Check(name = "省份ID", required = false, defaultValue = "") provinceIds: String?,
            @Check(name = "城市名称", required = false) cityName: String?,
            @Check(name = "关联品牌", required = false) brandIds: String?,
            @Check(name = "关联车型", required = false) serialIds: String?
    ): Any {

        if (!name.isNullOrEmpty() || templateId != 0L || !cityIds.isNullOrEmpty() || !brandIds.isNullOrEmpty() || !serialIds.isNullOrEmpty() || !provinceIds.isNullOrEmpty()) {
            materielService.update(id, Materiel(name = name, templateId = templateId, cityIds = cityIds, brandIds = brandIds, serialIds = serialIds, provinceIds = provinceIds))
        }
        if (!summary.isNullOrEmpty() || !content.isNullOrEmpty() || !image.isNullOrEmpty() || !cityName.isNullOrEmpty()) {
            materielService.updateExt(id, MaterielExt(summary = summary, content = content, image = image, cityName = cityName))
        }
        materielService.delBrokerMaterielCache(materiel_id = id)
        return Result()
    }


    /**
     * 修改物料信息
     */
    @RequestMapping(value = "updateBySource", method = arrayOf(RequestMethod.POST))
    fun updateBySource(
            @Check(name = "物料ID") originalId: String,
            @Check source: Short,
            @Check(name = "物料标题", required = false) name: String?,
            @Check(name = "物料摘要", required = false) summary: String?,
            @Check(name = "内容", required = false) content: String?,
            @Check(name = "封面图", required = false) image: String?,
            @Check(name = "定投ID", required = false, defaultValue = "0") templateId: Long?,
            @Check(name = "城市ID", required = false, defaultValue = "") cityIds: String?,
            @Check(name = "省份ID", required = false, defaultValue = "") provinceIds: String?,
            @Check(name = "城市名称", required = false) cityName: String?,
            @Check(name = "关联品牌", required = false) brandIds: String?,
            @Check(name = "关联车型", required = false) serialIds: String?
    ): Any {
        val materiel = materielService.materielMapper.selectOne(Materiel(originalId = originalId, source = source)) ?: return Result(9999, "内容不存在")
        val id = materiel.id
        if (id != null) {
            materielService.delBrokerMaterielCache(materiel_id = id)
        }
        return this.update(id!!, name, summary, content, image, templateId, cityIds, provinceIds, cityName, brandIds, serialIds)
    }


    /**
     * 获取推送给经纪人的物料列表（max）
     */
    @RequestMapping(value = "list", method = arrayOf(RequestMethod.GET))
    fun list(
            @Check broker_id: Long,
            @Check(defaultValue = "0") source: Long,
            @Check(defaultValue = "0") max: String,
            @Check(defaultValue = "20") limit: Int
    ): Any {
        val key = "qcdq:sp:$broker_id:$source:$max:$limit"
        var data = cache[key]
        if (data.isNullOrEmpty()) {
            data = JSON.toJSONString(Result(getPager(mapOf("broker_id" to broker_id, "source" to source), max, limit, distributeService::getMaterielIds, materielService::getMaterielList)))
            cache.set(key, data, 60)
        }
        return JSON.parseObject(data!!)
//        return Result(getPager(mapOf("broker_id" to broker_id, "source" to source), max, limit, distributeService::getMaterielIds, materielService::getMaterielList))
    }


    /**
     * 获取推送给经纪人的物料列表（page）
     */
    @RequestMapping(value = "plist", method = arrayOf(RequestMethod.GET))
    fun plist(
            @Check broker_id: Long,
            @Check(defaultValue = "0") source: Long,
            @Check(defaultValue = "1") page: Int,
            @Check(defaultValue = "20") limit: Int
    ): Any =
            Result(getPager(mapOf("broker_id" to broker_id, "source" to source), page, limit, distributeService::getMaterielIds, materielService::getMaterielList))


    /**
     * 获取物料详情
     */
    @EnjoyLog(value = "经纪人物料被浏览", target_id = "materiel_id")
    @RequestMapping(value = "get", method = arrayOf(RequestMethod.GET))
    fun get(
            @Check broker_id: Long,
            @Check materiel_id: Long
    ): Any {
        distributeService.get(Distribute(materielId = materiel_id, brokerId = broker_id, isDeleted = 0))?.apply {
            return Result(materielService.getMaterielById(materiel_id, broker_id))
        }
        return Result()
    }

    /**
     * 分配物料
     */
    @RequestMapping(value = "distribute", method = arrayOf(RequestMethod.GET))
    fun distribute(
            @Check broker_id: Set<Long>,
            @Check materiel_id: Long
    ): Any {
        materielService.handleBroker(brokerIds = *broker_id.toLongArray(), materielId = materiel_id)
        return Result()
    }


    /**
     * 分配物料给所有人
     */
    @RequestMapping(value = "distributeAll", method = arrayOf(RequestMethod.GET))
    fun distribute(
            @Check materiel_id: Long
    ): Any {
        materielService.handleAllBroker(materiel_id)
        return Result()
    }


    /**
     * 删除物料详情
     */
    @RequestMapping(value = "del", method = arrayOf(RequestMethod.GET))
    fun del(
            @Check broker_id: Long,
            @Check materiel_id: Long
    ): Any {
        if (distributeService.del(broker_id, materiel_id) > 0) {
            materielService.delBrokerMaterielCache(broker_id = broker_id)
            return Result()
        }
        return Result(9999, "删除失败")
    }


    /**
     * 删除物料详情--经销商
     */
    @RequestMapping(value = "delByDealer", method = arrayOf(RequestMethod.GET))
    fun delByDealer(
            @Check dealer_id: Long,
            @Check materiel_id: Long
    ): Any {
        val count = distributeService.delByDealer(dealer_id, materiel_id)
        if (count > 0) {
            distributeService.updateMaterielExtCount(materiel_id)
            materielService.delBrokerMaterielCache(dealer_id = dealer_id)
            return Result()
        }
        return Result(9999, "删除失败")
    }


    /**
     * 删除物料详情--经销商--评论删除，只行园慧使用
     */
    @RequestMapping(value = "delBySource")
    fun delBySource(
            @Check source: Short,
            @Check original_id: Set<String>
    ): Any {
        Thread({
            original_id.forEach {
                val materiel = materielService.materielMapper.selectOne(Materiel(originalId = it, source = 3))
                materiel?.apply {
                    dealerIds?.apply {
                        get(0)?.apply {
                            delByDealer(this, materiel.id!!)
                            materielService.delBrokerMaterielCache(dealer_id = this)
                        }
                    }
                }
            }
        }).start()
        return Result()
    }


    /**
     * 获取导航
     */
    @RequestMapping(value = "head", method = arrayOf(RequestMethod.GET))
    fun head(): Any {
        val header = ArrayList<JSONObject>()
        val all = JSONObject()
        all.put("title", "全部")
        all.put("source", 0)
        val all1 = JSONObject()
        all1.put("title", "汽车资讯")
        all1.put("source", 1)
        val all2 = JSONObject()
        all2.put("title", "店内资讯")
        all2.put("source", 2)
        val all3 = JSONObject()
        all3.put("title", "生活资讯")
        all3.put("source", 4)
        val all4 = JSONObject()
        all4.put("title", "汽车生活")
        all4.put("source", 3)
        val all5 = JSONObject()
        all5.put("title", "问答")
        all5.put("source", 5)
        header.add(all)
        header.add(all1)
        header.add(all2)
        header.add(all3)
        header.add(all4)
        header.add(all5)
        return Result(header)
    }

    /**
     * 获取导航
     */
    @RequestMapping(value = "report/distribute/number", method = arrayOf(RequestMethod.GET))
    fun distributeNumber(
            @Check materiel_ids: Set<String>
    ): Any {
        val datas = JSONArray()
        val data = JSONObject()
        data.put("distribute_dealer_number", materiel_ids.size)
        data.put("distribute_broker_number", materielService.materielMapper.getOriginDistributeCount(materiel_ids))
        datas.add(data)
//        materiel_ids.forEach {
//            val data = JSONObject()
//            data.put("materiel_id", it)
//            try {
//                val materiel = materielService.materielMapper.selectOne(Materiel(originalId = it.toString()))
//                val materielExt = materielService.materielExtMapper.selectOne(MaterielExt(materielId = materiel.id))
//                data.put("distribute_broker_number", materielExt.distributeCount)
////                data.put("distribute_dealer_number", distributeService.distributeMapper.getDistributeDealerCount(materiel.id!!))
//                data.put("distribute_dealer_number",1)
//            } catch (e: Exception) {
//                data.put("distribute_broker_number", 0)
//                data.put("distribute_dealer_number", 0)
//            }
//            datas.add(data)
//        }
        return Result(datas)
    }


}