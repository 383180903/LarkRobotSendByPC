package com.larkrobot.center.utils

import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import java.io.File


object OCRUtils {

    //eng ：英文  chi_sim ：简体中文
    const val DEFAULT_LANG = "chi_sim"
    const val DATA_PATH = "D:/LarkRobot/traindata"

    /**
     * 根据图片文件进行识别
     *
     * @param imageFile 图片文件
     * @param lang 指定语言库
     * @return 识别文本信息
     */
    @Throws(Exception::class)
    fun doOCRFromFile(imageFile: File?, lang: String?): String? {
        val instance: ITesseract = Tesseract()
        instance.setDatapath(DATA_PATH) //指定语言库目录
        instance.setTessVariable("user_defined_dpi", "300")
        instance.setTessVariable("tessedit_char_whitelist", "星期一二三四五六日天")
        instance.setLanguage(lang)
        return instance.doOCR(imageFile)
    }
}