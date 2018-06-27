package com.xyauto.qa.controller.rss

import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.service.Qh360Service
import com.xyauto.qa.util.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by shiqm on 2017-09-05.
 */

@RestController
@RequestMapping("task")
class Qh360Task : BaseController() {


    @Autowired
    lateinit var qh360Service: Qh360Service


    @RequestMapping("360")
    fun synchronizeQuestion(@Check task_id: String): Result {
        qh360Service.syncQuestion(task_id)
        return Result()
    }
}

