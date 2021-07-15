package com.larkrobot.center.view;

import com.larkrobot.center.helper.*;
import com.larkrobot.center.model.DayTime;
import com.larkrobot.center.model.TimeBean;
import com.larkrobot.center.utils.FileUtils;

import javax.swing.*;
import java.io.File;

public class MainViewDesigner {
    public JPanel panelMain;
    private JButton 发送文本Button;
    private JTextField 文本框;
    private JButton 选择图片文件Button;
    private JRadioButton 是否开启菜单推送RadioButton;
    private JRadioButton 是否开启天气推送RadioButton;
    private JRadioButton 是否开启新闻推送RadioButton;
    private JRadioButton 是否开启股市信息推送RadioButton;
    private JFrame container;
    private Boolean isOpenMenuPush = false;
    private Boolean isOpenWeatherPush = false;
    private Boolean isOpenNewsPush = false;
    private Boolean isOpenSharesPush = false;

    private final TimeBean morningMenuTime = new TimeBean(8, 30, 0);
    private final TimeBean noonMenuTime = new TimeBean(11, 50, 0);
    private final TimeBean dinnerMenuTime = new TimeBean(18, 25, 0);

    private final TimeBean weatherTime = new TimeBean(20, 0, 0);
    private final TimeBean newsTime = new TimeBean(10, 0, 0);

    private final TimeBean morningSharesTime = new TimeBean(9, 31, 0);
    private final TimeBean noonSharesTime = new TimeBean(15, 1, 0);

    public MainViewDesigner(JFrame container) {

        this.container = container;
        发送文本Button.addActionListener(e -> LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildNormalText(文本框.getText())));
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
                TimerSendHelper.startTimer(morningMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Morning.getValue()));
                TimerSendHelper.startTimer(noonMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Noon.getValue()));
                TimerSendHelper.startTimer(dinnerMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Dinner.getValue()));
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(morningMenuTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(noonMenuTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(dinnerMenuTime));
            }
        });
        是否开启天气推送RadioButton.addActionListener(e -> {
            isOpenWeatherPush = 是否开启天气推送RadioButton.isSelected();
            System.out.println("isOpenWeatherPush - " + isOpenWeatherPush);
            if (isOpenWeatherPush) {
                TimerSendHelper.startTimer(weatherTime, dayTime -> WeatherFetchHelper.requestWeather());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(weatherTime));
            }
        });
        是否开启新闻推送RadioButton.addActionListener(e -> {
            isOpenNewsPush = 是否开启新闻推送RadioButton.isSelected();
            System.out.println("isOpenNewsPush - " + isOpenNewsPush);
            if (isOpenNewsPush) {
                TimerSendHelper.startTimer(newsTime, dayTime -> NewsFetchHelper.requestNews());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(newsTime));
            }
        });
        是否开启股市信息推送RadioButton.addActionListener(e -> {
            isOpenSharesPush = 是否开启股市信息推送RadioButton.isSelected();
            System.out.println("isOpenSharesPush - " + isOpenSharesPush);
            if (isOpenSharesPush) {
                TimerSendHelper.startTimer(morningSharesTime, dayTime -> SharesFetchHelper.requestShares());
                TimerSendHelper.startTimer(noonSharesTime, dayTime -> SharesFetchHelper.requestShares());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(morningSharesTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(noonSharesTime));
            }
        });
    }
}
