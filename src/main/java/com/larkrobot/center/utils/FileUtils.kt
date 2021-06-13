package com.larkrobot.center.utils

import com.larkrobot.center.helper.LarkRobotSendHelper
import java.io.File
import java.lang.Exception
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel

object FileUtils {

    @JvmStatic
    fun pickLocalFile(container: JFrame): File? {
        val fileChooser = JFileChooser()
        fileChooser.showSaveDialog(container)
        try {
            val file = fileChooser.selectedFile
            if (file != null && file.exists()) {
                return file
            }
        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(JPanel(), "未选中文件", "Tips", JOptionPane.WARNING_MESSAGE)
        }
        return null
    }

    @JvmStatic
    fun isImageFile(file: File?): Boolean {
        if (file?.exists() == true) {
            val suffix = file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase()
            if (suffix == "jpg" || suffix == "png" || suffix == "gif") {
                return true
            } else {
                JOptionPane.showMessageDialog(JPanel(), "只能发送图片格式", "Tips", JOptionPane.WARNING_MESSAGE)
            }
        }
        return false
    }

    @JvmStatic
    fun getFileType(file: File?): String {
        if (file?.exists() == true) {
            return file.name.substring(file.name.lastIndexOf(".") + 1).toLowerCase()
        }
        return ""
    }
}