package com.larkrobot.center.helper

import java.util.*


object TimerSendHelper {
    private const val PERIOD_DAY = 24 * 60 * 60 * 1000.toLong()
    private var timer: Timer? = null

    @JvmStatic
    fun startTimer() {

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 1)
            set(Calendar.MINUTE, 54)
            set(Calendar.SECOND, 0)
        }

        var date = calendar.time
        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(Date())) {
            date = this.addDay(date, 1);
        }

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                println("定时器开启啦 - ${date.time}")
            }
        }, date, PERIOD_DAY)
    }

    @JvmStatic
    fun endTimer() {
        timer?.cancel()
        timer = null
    }

    private fun addDay(date: Date?, num: Int): Date? {
        val startDT = Calendar.getInstance()
        startDT.time = date
        startDT.add(Calendar.DAY_OF_MONTH, num)
        return startDT.time
    }
}