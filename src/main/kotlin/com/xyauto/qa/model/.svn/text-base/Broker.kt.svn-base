package com.xyauto.qa.model

import com.xyauto.qa.util.annotation.PoKo
import javax.persistence.Column
import javax.persistence.Id

/**
 * Created by shiqm on 2018-02-07.
 */
@PoKo
data class Broker(
        @Id
        @Column(name = "broker_id")
        var brokerId: Long? = null,

        var name: String? = null,

        @Column(name = "dealer_id")
        var dealerId: Long? = null,

        @Transient
        @Column(name = "dealer_name")
        var dealerName: String? = null

)