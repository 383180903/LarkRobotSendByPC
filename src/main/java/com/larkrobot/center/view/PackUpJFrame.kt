package com.larkrobot.center.view

import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.JFrame
import kotlin.system.exitProcess


class PackUpJFrame(name: String) : JFrame(name) {

    //托盘图标 new
    private var trayIcon: TrayIcon? = null
    //系统托盘
    private var systemTray: SystemTray? = null

    init {
        SystemTray.isSupported()
        systemTray = SystemTray.getSystemTray()
        try {
            trayIcon = TrayIcon(ImageIO.read(File("src/main/resources/images/main_icon.png")), "控制菜单", PopupMenu().apply {
                add(MenuItem("打开程序").apply {
                    addActionListener {
                        showMainWindow()
                    }
                })
                add(MenuItem("退出程序").apply {
                    addActionListener {
                        exitProcess(0)
                    }
                })
            })
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

        trayIcon?.addMouseListener(
                object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                        //双击托盘窗口再现
                        if (e.clickCount == 2) {
                            showMainWindow()
                        }
                    }
                })

    }

    private fun showMainWindow() {
        extendedState = Frame.NORMAL
        isVisible = true
        toFront()
    }
}