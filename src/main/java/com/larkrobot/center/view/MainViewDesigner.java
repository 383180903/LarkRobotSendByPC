package com.larkrobot.center.view;

import com.larkrobot.center.helper.LarkRobotSendHelper;
import com.larkrobot.center.helper.TimerSendHelper;
import com.larkrobot.center.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainViewDesigner {
    public JPanel panelMain;
    private JButton 发送文本Button;
    private JLabel 输入文本;
    private JTextField textField1;
    private JButton 选择图片文件Button;
    private JRadioButton 是否开启菜单推送RadioButton;
    private JFrame container;
    private Boolean isOpenMenuPush = false;

    public MainViewDesigner(JFrame container) {
        this.container = container;
        发送文本Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LarkRobotSendHelper.sendTextMessage(输入文本.getText());
            }
        });
        选择图片文件Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = FileUtils.pickLocalFile(container);
                if (FileUtils.isImageFile(file)) {
                    System.out.println("filePath - " + file.getAbsolutePath());
                    LarkRobotSendHelper.sendImageMessage(file.getAbsolutePath());
                }
            }
        });
        是否开启菜单推送RadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOpenMenuPush = 是否开启菜单推送RadioButton.isSelected();
                System.out.println("isOpenMenuPush - " + isOpenMenuPush);
                if (isOpenMenuPush) {
                    TimerSendHelper.startTimer();
                } else {
                    TimerSendHelper.endTimer();
                }
            }
        });
    }
}
