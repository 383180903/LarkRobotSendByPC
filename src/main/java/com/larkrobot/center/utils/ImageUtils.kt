package com.larkrobot.center.utils

import org.omg.PortableInterceptor.INACTIVE
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.ImageInputStream

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

    @JvmStatic
    fun cropImage(x: Int, y: Int, cropWidth: Int, cropHeight: Int, src: String, dst: String) {
        var fileInputStream: FileInputStream? = null
        var imageInputStream: ImageInputStream? = null
        try {
            val file = File(src)
            fileInputStream = FileInputStream(src)
            val decoder = ImageIO.getImageReadersByFormatName(FileUtils.getFileType(file));
            val imageReader: ImageReader = decoder.next()
            imageInputStream = ImageIO.createImageInputStream(fileInputStream)
            imageReader.setInput(imageInputStream, true)
            val params = imageReader.defaultReadParam
            val rect = Rectangle(x, y, cropWidth, cropHeight)
            params.sourceRegion = rect
            val bufferImage: BufferedImage = imageReader.read(0, params)
            ImageIO.write(bufferImage, FileUtils.getFileType(file), File(dst))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
            imageInputStream?.close()
        }
    }

    @JvmStatic
    fun getImageSize(file: File?): Pair<Int, Int>? {
        var fileInputStream: FileInputStream? = null
        try {
            if (file?.exists() == true) {
                fileInputStream = FileInputStream(file)
                val imageSource = ImageIO.read(fileInputStream)
                return Pair(imageSource.width, imageSource.height)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }
        return null
    }

}