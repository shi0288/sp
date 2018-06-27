package com.xyauto.qa.controller

import com.mcp.fastcloud.util.Result
import com.mcp.validate.annotation.Check
import com.xyauto.qa.service.UserDistributeLogService
import com.xyauto.qa.util.BaseController
import com.xyauto.qa.util.TimeValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by shiqm on 2017-12-15.
 */

@RestController
@RequestMapping("qa")
class QaReportController : BaseController() {

    @Autowired
    lateinit var userDistributeLogService: UserDistributeLogService

    @RequestMapping("distribute/report")
    fun getDistributeCount(
            @Check(value = "uids", required = true) uids: Set<Long>,
            @Check(value = "start", required = true, valid = arrayOf(TimeValidator::class), name = "起始时间") start: Long,
            @Check(value = "end", required = true, valid = arrayOf(TimeValidator::class), name = "结束时间") end: Long
    ): Any = Result(userDistributeLogService.getUserDistributeLostByUids(uids.toList(), start, end))

}