package com.larkrobot.center.helper

import com.larkrobot.center.model.DayTime
import com.larkrobot.center.model.WeekDay
import com.larkrobot.center.utils.ImageUtils
import com.larkrobot.center.utils.TimeUtils
import java.io.File


object MenuCropHelper {

    private const val MenuPath = "D:/menu/lizhi_menu.png"

    private const val XOffset = 64
    private const val YOffset = 64

    //每天截取的菜单宽度
    private const val DailyWidth = 205
    private const val DailyHeight = 105

    @JvmStatic
    fun cropMenuAndSend(dayTime: Int) {
        val xStep: Int = calculateWeekDay()
        if (xStep == -1) {
            println("周末没得吃啊")
            return
        }
        val yStep: Int = calculateDayTime(dayTime)
        val menuFile = File(MenuPath)
        val dstImagePath = menuFile.parent + File.separator + "dailyMenu.png"
        ImageUtils.cropImage(XOffset + xStep * DailyWidth, YOffset + yStep * DailyHeight, DailyWidth, DailyHeight, MenuPath, dstImagePath)
        buildMenuText(dayTime)?.let {
            LarkRobotSendHelper.sendTextMessage(it)
        }
        LarkRobotSendHelper.sendImageMessage(dstImagePath)
    }

    private fun buildMenuText(dayTime: Int): String? {
        return when (dayTime) {
            DayTime.Morning.value -> {
                "米娜桑，早餐菜单来啦~"
            }
            DayTime.Noon.value -> {
                "米娜桑，午餐菜单来啦~"
            }
            DayTime.Dinner.value -> {
                "米娜桑，晚餐菜单来啦~"
            }
            else -> {
                null
            }
        }
    }

    private fun calculateDayTime(dayTime: Int): Int {
        return when (dayTime) {
            DayTime.Morning.value -> {
                0
            }
            DayTime.Noon.value -> {
                1
            }
            DayTime.Dinner.value -> {
                2
            }
            else -> {
                -1
            }
        }
    }

    private fun calculateWeekDay(): Int {
        return when (TimeUtils.getTodayOfWeek()) {
            WeekDay.Monday.value -> {
                0
            }
            WeekDay.Tuesday.value -> {
                1
            }
            WeekDay.Wednesday.value -> {
                2
            }
            WeekDay.Thursday.value -> {
                3
            }
            WeekDay.Friday.value -> {
                4
            }
            else -> {
                -1
            }
        }
    }
}