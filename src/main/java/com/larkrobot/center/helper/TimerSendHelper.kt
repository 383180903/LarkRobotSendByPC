package com.larkrobot.center.helper

import com.larkrobot.center.IDoTask
import com.larkrobot.center.model.TimeBean
import java.util.*
import kotlin.collections.HashMap

object TimerSendHelper {

    private const val PERIOD_DAY = 24 * 60 * 60 * 1000.toLong()
    private var timerMap: HashMap<String, Timer> = HashMap()

    @JvmStatic
    fun startTimer(time: TimeBean, task: IDoTask) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hours)
            set(Calendar.MINUTE, time.mins)
            set(Calendar.SECOND, time.second)
        }

        var date = calendar.time
        //如果第一次执行定时任务的时间 小于 当前的时间
        //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(Date())) {
            date = this.addDay(date, 1)
        }
        val key = buildTimerKey(time)
        timerMap[key] = Timer()
        timerMap[key]?.schedule(object : TimerTask() {
            override fun run() {
                task.doTask(1)
            }
        }, date, PERIOD_DAY)
    }

    @JvmStatic
    fun endTimer(timerKey: String) {
        if (timerMap.containsKey(timerKey)) {
            timerMap[timerKey]?.cancel()
            timerMap.remove(timerKey)
        }
    }

    @JvmStatic
    fun buildTimerKey(time: TimeBean): String {
        return with(time) {
            "$hours-$mins-$second"
        }
    }

    private fun addDay(date: Date?, num: Int): Date? {
        val startDT = Calendar.getInstance()
        startDT.time = date
        startDT.add(Calendar.DAY_OF_MONTH, num)
        return startDT.time
    }
}