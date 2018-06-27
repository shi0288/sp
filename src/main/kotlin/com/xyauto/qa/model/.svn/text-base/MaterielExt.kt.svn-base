package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

/**
 * Created by shiqm on 2018-01-23.
 */
@PoKo
@Table(name = "broker_materiel_ext")
data class MaterielExt(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column(name = "materiel_id")
        var materielId: Long? = null,

        var content: String? = null,

        var summary: String? = null,

        @Column(name = "view_count")
        var viewCount: Int? = null,

        @Column(name = "agree_count")
        var agreeCount: Int? = null,

        var image: String? = null,

        @Column(name = "source_name")
        var sourceName: String? = null,

        @Column(name = "category_name")
        var categoryName: String? = null,

        @Column(name = "tag")
        var tag: String? = null,

        @Column(name = "score")
        var score: BigDecimal? = null,

        @Column(name = "city_name")
        var cityName: String? = null,

        @Column(name = "distribute_count")
        var distributeCount: Int? = null,

        @Column(name = "create_time")
        var createTime: Date? = null,

        @Column(name = "update_time")
        var updateTime: Date? = null,

        @Column(name = "is_deleted")
        var isDeleted: Short? = null


)