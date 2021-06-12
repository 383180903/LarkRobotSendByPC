package com.larkrobot.center.view

import com.larkrobot.center.helper.LarkRobotSendHelper
import java.awt.Color
import java.awt.GridLayout
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class MainWindow : WindowListener {

    companion object {
        const val windowWidth = 300
        const val windowHeight = 300
    }

    fun create() {
        JFrame("飞书机器人控制中心").apply {
            setLocation((1920 - windowWidth) / 2, (1080 - windowHeight) / 2)
            setSize(windowWidth, windowHeight)
            background = Color.DARK_GRAY
            isResizable = true
            isVisible = true
            contentPane = JPanel(GridLayout(2, 4)).apply {
                background = Color.WHITE
                add(JButton("发送文本消息").apply {
                    setSize(100,50)
                    addActionListener{
                        LarkRobotSendHelper.sendTextMessage("from pc" + System.currentTimeMillis())
                    }
                })
            }
        }
    }

    override fun windowDeiconified(e: WindowEvent?) {

    }

    override fun windowClosing(e: WindowEvent?) {
        System.exit(0)
    }

    override fun windowClosed(e: WindowEvent?) {
        System.exit(0)
    }

    override fun windowActivated(e: WindowEvent?) {

    }

    override fun windowDeactivated(e: WindowEvent?) {

    }

    override fun windowOpened(e: WindowEvent?) {

    }

    override fun windowIconified(e: WindowEvent?) {

    }

}