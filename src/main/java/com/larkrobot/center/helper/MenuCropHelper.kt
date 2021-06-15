package com.larkrobot.center.helper

import com.larkrobot.center.model.DayTime
import com.larkrobot.center.model.WeekDay
import com.larkrobot.center.utils.ImageUtils
import com.larkrobot.center.utils.OCRUtils
import com.larkrobot.center.utils.OCRUtils.doOCRFromFile
import com.larkrobot.center.utils.TimeUtils
import java.io.File


object MenuCropHelper {

    private const val MenuPath = "D:/LarkRobot/menu/lizhi_menu.png"

    private const val XOffset = 64
    private const val YOffset = 64

    private const val YOffsetByOCR = 93

    //每天截取的菜单宽度
    private const val DailyWidth = 205
    private const val DailyHeight = 105


    @JvmStatic
    fun cropMenuAndSend(dayTime: Int) {
        val yStep: Int = calculateDayTime(dayTime)
        val menuFile = File(MenuPath)
        val dstOCRPath = menuFile.parent + File.separator + "dateOcr.png"
        for (xStep in 0 until 5) {
            ImageUtils.cropImage(XOffset + xStep * DailyWidth + DailyWidth / 3, YOffsetByOCR, DailyWidth / 3, DailyHeight / 4, MenuPath, dstOCRPath)
            try {
                val text: String = doOCRFromFile(File(dstOCRPath), OCRUtils.DEFAULT_LANG) ?: ""
                println("识别到的文本是：$text")
                if (!calculateWeekDay(text)) {
                    continue
                }
                val dstImagePath = menuFile.parent + File.separator + "dailyMenu.png"
                ImageUtils.cropImage(XOffset + xStep * DailyWidth, YOffset + yStep * DailyHeight, DailyWidth, DailyHeight, MenuPath, dstImagePath)
                buildMenuText(dayTime)?.let { title ->
                    LarkRobotSendHelper.uploadImage(dstImagePath) { imageKey ->
                        LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildMenuText(title, imageKey, DailyWidth, DailyHeight))
                    }
                }
                break
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun buildMenuText(dayTime: Int): String? {
        return when (dayTime) {
            DayTime.Morning.value -> {
                "米娜桑，清晨的第一缕阳光就是龙司的早餐啦~"
            }
            DayTime.Noon.value -> {
                "米娜桑，快12点了，别写代码了，开饭啦~"
            }
            DayTime.Dinner.value -> {
                "米娜桑，6点几啦，食饭先啦~"
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

    private fun calculateWeekDay(matchText: String): Boolean {
        return when (TimeUtils.getTodayOfWeek()) {
            WeekDay.Monday.value -> {
                matchText.contains("一")
            }
            WeekDay.Tuesday.value -> {
                matchText.contains("二")
            }
            WeekDay.Wednesday.value -> {
                matchText.contains("三")
            }
            WeekDay.Thursday.value -> {
                matchText.contains("四")
            }
            WeekDay.Friday.value -> {
                matchText.contains("五")
            }
            else -> {
                false
            }
        }
    }
}