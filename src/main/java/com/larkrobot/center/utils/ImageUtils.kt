package com.larkrobot.center.utils

import java.io.File
import java.io.FileInputStream
import java.lang.Exception

/**
 *  Created by wuxiaofeng on 2021/6/10.
 */
object ImageUtils {

    fun image2Bytes(imagePath: String): ByteArray? {
        var fin: FileInputStream? = null
        var bytes: ByteArray? = null
        try {
            fin = FileInputStream(File(imagePath))
            bytes = ByteArray(fin.available())
            fin.read(bytes)
            fin.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fin?.close()
        }
        return bytes
    }

}