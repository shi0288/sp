package com.xyauto.qa.util

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

/**
 * Created by shiqm on 2017-09-05.
 */
@Component
class Cache(var redisTemplate: StringRedisTemplate) {

    operator fun set(key: String, value: String): Boolean {
        var result = false
        try {
            redisTemplate.opsForValue().set(key, value)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    operator fun set(key: String, value: String, expireTime: Long?): Boolean {
        var result = false
        try {
            redisTemplate.opsForValue().set(key, value)
            redisTemplate.expire(key, expireTime!!, TimeUnit.SECONDS)
            result = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    fun remove(vararg keys: String) {
        for (key in keys) {
            remove(key)
        }
    }

    fun removePattern(pattern: String) {
        val keys = redisTemplate.keys(pattern)
        if (keys.size > 0) {
            redisTemplate.delete(keys)
        }
    }

    fun remove(key: String) {
        if (exists(key)) {
            redisTemplate.delete(key)
        }
    }

    fun exists(key: String): Boolean {
        return redisTemplate.hasKey(key)!!
    }

    operator fun get(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    fun hmSet(key: String, hashKey: String, value: String) {
        redisTemplate.opsForHash<Any, Any>().put(key, hashKey, value)
    }

    fun hmGet(key: String, hashKey: String): String {
        return redisTemplate.opsForHash<Any, Any>().get(key, hashKey) as String
    }

    fun lPush(k: String, v: String) {
        redisTemplate.opsForList().rightPush(k, v)
    }

    fun lRange(k: String, l: Long, l1: Long): List<String> {
        return redisTemplate.opsForList().range(k, l, l1)
    }

    fun add(key: String, value: String) {
        redisTemplate.opsForSet().add(key, value)
    }

    fun setMembers(key: String): Set<String> {
        return redisTemplate.opsForSet().members(key)
    }

    fun zAdd(key: String, value: String, scoure: Double) {
        redisTemplate.opsForZSet().add(key, value, scoure)
    }

    fun rangeByScore(key: String, scoure: Double, scoure1: Double): Set<String> {
        return redisTemplate.opsForZSet().rangeByScore(key, scoure, scoure1)
    }

    fun keys_count(pattern: String): Int {
        val keys = redisTemplate.keys(pattern)
        return keys.size
    }

    fun setnx(key: String, value: String, seconds: Long): Boolean {
        val ret = redisTemplate.opsForValue().setIfAbsent(key, value)!!
        if (ret) {
            return if (seconds > 0) {
                redisTemplate.expire(key, seconds, TimeUnit.SECONDS)!!
            } else {
                true
            }
        }
        return false
    }

    fun ttl(key: String): Long {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)!!
    }


}