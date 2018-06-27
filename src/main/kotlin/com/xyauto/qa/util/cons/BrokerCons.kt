package com.xyauto.qa.util.cons

/**
 * Created by shiqm on 2018-01-27.
 */

class BrokerCons {

    object DistributeStatusFlag {
        /**
         * 定时执行状态
         */
        const val TIMER: Short = 1


        /**
         * 立即执行状态
         */
        const val ACTUAL: Short = 2
    }


    object DistributeTypeFlag {
        /**
         * 大全
         */
        const val QCDQ: Short = 1


        /**
         * 赤兔
         */
        const val RED_RABBIT: Short = 2

        /**
         * 行圆慧
         */
        const val XY: Short = 3

        /**
         * 定投
         */
        const val DT: Short = 4
    }

    object DistributeTaskFlag {
        /**
         * 9
         */
        const val TIME_9: Short = 1
        /**
         * 12
         */
        const val TIME_12: Short = 2
        /**
         * 15
         */
        const val TIME_15: Short = 3
        /**
         * 17：30
         */
        const val TIME_17_30: Short = 4
        /**
         * 20：00
         */
        const val TIME_20: Short = 5
    }

}