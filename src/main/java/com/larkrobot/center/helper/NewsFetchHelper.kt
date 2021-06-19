package com.larkrobot.center.helper

import net.sf.json.JSONArray
import net.sf.json.JSONObject
import okhttp3.*
import java.io.IOException

object NewsFetchHelper {
    private const val NewsUrl = "https://way.jd.com/jisuapi/get?channel=头条&num=8&start=0&appkey=5d315785afc0b3b42e2d585a3eaf3231"

    @JvmStatic
    fun requestNews() {
        Thread {
            OkHttpClient().newCall(Request.Builder().apply {
                this.url(NewsUrl)
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
                            buildNewsMessage((jsonObject.opt("result") as JSONObject).opt("result") as JSONObject?)
                        }
                    }
                }
            })
        }.start()
    }

    private fun buildNewsMessage(jsonObject: JSONObject?) {
        jsonObject?.let { json ->
            val channel = json.opt("channel")
            val newsArray = (json.opt("list") as JSONArray)
            val newsTitles: ArrayList<String> = ArrayList()
            val newsTimes: ArrayList<String> = ArrayList()
            val newsWebUrls: ArrayList<String> = ArrayList()
            for (index in newsArray.indices) {
                val jsonObj = newsArray[index] as JSONObject
                newsTitles.add(jsonObj.optString("title"))
                newsTimes.add(jsonObj.optString("time"))
                newsWebUrls.add(jsonObj.optString("weburl"))
            }
            val sendObj = TextMessageBuilder.buildNewsText("今日新闻来自于:$channel", newsTitles, newsTimes, newsWebUrls)
            LarkRobotSendHelper.sendTextMessage(sendObj)
        }

    }
}