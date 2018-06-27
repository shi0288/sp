package com.xyauto.qa.service

import com.github.pagehelper.PageHelper
import com.mcp.fastcloud.annotation.Log
import com.xyauto.qa.mapper.broker.BrokerMapper
import com.xyauto.qa.mapper.log.DistributeMapper
import com.xyauto.qa.mapper.broker.MaterielExtMapper
import com.xyauto.qa.mapper.broker.MaterielMapper
import com.xyauto.qa.model.Broker
import com.xyauto.qa.model.Distribute
import com.xyauto.qa.util.Pager
import com.xyauto.qa.util.PagerForMax
import com.xyauto.qa.util.PagerForPage
import com.xyauto.qa.util.PostgreCopyUtil
import com.xyauto.qa.util.assist.businessByPage
import com.xyauto.qa.util.assist.businesstByListPage
import com.xyauto.qa.util.assist.getFileWriter
import com.xyauto.qa.util.cons.BrokerCons
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.BufferedWriter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by shiqm on 2018-01-26.
 */

@Service
class DistributeService {


    @Autowired
    lateinit var distributeMapper: DistributeMapper

    @Autowired
    lateinit var materielExtMapper: MaterielExtMapper

    @Autowired
    lateinit var materielMapper: MaterielMapper

    @Autowired
    lateinit var postgreCopyUtil: PostgreCopyUtil

    @Log
    lateinit var logger: Logger

    @Autowired
    lateinit var brokerService: BrokerService

    @Autowired
    lateinit var brokerMapper: BrokerMapper


