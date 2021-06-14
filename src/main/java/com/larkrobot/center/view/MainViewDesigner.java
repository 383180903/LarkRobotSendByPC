package com.larkrobot.center.view;

import com.larkrobot.center.helper.LarkRobotSendHelper;
import com.larkrobot.center.helper.MenuCropHelper;
import com.larkrobot.center.helper.TimerSendHelper;
import com.larkrobot.center.helper.WeatherFetchHelper;
import com.larkrobot.center.model.DayTime;
import com.larkrobot.center.utils.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainViewDesigner {
    public JPanel panelMain;
    private JButton 发送文本Button;
    private JTextField 文本框;
    private JButton 选择图片文件Button;
    private JRadioButton 是否开启菜单推送RadioButton;
    private JRadioButton 是否开启天气推送RadioButton;
    private JFrame container;
    private Boolean isOpenMenuPush = false;
    private Boolean isOpenWeatherPush = false;

    public MainViewDesigner(JFrame container) {

        this.container = container;
        发送文本Button.addActionListener(e -> LarkRobotSendHelper.sendTextMessage(文本框.getText()));
        选择图片文件Button.addActionListener(e -> {
            File file = FileUtils.pickLocalFile(container);
            if (FileUtils.isImageFile(file)) {
                LarkRobotSendHelper.sendImageMessage(file.getAbsolutePath());
            }
        });
        是否开启菜单推送RadioButton.addActionListener(e -> {
            isOpenMenuPush = 是否开启菜单推送RadioButton.isSelected();
            System.out.println("isOpenMenuPush - " + isOpenMenuPush);
            if (isOpenMenuPush) {
                TimerSendHelper.startTimer(8, 30, 0, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Morning.getValue()));
                TimerSendHelper.startTimer(11, 55, 0, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Noon.getValue()));
                TimerSendHelper.startTimer(18, 25, 0, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Dinner.getValue()));
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(8, 30, 0));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(11, 55, 0));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(18, 25, 0));
            }
        });
        是否开启天气推送RadioButton.addActionListener(e -> {
            isOpenWeatherPush = 是否开启天气推送RadioButton.isSelected();
            System.out.println("isOpenWeatherPush - " + isOpenWeatherPush);
            if (isOpenWeatherPush) {
                TimerSendHelper.startTimer(10, 0, 0, dayTime -> WeatherFetchHelper.requestWeather());
                TimerSendHelper.startTimer(20, 0, 0, dayTime -> WeatherFetchHelper.requestWeather());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(10, 0, 0));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(20, 0, 0));
            }
        });
    }
}
