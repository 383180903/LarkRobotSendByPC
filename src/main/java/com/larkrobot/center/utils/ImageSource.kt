package com.larkrobot.center.utils

import java.awt.Image
import javax.swing.ImageIcon

object ImageSource {
    var APP_ICON: Image? = null

    init {
        val fileURL = ImageSource::class.java.getResource("/main_icon.png")
        APP_ICON = ImageIcon(fileURL).image
    }
}