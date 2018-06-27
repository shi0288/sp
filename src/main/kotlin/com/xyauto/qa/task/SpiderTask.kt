package com.xyauto.qa.task

import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.service.SpiderService
import com.xyauto.qa.util.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by shiqm on 2017-11-13.
 */


@RestController
@RequestMapping("task")
class SpiderTask : BaseController() {


    @Autowired
    lateinit var spiderService: SpiderService

    @RequestMapping("spider/question")
    fun synchronizeQuestion(@Check task_id: String): Result {
        spiderService.syncQuestion(task_id)
        return Result()
    }


}