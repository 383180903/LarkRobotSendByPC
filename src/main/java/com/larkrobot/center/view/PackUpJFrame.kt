package com.larkrobot.center.view

import java.awt.AWTException
import java.awt.Frame
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame


class PackUpJFrame(name: String) : JFrame(name) {

    private var trayIcon //托盘图标 new
            : TrayIcon? = null
    private var systemTray //系统托盘  //
            : SystemTray? = null

    init {
        SystemTray.isSupported()
        systemTray = SystemTray.getSystemTray()
        try {

            trayIcon = TrayIcon(ImageIO.read(File("src/main/resources/images/main_icon.png")))
            systemTray?.add(trayIcon) //设置托盘的图标，0.gif与该类文件同一目录
        } catch (e1: IOException) {
            e1.printStackTrace()
        } catch (e2: AWTException) {
            e2.printStackTrace()
        }
        addWindowListener(
                object : WindowAdapter() {
                    override fun windowIconified(e: WindowEvent?) {
                        dispose() //窗口最小化时dispose该窗口
                    }
                })

        trayIcon!!.addMouseListener(
                object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        if (e.clickCount == 2) //双击托盘窗口再现
                            extendedState = Frame.NORMAL
                        isVisible = true
                    }
                })

    }
}