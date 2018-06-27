package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import tk.mybatis.mapper.annotation.ColumnType
import java.util.*
import javax.persistence.*

/**
 * Created by shiqm on 2018-01-23.
 */
@PoKo
@Table(name = "broker_materiel_distribute")
data class Distribute(

        @Id
        var id: Long? = null,

        @Column(name = "broker_id")
        var brokerId: Long? = null,

        @Column(name = "materiel_id")
        var materielId: Long? = null,

        var status: Short? = null,

        var type: Short? = null,

        var source: Short? = null,

        @Column(name = "content_type")
        var contentType: Short? = null,

        @Column(name = "task_tag")
        var taskTag: Short? = null,

        @Column(name = "broker_name")
        var brokerName: String? = null,

        @Column(name = "materiel_name")
        var materielName: String? = null,

        @Column(name = "dealer_id")
        var dealerId: Long? = null,

        @Column(name = "brand_ids")
        var brandIds: IntArray? = null,

        @Column(name = "series_ids")
        var seriesIds: IntArray? = null,

        @Column(name = "dealer_name")
        var dealerName: String? = null,

        @Column(name = "is_deleted")
        var isDeleted: Short? = null,

        @Column(name = "create_time")
        var createTime: Date? = null,

        @Column(name = "update_time")
        var updateTime: Date? = null
) {

    fun getTableName() = "broker_materiel_distribute(broker_id,materiel_id,status,type,source,content_type,task_tag,broker_name,materiel_name,dealer_id,brand_ids,series_ids,dealer_name,create_time,update_time)"


    fun toCopyText(): String {
        var serids: String? = null
        if (seriesIds != null && !seriesIds.toString().isEmpty()) {
            serids = seriesIds!!.joinToString(",","{","}")
        }
        var brands: String? = null
        if (brandIds != null && !brandIds.toString().isEmpty()) {
            brands = brandIds!!.joinToString(",","{","}")
        }
        return "${brokerId ?: "\\N"}\t" +
                "${materielId ?: "\\N"}\t" +
                "${status ?: "\\N"}\t" +
                "${type ?: "\\N"}\t" +
                "${source ?: "\\N"}\t" +
                "${contentType ?: "\\N"}\t" +
                "${taskTag ?: "\\N"}\t" +
                "${brokerName ?: "\\N"}\t" +
                "${materielName ?: "\\N"}\t" +
                "${dealerId ?: "\\N"}\t" +
                "${brands ?: "\\N"}\t" +
                "${serids ?: "\\N"}\t" +
                "${dealerName ?: "\\N"}\t" +
                "$createTime\t" +
                "${updateTime ?: "\\N"}\n"
    }

}

