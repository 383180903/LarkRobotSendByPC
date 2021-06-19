package com.larkrobot.center.helper

import net.sf.json.JSONObject
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.StringReader

/**
 * 天气获取的类
 */
object WeatherFetchHelper {

    private const val WeatherUrl = "https://way.jd.com/jisuapi/weather?city=广州&cityid=020&citycode=&appkey=5d315785afc0b3b42e2d585a3eaf3231"

    @JvmStatic
    fun requestWeather() {
        Thread {
            OkHttpClient().newCall(Request.Builder().apply {
                this.url(WeatherUrl)
                this.addHeader("Content-Type", "application/json; charset=utf-8")
                this.get()
            }.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        val jsonObject = JSONObject.fromObject(response.body?.string())
                        if (jsonObject != null) {
                            buildWeatherMessage((jsonObject.opt("result") as JSONObject).opt("result") as JSONObject?)
                        }
                    }
                }
            })
        }.start()
    }

    private fun buildWeatherMessage(jsonObject: JSONObject?) {
        jsonObject?.let { opt ->
            val city = opt.opt("city")
            val date = opt.opt("date")
            val week = opt.opt("week")
            val weather = opt.optString("weather")
            val temp = opt.opt("temp")
            val tempHigh = opt.opt("temphigh")
            val tempLow = opt.opt("templow")
            val windSpeed = opt.opt("windspeed")

            val advice = if(weather.contains("雨")){
                "建议带伞出门哦"
            }else {
                "早走早享受"
            }
            val sendText = "今日天气预报：\n" +
                    "城市：$city\n" +
                    "日期：$date $week\n" +
                    "天气：$weather\n" +
                    "当前温度：$temp℃\n" +
                    "温度区间：$tempLow ~ $tempHigh℃\n" +
                    "风速：$windSpeed\n" +
                    "出行建议：$advice"

            println(sendText)
            LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildNormalText(sendText))
        }

    }
}