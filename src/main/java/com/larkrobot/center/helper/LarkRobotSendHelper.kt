package com.larkrobot.center.helper

import com.larkrobot.center.utils.ImageUtils
import net.sf.json.JSONObject
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.Exception

/**
 *  Created by wuxiaofeng on 2021/6/10.
 */
object LarkRobotSendHelper {

    private const val TAG = "LarkRobotSendHelper"

    private const val AppId = "cli_a04b0481c239500e"
    private const val AppSecret = "eZ9yD8AatBG5M7bkmJQRzhSGCtp6i3cx"
    private const val Boundary = "---12erwr345QWE22231R"

    private const val MessageCommonUrl =
            "https://open.feishu.cn/open-apis/bot/v2/hook/177abf5c-90af-4fc0-8290-1fa1a516e672"
    private const val ImageUploadUrl =
            "https://open.feishu.cn/open-apis/im/v1/images"
    private const val AccessTokenUrl =
            "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal/"

    @JvmStatic
    fun sendTextMessage(text: String) {
        buildRequest(JSONObject().apply {
            put("msg_type", "text")
            put("content", JSONObject().apply {
                put("text", text)
            })
        }.toString())
    }

    @JvmStatic
    fun sendImageMessage(imagePath: String) {
        Thread(Runnable {
            val tokenResp = requestToken()
            if (tokenResp.code == 200) {
                val token = parseToken(tokenResp.body?.string())
                val imageBytes = imageToBytes(imagePath)
                val uploadResp = uploadImage(imageBytes, token)
                if (uploadResp.code == 200) {
                    val imageKey = parseImageKey(uploadResp.body?.string())
                    buildRequest(JSONObject().apply {
                        put("msg_type", "image")
                        put("content", JSONObject().apply {
                            put("image_key", imageKey)
                        })
                    }.toString())
                } else {
                    System.out.println("$TAG uploadResp code:${uploadResp.code}")
                }
            } else {
                System.out.println("$TAG tokenResp code:${tokenResp.code}")
            }
        }).start()
    }

    private fun parseToken(body: String?): String {
        try {
            JSONObject.fromObject(body!!).apply {
                if (has("tenant_access_token")) {
                    return getString("tenant_access_token")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun parseImageKey(body: String?): String {
        try {
            JSONObject.fromObject(body!!).apply {
                if (has("data")) {
                    val data = getJSONObject("data")
                    if (data.has("image_key"))
                        return data.getString("image_key")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun requestToken(): Response {
        return OkHttpClient().newCall(Request.Builder().apply {
            this.url(AccessTokenUrl)
            this.addHeader("Content-Type", "application/json; charset=utf-8")
            this.post(JSONObject().apply {
                put("app_id", AppId)
                put("app_secret", AppSecret)
            }.toString().toRequestBody(null))
        }.build()).execute()
    }

    private fun imageToBytes(imagePath: String): ByteArray? {
        return ImageUtils.image2Bytes(imagePath)

    }

    /**
    ---7MA4YWxkTrZu0gW
    Content-Disposition: form-data; name="image_type";

    message
    ---7MA4YWxkTrZu0gW
    Content-Disposition: form-data; name="image";
    Content-Type: application/octet-stream

    二进流
    ---7MA4YWxkTrZu0gW
     */
    private fun uploadImage(imageBytes: ByteArray?, authorization: String): Response {
        return OkHttpClient().newCall(Request.Builder().apply {
            this.url(ImageUploadUrl)
            this.addHeader("Authorization", "Bearer $authorization")
            this.addHeader("Content-Type", "multipart/form-data; boundary=$Boundary")
            val data = buildBodyArray()
            val endData = ("\r\n--$Boundary--\r\n").toByteArray()
            this.post((data + imageBytes!! + endData).toRequestBody())
        }.build()).execute()

    }

    private fun buildBodyArray(): ByteArray {
        return ("--$Boundary" +
                "\r\n" +
                "Content-Disposition: form-data; name=\"image_type\"" +
                "\r\n\r\n" +
                "message" +
                "\r\n" +
                "--$Boundary" +
                "\r\n" +
                "Content-Disposition: form-data; name=\"image\"" +
                "\r\n" +
                "Content-Type: application/octet-stream\r\n\r\n").toByteArray()
    }


    private fun buildRequest(jsonBody: String) {
        Thread(Runnable {
            OkHttpClient().newCall(Request.Builder().apply {
                this.url(MessageCommonUrl)
                this.addHeader("Content-Type", "application/json")
                this.post(jsonBody.toRequestBody(null))
            }.build()).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    System.out.println("$TAG onFailure:${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    System.out.println("$TAG onResponse code:${response.code}")
                }
            })
        }).start()
    }

}