package com.xyauto.qa.service

import com.xyauto.qa.mapper.broker.MaterielMapper
import com.xyauto.qa.model.Broker
import com.xyauto.qa.util.CacheMemory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by shiqm on 2018-04-12.
 */
@Service
class BrokerService {

    @Autowired
    lateinit var materielMapper: MaterielMapper

    fun saveAllToMongo(collectionName: String) {
        var brokers = materielMapper.getAllBrokerIds()
        val conMap = ConcurrentHashMap<Long, String?>()
        brokers.keys.forEach {
            conMap.put(it, "1")
        }
        brokers.clear()
        CacheMemory.brokerMemory.put(collectionName, conMap)
    }

    fun getBrokerIds(collectionName: String, size: Int): List<Long>? {
        if (size > 0) {
            return if (CacheMemory.brokerMemory[collectionName]!!.size < size) {
                CacheMemory.brokerMemory[collectionName]!!.keys.map { it }
            } else {
                CacheMemory.brokerMemory[collectionName]!!.keys.take(size).map { it }
            }
        }
        return null
    }


    fun getBrokerSize(collectionName: String, size: Int): Int {
        if (CacheMemory.brokerMemory[collectionName]!!.size == 0 || size == 0) {
            return 0
        }
        return CacheMemory.brokerMemory[collectionName]!!.size / size + if (CacheMemory.brokerMemory[collectionName]!!.size % size == 0) 0 else 1
    }


    fun getDistributeBrokerQueue(type: Short, taskTag: Short): String {
        var timeFormat = SimpleDateFormat("yyyyMMdd")
        return "distribute_${timeFormat.format(Date())}_${type}_$taskTag"
    }

    fun collectionExists(collectionName: String): Boolean {
        if (CacheMemory.brokerMemory.containsKey(collectionName)) {
            if (CacheMemory.brokerMemory[collectionName]!!.size == 0) {
                return false
            }
            return true
        }
        return false
    }

    fun existBroker(collectionName: String, broker: Broker): Boolean {
        if (!CacheMemory.brokerMemory.containsKey(collectionName)) {
            return false
        }
        return CacheMemory.brokerMemory[collectionName]!!.containsKey(broker.brokerId)
    }


    fun delBroker(collectionName: String, broker: Broker) {
        if (!CacheMemory.brokerMemory.containsKey(collectionName)) {
            return
        }
        CacheMemory.brokerMemory[collectionName]!!.remove(broker.brokerId)
    }


    fun brokerCount(collectionName: String): Int {
        if (!CacheMemory.brokerMemory.containsKey(collectionName)) {
            return 0
        }
        return CacheMemory.brokerMemory[collectionName]!!.size
    }


    fun dropCollection(collectionName: String) {
        CacheMemory.brokerMemory.remove(collectionName)
    }

}
