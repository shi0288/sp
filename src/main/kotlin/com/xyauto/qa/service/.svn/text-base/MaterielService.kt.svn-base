package com.xyauto.qa.service

import com.alibaba.fastjson.JSONArray
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.mcp.fastcloud.annotation.Log
import com.mcp.fastcloud.util.exception.base.ErrorException
import com.xyauto.qa.mapper.broker.BrokerMapper
import com.xyauto.qa.mapper.broker.MaterielExtMapper
import com.xyauto.qa.mapper.broker.MaterielMapper
import com.xyauto.qa.model.Answer
import com.xyauto.qa.model.Distribute
import com.xyauto.qa.model.Materiel
import com.xyauto.qa.model.MaterielExt
import com.xyauto.qa.util.Cache
import com.xyauto.qa.util.Pager
import com.xyauto.qa.util.cons.BrokerCons
import com.xyauto.qa.util.enum.SpcpCode
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tk.mybatis.mapper.entity.Example
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import javax.annotation.PostConstruct

/**
 * Created by shiqm on 2018-01-23.
 */

@Service
class MaterielService {

    @Autowired
    lateinit var materielMapper: MaterielMapper

    @Autowired
    lateinit var materielExtMapper: MaterielExtMapper

    @Autowired
    lateinit var distributeService: DistributeService

    @Autowired
    lateinit var brokerMapper: BrokerMapper

    @Autowired
    lateinit var brokerService: BrokerService

    @Autowired
    lateinit var cache: Cache

    @Log
    lateinit var logger: Logger

    lateinit private var rabbitThreadPoolExecutor: ThreadPoolExecutor
    lateinit private var masterThreadPoolExecutor: ThreadPoolExecutor


    @PostConstruct
    private fun init() {
        rabbitThreadPoolExecutor = Executors.newFixedThreadPool(20) as ThreadPoolExecutor
        masterThreadPoolExecutor = Executors.newFixedThreadPool(20) as ThreadPoolExecutor
    }


    /**
     * 保存物料数据
     */
    @Transactional("brokerTransactionManager")
    fun receive(materiel: Materiel, materielExt: MaterielExt) {
        materielMapper.insertSelective(materiel)
        materielExt.materielId = materiel.id
        materielExtMapper.insertSelective(materielExt)
    }


    /**
     * 封装分发数据
     */
    fun buildDistribute(materiel: Materiel, status: Short, type: Short, taskTag: Short = 0): Distribute {
        val distribute = Distribute()
        distribute.materielId = materiel.id
        distribute.materielName = materiel.name
        distribute.status = status
        distribute.type = type
        distribute.taskTag = taskTag
        distribute.source = materiel.source
        distribute.contentType = materiel.contentType
        if (!materiel.brandIds.isNullOrEmpty() && !materiel.brandIds.equals("0")) {
            try {
                distribute.brandIds = materiel.brandIds!!.split(",").map { it.toInt() }.toIntArray()
            } catch (e: Exception) {
            }
        }
        if (!materiel.serialIds.isNullOrEmpty() && !materiel.serialIds.equals("0")) {
            try {
                distribute.seriesIds = materiel.serialIds!!.split(",").map { it.toInt() }.toIntArray()
            } catch (e: Exception) {
            }
        }
        return distribute
    }


    /**
     * 定时任务逻辑近似，统一封装
     */
    fun handleTimer(startTime: Date, endTime: Date, status: Short, type: Short, taskTag: Short, block: (Date, Date) -> Page<Materiel>, toOuter: (Distribute, Materiel, Int) -> Unit) {
        //获取分发物料
        val page = block(startTime, endTime)
        var time = 1
        when (taskTag) {
            BrokerCons.DistributeTaskFlag.TIME_12 -> {
                time = 2
            }
            BrokerCons.DistributeTaskFlag.TIME_17_30 -> {
                time = 4
            }
        }
        var filerTimes = page.size / time + if (page.size % time == 0) 0 else 1
        var jump = false
        (1..time).forEach { curTime ->
            if (jump) {
                return@forEach
            }
            var startIndex = (curTime - 1) * filerTimes
            var endIndex = curTime * filerTimes

            if (endIndex > page.size) {
                filerTimes = (page.size - startIndex)
                endIndex = page.size
                jump = true
            }
            logger.info("save分组 $filerTimes")
            logger.info("index $startIndex  $endIndex")
            if (endIndex > page.size) {
                return@forEach
            }
            val collectionName = brokerService.getDistributeBrokerQueue(type, taskTag)
            brokerService.saveAllToMongo(collectionName)
            logger.error("save队列 $collectionName")
            val size = brokerService.getBrokerSize(collectionName, filerTimes)
            logger.info("每个物料分发size $size")
            run {
                page.subList(startIndex, endIndex).forEach {
                    //如果经纪人队列已经为空，证明已经所需物料数已经满足，直接返回，不再处理后续的物料了
                    if (!brokerService.collectionExists(collectionName)) {
                        logger.info("$collectionName 已经处理完毕")
                        return@run
                    }
                    logger.info("物料running ${it.id}")
                    val distribute = buildDistribute(it, status, type, taskTag)
                    toOuter(distribute, it, size)
                    logger.info("剩余  ${brokerService.brokerCount(collectionName)}")
                }
            }
            //删除队列
            logger.info("rabbit 删除队列 $collectionName")
            brokerService.dropCollection(collectionName)
        }
    }

