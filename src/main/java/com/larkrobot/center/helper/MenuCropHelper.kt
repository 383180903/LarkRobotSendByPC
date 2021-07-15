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

    private const val XOffset = 90
    private const val YOffset = 114

    private const val YOffsetByOCR = 93
    private const val OCRHeight = 20

    //每天截取的菜单宽度
    private const val DailyWidth = 120
    private const val DailyMorningHeight = 156
    private const val DailyNoonHeight = 128
    private const val DailyDinnerHeight = 108


    @JvmStatic
    fun cropMenuAndSend(dayTime: Int) {
        val yStep: Int = calculateDayTime(dayTime)
        val menuFile = File(MenuPath)
        val dstOCRPath = menuFile.parent + File.separator + "dateOcr.png"
        for (xStep in 0 until 5) {
            ImageUtils.cropImage(XOffset + xStep * DailyWidth + DailyWidth / 4, YOffsetByOCR, DailyWidth / 2, OCRHeight , MenuPath, dstOCRPath)
            try {
                val text: String = doOCRFromFile(File(dstOCRPath), OCRUtils.DEFAULT_LANG) ?: ""
                println("识别到的文本是：$text")
                if (!calculateWeekDay(text)) {
                    continue
                }
                val dstImagePath = menuFile.parent + File.separator + "dailyMenu.png"
                val y = when(yStep){
                    0 -> {
                        0
                    }
                    1 -> {
                        DailyMorningHeight
                    }
                    2 -> {
                        DailyMorningHeight + DailyNoonHeight
                    }
                    else ->{
                        0
                    }
                }
                val height = when(yStep){
                    0 -> {
                        DailyMorningHeight
                    }
                    1 -> {
                        DailyNoonHeight
                    }
                    2 -> {
                        DailyDinnerHeight
                    }
                    else ->{
                        0
                    }
                }
                ImageUtils.cropImage(XOffset + xStep * DailyWidth, YOffset + y, DailyWidth, height, MenuPath, dstImagePath)
                buildMenuText(dayTime)?.let { title ->
                    LarkRobotSendHelper.uploadImage(dstImagePath) { imageKey ->
                        LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildMenuText(title, imageKey, DailyWidth, height))
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
                matchText.replace("\n","").endsWith("一")
            }
            WeekDay.Tuesday.value -> {
                matchText.replace("\n","").endsWith("二")
            }
            WeekDay.Wednesday.value -> {
                matchText.replace("\n","").endsWith("三")
            }
            WeekDay.Thursday.value -> {
                matchText.replace("\n","").endsWith("四")
            }
            WeekDay.Friday.value -> {
                matchText.replace("\n","").endsWith("五")
            }
            else -> {
                false
            }
        }
    }
}