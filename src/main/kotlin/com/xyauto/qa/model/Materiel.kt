package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import java.util.*
import javax.persistence.*

/**
 * Created by shiqm on 2018-01-23.
 */
@PoKo
@Table(name = "broker_materiel")
data class Materiel(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "name")
        var name: String? = null,

        var type: Short? = null,

        @Column(name = "content_type")
        var contentType: Short? = null,

        var source: Short? = null,

        @Column(name = "original_id")
        var originalId: String? = null,

        @Column(name = "publish_time")
        var publishTime: Int? = null,

        @Column(name = "task_id")
        var taskId: String? = null,

        @Column(name = "city_ids")
        var cityIds: String? = null,

        @Column(name = "province_ids")
        var provinceIds: String? = null,

        @Column(name = "is_join_car")
        var isJoinCar: Short? = null,

        @Column(name = "dealer_ids")
        var dealerIds: LongArray? = null,

        @Column(name = "brand_ids")
        var brandIds: String? = null,

        @Column(name = "serial_ids")
        var serialIds: String? = null,

        @Column(name = "ask_type")
        var askType: Short? = null,

        @Column(name = "ask_uid")
        var askUid: Long? = null,

        @Column(name = "ask_id")
        var askId: Long? = null,

        @Column(name = "template_id")
        var templateId: Long? = null,

        var status: Short? = null,

        @Column(name = "create_time")
        var createTime: Date? = null,

        @Column(name = "update_time")
        var updateTime: Date? = null,

        @Column(name = "is_deleted")
        var isDeleted: Short? = null

)