    /**
     * Currying
     */
    fun handleTimer(startTime: Date, endTime: Date, taskTag: Short)
            = fun(status: Short, type: Short)
            = fun(block: (Date, Date) -> Page<Materiel>, toOuter: (Distribute, Materiel, Int) -> Unit)
                    = handleTimer(startTime, endTime, status, type, taskTag, block, toOuter)


    /**
     * 赤兔---定时
     */
    fun handleTimerRabbit(startTime: Date, endTime: Date, taskTag: Short) {
        logger.error("rabbit start")
        handleTimer(startTime, endTime, taskTag)(BrokerCons.DistributeStatusFlag.TIMER, BrokerCons.DistributeTypeFlag.RED_RABBIT)(
                materielMapper::getTimerRabbitMateriel,
                { distribute, materiel, materielSize ->
                    //有地区优先  全国通发
                    distributeService.uponUserByParam(rabbitThreadPoolExecutor, distribute = distribute, cityIds = materiel.cityIds, size = materielSize)
                }
        )
        logger.error("rabbit end")
    }

    /**
     * 大全---文章物料&问答精品--定时
     */
    fun handleTimerMaster(startTime: Date, endTime: Date, taskTag: Short) {
        handleTimer(startTime, endTime, taskTag)(BrokerCons.DistributeStatusFlag.TIMER, BrokerCons.DistributeTypeFlag.QCDQ)(
                { startTime, endTime ->
                    val pageDq = materielMapper.getTimerMasterMateriel(startTime, endTime)
                    val pageQa = materielMapper.getTimerQaMateriel(startTime, endTime)
                    pageDq.addAll(pageQa)
                    pageDq
                },
                { distribute, materiel, materielSize ->
                    //有车型的按照车型发放 有地区的按照地区发放 全国通发
                    distributeService.uponUserByParam(masterThreadPoolExecutor, distribute, serialIds = materiel.serialIds, cityIds = materiel.cityIds, size = materielSize)
                }
        )
    }


    /**
     * 大全---问答---实时
     */
    fun handleActualQa(startTime: Date, endTime: Date) {
        val page = materielMapper.getActualQaMateriel(startTime, endTime)
        page.forEach {
            val distribute = this.buildDistribute(it, BrokerCons.DistributeStatusFlag.ACTUAL, BrokerCons.DistributeTypeFlag.QCDQ)
            //匹配车顾问分发
            distributeService.addDistribute(distribute, brokers = *brokerMapper.getBrokerByIds(listOf(it.askUid!!.toLong())).toTypedArray())
        }
    }

    /**
     * 行圆慧--实时
     */
    fun handleActualNews(startTime: Date, endTime: Date) {
        val page = materielMapper.getActualNewsMateriel(startTime, endTime)
        page.forEach {
            val distribute = this.buildDistribute(it, BrokerCons.DistributeStatusFlag.ACTUAL, BrokerCons.DistributeTypeFlag.XY)
            it.dealerIds?.forEach {
                //匹配经销商
                distributeService.addDistribute(distribute, brokers = *brokerMapper.getBrokerByDealerId(it).toTypedArray())
            }
        }
    }


    /**
     * 模板--实时
     */
    fun handleActualTemplate(startTime: Date, endTime: Date) {
        val list = materielMapper.getMaterielTemplateIdByTime(startTime, endTime)
        list.forEach { materiel ->
            val items = materielMapper.getDealerIdsByTemplateId(materiel.templateId!!)
            items.forEach { dealerId ->
                val distribute = this.buildDistribute(materiel, BrokerCons.DistributeStatusFlag.ACTUAL, BrokerCons.DistributeTypeFlag.DT)
                //匹配经销商
                distributeService.addDistribute(distribute, brokers = *brokerMapper.getBrokerByDealerId(dealerId).toTypedArray())
            }
        }
    }


