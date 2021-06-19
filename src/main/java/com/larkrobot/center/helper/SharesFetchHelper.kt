package com.larkrobot.center.helper

import okhttp3.*
import java.io.IOException

object SharesFetchHelper {

    private const val SHANGZHENG = "sh000001"
    private const val SHENZHENG = "sz399001"
    private const val CHUANYE = "sz399006"
    private const val ZHONGZHENGBAIJIU = "sz399997"
    private const val TIMES_URL = "http://image.sinajs.cn/newchart/min/n/"
    private const val K_URL = "http://image.sinajs.cn/newchart/daily/n/"
    private val MAIN_BLOCK_URL = "http://hq.sinajs.cn/list=$SHANGZHENG,$SHENZHENG,$CHUANYE,$ZHONGZHENGBAIJIU"

    @JvmStatic
    fun requestShares() {
        Thread {
            OkHttpClient().newCall(Request.Builder().apply {
                this.url(MAIN_BLOCK_URL)
                this.addHeader("Content-Type", "application/json; charset=utf-8")
                this.get()
            }.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.code == 200) {
                        val indexArray = response.body?.string().toString().split("\n")

                        val sharesName: ArrayList<String> = ArrayList()
                        val shareContents: ArrayList<String> = ArrayList()
                        val sharesTimesWebUrls: ArrayList<String> = ArrayList()
                        val sharesKWebUrls: ArrayList<String> = ArrayList()
                        indexArray.forEachIndexed { _, s ->
                            val startIndex = s.indexOf("\"")
                            val endIndex = s.lastIndexOf("\"")
                            if (startIndex == -1 || endIndex <= startIndex) {
                                return@forEachIndexed
                            }
                            val valueArray = s.subSequence(startIndex + 1, endIndex - 1).split(",")
                            sharesName.add("股票名字 : ${valueArray[0]}")
                            val sendText = "今日开盘价 : ${valueArray[1]}，" +
                                    "昨日收盘价 : ${valueArray[2]}，" +
                                    "当前价格 : ${valueArray[3]}，" +
                                    "今日最高价 : ${valueArray[4]}，" +
                                    "今日最低价 : ${valueArray[5]}"
                            shareContents.add(sendText)
                            if (s.contains(SHANGZHENG)){
                                sharesTimesWebUrls.add("$TIMES_URL$SHANGZHENG.gif")
                                sharesKWebUrls.add("$K_URL$SHANGZHENG.gif")
                            }else if (s.contains(SHENZHENG)){
                                sharesTimesWebUrls.add("$TIMES_URL$SHENZHENG.gif")
                                sharesKWebUrls.add("$K_URL$SHENZHENG.gif")
                            }else if (s.contains(CHUANYE)){
                                sharesTimesWebUrls.add("$TIMES_URL$CHUANYE.gif")
                                sharesKWebUrls.add("$K_URL$CHUANYE.gif")
                            }else if (s.contains(ZHONGZHENGBAIJIU)){
                                sharesTimesWebUrls.add("$TIMES_URL$ZHONGZHENGBAIJIU.gif")
                                sharesKWebUrls.add("$K_URL$ZHONGZHENGBAIJIU.gif")
                            }
                        }
                        val sendObj = TextMessageBuilder.buildSharesText("今日股市推送", sharesName, shareContents, sharesTimesWebUrls, sharesKWebUrls)
                        LarkRobotSendHelper.sendTextMessage(sendObj)
                    }
                }
            })
        }.start()
    }
}