    fun uponUserByParam(executeThreadPoolExecutor: ThreadPoolExecutor, distribute: Distribute, serialIds: String? = "", cityIds: String? = "", provinceIds: String? = "", size: Int = 0) {
        val filename = "/tmp/broker_sp_${distribute.status}_${distribute.type}_${distribute.taskTag}_${distribute.materielId}.txt"
        val writer = getFileWriter(filename)
        try {
            when {
            //车型
                !serialIds.isNullOrEmpty() -> {
                    val serialIdsList = serialIds!!.split(",").toList()
                    val count = materielMapper.getUidBySeriesIdsCount(serialIdsList)
                    businessByPage(executeThreadPoolExecutor, count, 50, {
                        val brokers = materielMapper.getUidBySeriesIds(serialIdsList)
                        addDistribute(distribute, writer, brokers = *brokers.toTypedArray())
                    })
                }
            //地区
                !provinceIds.isNullOrEmpty() && provinceIds != "0" -> {
                    val provinceIdsList = provinceIds!!.split(",").toList()
                    val cityIdsList = materielMapper.getCityIdsByParentId(provinceIdsList)
                    if (!cityIds.isNullOrEmpty() && cityIds != "0") {
                        val temp = cityIds!!.split(",").toList()
                        temp.forEach {
                            if (!cityIdsList.contains(it)) {
                                cityIdsList.add(it)
                            }
                        }
                    }
                    val count = materielMapper.getUidByCityIdCount(cityIdsList)
                    businessByPage(executeThreadPoolExecutor, count, 50, {
                        val brokers = materielMapper.getUidByCityId(cityIdsList)
                        addDistribute(distribute, writer, brokers = *brokers.toTypedArray())
                    })
                }
            //地区
                !cityIds.isNullOrEmpty() && cityIds != "0" -> {
                    val cityIdsList = cityIds!!.split(",").toList()
                    val count = materielMapper.getUidByCityIdCount(cityIdsList)
                    businessByPage(executeThreadPoolExecutor, count, 50, {
                        val brokers = materielMapper.getUidByCityId(cityIdsList)
                        addDistribute(distribute, writer, brokers = *brokers.toTypedArray())
                    })
                }
            //全国
                else -> {
                    val collectionName = brokerService.getDistributeBrokerQueue(distribute.type!!, distribute.taskTag!!)
                    if (brokerService.collectionExists(collectionName)) {
                        val brokerIdsList = brokerService.getBrokerIds(collectionName, size)
                        logger.error("分发  ${brokerIdsList!!.size}")
                        if (brokerIdsList != null && brokerIdsList.isNotEmpty()) {
                            businesstByListPage(executeThreadPoolExecutor, brokerIdsList, 300, {
                                val brokers = brokerMapper.getBrokerByIds(it)
                                addDistribute(distribute, writer, brokers = *brokers.toTypedArray())
                            })
                        }
                    } else {
                        val count = materielMapper.getAllUidCount()
                        businessByPage(executeThreadPoolExecutor, count, 300, {
                            val brokers = materielMapper.getAllUid()
                            addDistribute(distribute, writer, brokers = *brokers.toTypedArray())
                        })
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logger.error("error")
        } finally {
            while (true) {
                Thread.sleep(200)
                if (executeThreadPoolExecutor.activeCount == 0) {
                    writer.close()
                    try {
                        postgreCopyUtil.copyFromFile(filename, distribute.getTableName())
                        //更新数量
                        updateMaterielExtCount(distribute.materielId!!)
                    }catch (e:Exception){
                        logger.error("数据库copy失败")
                        e.printStackTrace()
                    }
                    val file = File(filename)
                    file.delete()
                    break
                }
            }
        }
    }

    fun updateMaterielExtCount(materielId: Long) {
        val count = distributeMapper.getDistributeCount(materielId)
        materielExtMapper.updateCount(materielId, count)
    }


    fun addDistribute(distribute: Distribute, buffer: BufferedWriter? = null, vararg brokers: Broker?) {
        when (distribute.status) {
        //定时发送
            BrokerCons.DistributeStatusFlag.TIMER -> {
                timeDistribute(distribute, buffer, brokers = *brokers)
            }
        //实时发送
            BrokerCons.DistributeStatusFlag.ACTUAL -> {
                onceDistribute(distribute, brokers = *brokers)
            }
        }
    }


    /**
     * 定时分发
     */
    fun timeDistribute(distribute: Distribute, buffer: BufferedWriter? = null, vararg brokers: Broker?) {
        var timeFormat = SimpleDateFormat("yyyy-MM-dd")
        val day = timeFormat.format(Date())
        val startTime = "$day 00:00:00"
        val endTime = "$day 23:59:59"
        timeFormat = SimpleDateFormat("yyyyMMdd")
        val collectionName = brokerService.getDistributeBrokerQueue(distribute.type!!, distribute.taskTag!!)
        timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        brokers?.forEach {
            //如果集合中为空，证明已经删除
            if (!brokerService.existBroker(collectionName, it!!)) {
                return@forEach
            }
            var distributeTemp = distribute.copy()
            distributeTemp.brokerId = it!!.brokerId
            distributeTemp.brokerName = it.name
            distributeTemp.dealerId = it.dealerId
            distributeTemp.dealerName = it.dealerName
            distributeTemp.createTime = Date()
            distributeTemp.updateTime = Date()
            //来源分发逻辑分离，正常情况不会有重复的，为了效率不执行
            if (distributeMapper.selectCount(Distribute(brokerId = distributeTemp.brokerId, materielId = distributeTemp.materielId)) > 0) {
                return@forEach
            }
            when (distributeTemp.taskTag) {
                BrokerCons.DistributeTaskFlag.TIME_9 -> {
                    val count = distributeMapper.getUserCount(
                            distributeTemp, BrokerCons.DistributeTaskFlag.TIME_9,
                            timeFormat.parse(startTime),
                            timeFormat.parse(endTime))
                    if (count > 0) {
                        return@forEach
                    }
                }
                BrokerCons.DistributeTaskFlag.TIME_12 -> {
                    var count = distributeMapper.getUserCount(
                            distributeTemp, BrokerCons.DistributeTaskFlag.TIME_12,
                            timeFormat.parse(startTime),
                            timeFormat.parse(endTime))
                    if (count > 0) {
                        count = distributeMapper.getUserCount(
                                distributeTemp, null,
                                timeFormat.parse(startTime),
                                timeFormat.parse(endTime))
                        if (count >= 2) {
                            return@forEach
                        }
                    }
                }
                BrokerCons.DistributeTaskFlag.TIME_15 -> {
                    val count = distributeMapper.getUserCount(
                            distributeTemp, BrokerCons.DistributeTaskFlag.TIME_15,
                            timeFormat.parse(startTime),
                            timeFormat.parse(endTime))
                    if (count > 0) {
                        return@forEach
                    }
                }
                BrokerCons.DistributeTaskFlag.TIME_17_30 -> {
                    var count = distributeMapper.getUserCount(
                            distributeTemp, BrokerCons.DistributeTaskFlag.TIME_17_30,
                            timeFormat.parse(startTime),
                            timeFormat.parse(endTime))
                    if (count > 0) {
                        count = distributeMapper.getUserCount(
                                distributeTemp, null,
                                timeFormat.parse(startTime),
                                timeFormat.parse(endTime))
                        if (count >= 4) {
                            return@forEach
                        }
                    }
                }
                BrokerCons.DistributeTaskFlag.TIME_20 -> {
                    val count = distributeMapper.getUserCount(
                            distributeTemp, BrokerCons.DistributeTaskFlag.TIME_20,
                            timeFormat.parse(startTime),
                            timeFormat.parse(endTime))
                    if (count > 0) {
                        return@forEach
                    }
                }
            }
            buffer!!.write(distributeTemp.toCopyText())
            brokerService.delBroker(collectionName, it!!)
        }

    }


    /**
     * 立即分发
     */
    fun onceDistribute(distribute: Distribute, vararg brokers: Broker?) {
        brokers?.forEach {
            var distributeTemp = distribute.copy()
            distributeTemp.brokerId = it!!.brokerId
            distributeTemp.brokerName = it.name
            distributeTemp.dealerId = it.dealerId
            distributeTemp.dealerName = it.dealerName
            distributeTemp.createTime = Date()
            distributeTemp.updateTime = Date()
            if (distributeMapper.selectCount(Distribute(brokerId = distributeTemp.brokerId, materielId = distributeTemp.materielId)) > 0) {
                logger.info("${distributeTemp.brokerId}--${distributeTemp.materielId}已经推送过")
                return@forEach
            }
            distributeMapper.insertSelective(distributeTemp)
        }
        //更新数量
        updateMaterielExtCount(distribute.materielId!!)
    }


    /**
     * 获取maxIds
     */
    fun getMaterielIds(param: Map<String, Any>, max: String, limit: Int): Pager<Long> {
        val brokerId = param["broker_id"].toString().toLong()
        var source: Int? = null
        if (param["source"] != null) {
            source = param["source"].toString().toInt()
        }
        val count = distributeMapper.getDistributeIdsCount(broker_id = brokerId, source = source)
        if (count == 0) {
            return PagerForMax(count = count.toLong(), limit = limit)
        }
        PageHelper.startPage<Any>(1, limit, false)
        PageHelper.orderBy("create_time desc")
        val pageList = distributeMapper.getDistributeIds(broker_id = brokerId, max = max.toLong(), source = source)
        val ids: ArrayList<Long> = ArrayList()
        var nextMax = StringUtils.EMPTY
        pageList.forEach {
            nextMax = it.id.toString()
            ids.add(it.materielId!!)
        }
        val pager = PagerForMax<Long>(next_max = nextMax, count = count.toLong(), limit = limit, params = param)
        if (!nextMax.isEmpty()) {
            if (distributeMapper.getDistributeIdsCount(broker_id = brokerId, max = nextMax.toLong(), source = source) > 0) {
                pager.has_more = 1
            } else {
                pager.next_max = StringUtils.EMPTY
            }
        }
        pager.ids = ids
        return pager
    }

    /**
     * 获取pageIds
     */
    fun getMaterielIds(param: Map<String, Any>, page: Int, limit: Int): Pager<Long> {
        val brokerId = param["broker_id"].toString().toLong()
        var source: Int? = null
        if (param["source"] != null) {
            source = param["source"].toString().toInt()
        }
        PageHelper.startPage<Any>(page, limit)
        PageHelper.orderBy("create_time desc")
        val pageList = distributeMapper.getDistributeIds(broker_id = brokerId, source = source)
        val ids: ArrayList<Long> = ArrayList()
        pageList.forEach {
            ids.add(it.materielId!!)
        }
        val pager = PagerForPage<Long>(count = pageList.total, limit = limit, page = page, pages = pageList.pages, params = param)
        pager.ids = ids
        return pager
    }

    fun get(distribute: Distribute): Distribute? = distributeMapper.selectOne(distribute)

    fun del(broker_id: Long, materiel_id: Long): Int = distributeMapper.del(broker_id, materiel_id)

    fun delByDealer(dealer_id: Long, materiel_id: Long): Int = distributeMapper.delByDealer(dealer_id, materiel_id)


}