    /**
     * 直接分配
     */
    fun handleBroker(vararg brokerIds: Long, materielId: Long) {
        try {
            val materiel = materielMapper.selectByPrimaryKey(materielId) ?: throw ErrorException(SpcpCode.Materiel("物料不存在"))
            val distribute = this.buildDistribute(materiel, BrokerCons.DistributeStatusFlag.ACTUAL, BrokerCons.DistributeTypeFlag.DT)
            val list = brokerMapper.getBrokerByIds(brokerIds.asList())
            distributeService.addDistribute(distribute, brokers = *list.toTypedArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 直接分配全人
     */
    fun handleAllBroker(materielId: Long) {
        try {
            val materiel = materielMapper.selectByPrimaryKey(materielId) ?: throw ErrorException(SpcpCode.Materiel("物料不存在"))
            val distribute = this.buildDistribute(materiel, BrokerCons.DistributeStatusFlag.ACTUAL, BrokerCons.DistributeTypeFlag.DT)
            distributeService.uponUserByParam(masterThreadPoolExecutor, distribute)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 更新
     */
    fun update(id: Long, materiel: Materiel) {
        materiel.id = id
        materielMapper.updateByPrimaryKeySelective(materiel)
    }

    /**
     * 更新扩展
     */
    fun updateExt(id: Long, materielExt: MaterielExt) {
        val example = Example(MaterielExt::class.java)
        example.createCriteria().andEqualTo("materielId", id)
        materielExtMapper.updateByExampleSelective(materielExt, example)
    }


    /**
     * 获取物料列表信息
     */
    fun getMaterielList(pagerFor: Pager<Long>): Pager<Long> {
        if (pagerFor.takeIds() != null && !pagerFor.takeIds()!!.isEmpty()) {
            val list = materielMapper.getMaterielByIds(pagerFor.takeIds()!!)
            val param = pagerFor.params()
            val brokerSeriesList = brokerMapper.getSeriesByBrokerId(param!!["broker_id"].toString().toLong())
            list.forEach {
                it.put("source_title", "")
                when (param["source"].toString().toLong()) {
                    0L -> {
                        if (it["source"].toString().toInt() == 3) {
                            it.put("source_title", "店内资讯")
                        } else if (!it["serial_ids"].toString().isEmpty() && it["serial_ids"].toString() != "0") {
                            it.put("source_title", "汽车资讯")
                        } else if (it["source"].toString().toInt() == 1 && (it["serial_ids"].toString().isEmpty() || it["serial_ids"].toString() == "0")) {
                            it.put("source_title", "汽车生活")
                        } else if (it["source"].toString().toInt() == 2 && (it["serial_ids"].toString().isEmpty() || it["serial_ids"].toString() == "0")) {
                            it.put("source_title", "生活资讯")
                        } else if (it["source"].toString().toInt() == 6 && (it["serial_ids"].toString().isEmpty() || it["serial_ids"].toString() == "0")) {
                            it.put("source_title", "问答")
                        }
                    }
                    1L -> {
                        it.put("source_title", "汽车资讯")
                    }
                    2L -> {
                        it.put("source_title", "店内资讯")
                    }
                    3L -> {
                        it.put("source_title", "汽车生活")
                    }
                    4L -> {
                        it.put("source_title", "生活资讯")
                    }
                    5L -> {
                        it.put("source_title", "问答")
                    }
                }
                if (it["brand_ids"].toString().isEmpty()) {
                    it.put("brand_ids", JSONArray())
                } else {
                    it.put("brand_ids", it["brand_ids"].toString().split(",").map { it.toLong() })
                }
                if (it["serial_ids"].toString().isEmpty() || brokerSeriesList.isEmpty()) {
                    it.put("cars", JSONArray())
                    it.put("serial_ids", JSONArray())
                } else {
                    val serialIds = it["serial_ids"].toString().split(",")
                    var queryId: String? = null
                    run {
                        brokerSeriesList.forEach({ id ->
                            serialIds.forEach(
                                    {
                                        if (it == id.toString()) {
                                            queryId = it
                                            return@run
                                        }
                                    }
                            )
                        })
                    }
                    if (queryId.isNullOrEmpty()) {
                        it.put("cars", JSONArray())
                        it.put("serial_ids", JSONArray())
                    } else {
                        it.put("cars", JSONArray(materielExtMapper.getSeriesName(listOf(queryId!!))))
                        it.put("serial_ids", JSONArray(listOf(queryId!!).map { it.toLong() }))
                    }
                }
                if (it.get("image") == null || it.get("image")!!.toString().isEmpty()) {
                    it.put("image", "http://img1.qcdqcdn.com/group1/M00/1E/E5/o4YBAFrLPAuANXNNAAD2feHlG68950.jpg")
                }
//                it.put("link", "http://dealer.h5.qichedaquan.com/${param!!["broker_id"]}/KonwledgeDetail/${it["materiel_id"]}/0")
                it.put("link", "https://h5.qichedaquan.com/weidian/news/index.html?agentId=${param!!["broker_id"]}&newsId=${it["materiel_id"]}")
            }
            pagerFor.putList(list)
        }
        return pagerFor
    }

    fun getMaterielById(materiel_id: Long, broker_id: Long): Map<String, Any>? {
        val map = materielMapper.getMaterielById(materiel_id)
        if (map != null) {
            val list = brokerMapper.getSeriesByBrokerId(broker_id)
            if (map["brand_ids"].toString().isEmpty()) {
                map.put("brand_ids", JSONArray())
            } else {
                map.put("brand_ids", map["brand_ids"].toString().split(",").map { it.toLong() })
            }
            val serialIds = map["serial_ids"].toString().split(",")
            var queryId: String? = null
            run {
                list.forEach({ id ->
                    serialIds.forEach(
                            {
                                if (it == id.toString()) {
                                    queryId = it
                                    return@run
                                }
                            }
                    )
                })
            }
            if (queryId.isNullOrEmpty()) {
                map.put("cars", JSONArray())
                map.put("serial_ids", JSONArray())
            } else {
                map.put("cars", JSONArray(materielExtMapper.getSeriesInfo(listOf(queryId!!)).map {
                    it.put("serial_id", queryId!!.toLong())
                    if (it.get("image") != null) {
                        it.put("image", getImageUrl(it.get("image").toString()))
                    }
                    if (it.get("dealer_price_desc") != null) {
                        it.put("dealer_price_desc", it.get("dealer_price_desc").toString() + "万")
                    }
                    it
                }))
                map.put("serial_ids", JSONArray(listOf(queryId!!)))
            }
//            map.put("link", "http://dealer.h5.qichedaquan.com/$broker_id/KonwledgeDetail/${map["materiel_id"]}/0")
            map.put("link", "https://h5.qichedaquan.com/weidian/news/index.html?agentId=$broker_id&newsId=${map["materiel_id"]}")
        }
        return map
    }

    fun getImageUrl(pathUrl: String): String {
        var pathUrl = pathUrl
        if (pathUrl.toLowerCase().startsWith("http://") || pathUrl.toLowerCase().startsWith("https://")) {
            return pathUrl
        }
        if (pathUrl.startsWith("/group1") || pathUrl.startsWith("/group2")) {
            pathUrl = pathUrl.substring(1, pathUrl.length)
        }
        if (pathUrl.startsWith("group1")) {
            pathUrl = "http://img1.qcdqcdn.com/" + pathUrl
        }
        if (pathUrl.startsWith("group2")) {
            pathUrl = "http://img2.qcdqcdn.com/" + pathUrl
        }
        return pathUrl
    }

//    @Async
    fun delBrokerMaterielCache(broker_id: Long = 0, dealer_id: Long = 0, materiel_id: Long = 0) {
//        if (broker_id.compareTo(0) != 0) {
//            val key = "qcdq:sp:$broker_id:*"
//            cache.removePattern(key)
//        }
//        if (dealer_id.compareTo(0) != 0) {
//            val list = brokerMapper.getBrokerByDealerId(dealer_id)
//            list.forEach {
//                val key = "qcdq:sp:${it.brokerId}:*"
//                cache.removePattern(key)
//            }
//        }
//        if (materiel_id.compareTo(0) != 0) {
//            val count = distributeService.distributeMapper.selectCount(Distribute(materielId = materiel_id))
//            if (count > 0) {
//                val pageNum = count / 500 + if (count % 500 == 0) 0 else 1
//                for (i in 1..pageNum) {
//                    //当前处理的页码
//                    PageHelper.startPage<Answer>(i, 500)
//                    //排序
//                    PageHelper.orderBy("id asc")
//                    val list = distributeService.distributeMapper.select(Distribute(materielId = materiel_id))
//                    list.forEach {
//                        val key = "qcdq:sp:${it.brokerId}:*"
//                        cache.removePattern(key)
//                    }
//                }
//            }
//        }
    }

}


