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
    fun buildMenuText(text: String, imageKey: String, width: Int, height: Int): JSONObject {
        return JSONObject().apply {
            put("msg_type", "post")
            put("content", JSONObject().apply {
                put("post", JSONObject().apply {
                    put("zh_cn", JSONObject().apply {
                        put("title", text)
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
}