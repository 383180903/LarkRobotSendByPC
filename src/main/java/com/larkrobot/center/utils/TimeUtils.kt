package com.larkrobot.center.utils

import java.util.*

object TimeUtils {

    @JvmStatic
    fun getTodayOfWeek(): Int {
        val c = Calendar.getInstance()
        c.time = Date()
        return c[Calendar.DAY_OF_WEEK] - 1
    }

}