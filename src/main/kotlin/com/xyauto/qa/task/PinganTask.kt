package com.xyauto.qa.task

import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.service.PinganService
import com.xyauto.qa.util.BaseController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by shiqm on 2017-11-06.
 */

@RestController
@RequestMapping("task")
class PinganTask : BaseController() {

    @Autowired
    lateinit var pinganService: PinganService



    @RequestMapping("pingan/answer")
    fun synchronizeAnswer(@Check task_id: String): Result {
        pinganService.syncAnswer(task_id)
        return Result()
    }





}