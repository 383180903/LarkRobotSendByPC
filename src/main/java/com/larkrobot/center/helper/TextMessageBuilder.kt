package com.larkrobot.center.helper

import net.sf.json.JSONArray
import net.sf.json.JSONObject

object TextMessageBuilder {

    @JvmStatic
    fun buildNormalText(text: String): JSONObject {
        return JSONObject().apply {
            put("msg_type", "text")
            put("content", JSONObject().apply {
                put("text", text)
            })
        }
    }

    @JvmStatic
    fun buildMenuText(title: String, imageKey: String, width: Int, height: Int): JSONObject {
        return JSONObject().apply {
            put("msg_type", "post")
            put("content", JSONObject().apply {
                put("post", JSONObject().apply {
                    put("zh_cn", JSONObject().apply {
                        put("title", title)
                        put("content", JSONArray().apply {
                            add(JSONArray().apply {
                                add(JSONObject().apply {
                                    put("tag", "img")
                                    put("image_key", imageKey)
                                    put("width", width)
                                    put("height", height)
                                })
                            })
                        })
                    })
                })
            })
        }
    }

    @JvmStatic
    fun buildNewsText(title: String, newsTitles: ArrayList<String>, newsTimes: ArrayList<String>, newsWebUrls: ArrayList<String>): JSONObject {
        return JSONObject().apply {
            put("msg_type", "post")
            put("content", JSONObject().apply {
                put("post", JSONObject().apply {
                    put("zh_cn", JSONObject().apply {
                        put("title", title)
                        put("content", JSONArray().apply {
                            for (index in newsTitles.indices) {
                                add(JSONArray().apply {
                                    add(JSONObject().apply {
                                        put("tag", "text")
                                        put("text", newsTimes[index] + "  \n")
                                    })
                                    add(JSONObject().apply {
                                        put("tag", "text")
                                        put("text", newsTitles[index] + "  ")
                                    })
                                    add(JSONObject().apply {
                                        put("tag", "a")
                                        put("text", "#查看新闻\n")
                                        put("href", newsWebUrls[index])
                                    })
                                })
                            }
                        })
                    })
                })
            })
        }

    }

    @JvmStatic
    fun buildSharesText(title: String, sharesName: ArrayList<String>, shareContents: ArrayList<String>, sharesTimesWebUrls: ArrayList<String> ,sharesKWebUrls: ArrayList<String>): JSONObject {
        return JSONObject().apply {
            put("msg_type", "post")
            put("content", JSONObject().apply {
                put("post", JSONObject().apply {
                    put("zh_cn", JSONObject().apply {
                        put("title", title)
                        put("content", JSONArray().apply {
                            for (index in sharesName.indices) {
                                add(JSONArray().apply {
                                    add(JSONObject().apply {
                                        put("tag", "text")
                                        put("text", sharesName[index] + "  \n")
                                    })
                                    add(JSONObject().apply {
                                        put("tag", "text")
                                        put("text", shareContents[index] + "\n")
                                    })
                                    add(JSONObject().apply {
                                        put("tag", "a")
                                        put("text", "#查看分时图 - ")
                                        put("href", sharesTimesWebUrls[index])
                                    })
                                    add(JSONObject().apply {
                                        put("tag", "a")
                                        put("text", "#查看K线图\n")
                                        put("href", sharesKWebUrls[index])
                                    })
                                })
                            }
                        })
                    })
                })
            })
        }

    }
}