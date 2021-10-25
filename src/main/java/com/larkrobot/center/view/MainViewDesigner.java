package com.larkrobot.center.view;

import com.larkrobot.center.helper.*;
import com.larkrobot.center.model.DayTime;
import com.larkrobot.center.model.TimeBean;
import com.larkrobot.center.utils.FileUtils;

import javax.swing.*;
import java.io.File;

public class MainViewDesigner {
    public JPanel panelMain;
    private JButton sendTextButton;
    private JTextField textArea;
    private JButton sendPictureButton;
    private JRadioButton openMenuPushRadioButton;
    private JRadioButton openWeatherPushRadioButton;
    private JRadioButton openNewsPushRadioButton;
    private JRadioButton openGushiPushRadioButton;
    private JFrame container;
    private Boolean isOpenMenuPush = false;
    private Boolean isOpenWeatherPush = false;
    private Boolean isOpenNewsPush = false;
    private Boolean isOpenSharesPush = false;

    private final TimeBean morningMenuTime = new TimeBean(8, 30, 0);
    private final TimeBean noonMenuTime = new TimeBean(11, 0, 0);
    private final TimeBean noonSetOffTime = new TimeBean(11, 53, 0);
    private final TimeBean dinnerMenuTime = new TimeBean(18, 0, 0);
    private final TimeBean dinnerSetOffTime = new TimeBean(18, 23, 0);

    private final TimeBean weatherTime = new TimeBean(20, 0, 0);
    private final TimeBean newsTime = new TimeBean(10, 0, 0);

    private final TimeBean morningSharesTime = new TimeBean(9, 31, 0);
    private final TimeBean noonSharesTime = new TimeBean(15, 1, 0);

    public MainViewDesigner(JFrame container) {

        this.container = container;
        sendTextButton.addActionListener(e -> LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildNormalText(textArea.getText())));
        sendPictureButton.addActionListener(e -> {
            File file = FileUtils.pickLocalFile(container);
            if (FileUtils.isImageFile(file)) {
                LarkRobotSendHelper.sendImageMessage(file.getAbsolutePath());
            }
        });
        openMenuPushRadioButton.addActionListener(e -> {
            isOpenMenuPush = openMenuPushRadioButton.isSelected();
            System.out.println("isOpenMenuPush - " + isOpenMenuPush);
            if (isOpenMenuPush) {
                TimerSendHelper.startTimer(morningMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Morning.getValue()));
                TimerSendHelper.startTimer(noonMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Noon.getValue()));
                TimerSendHelper.startTimer(dinnerMenuTime, dayTime -> MenuCropHelper.cropMenuAndSend(DayTime.Dinner.getValue()));
                TimerSendHelper.startTimer(noonSetOffTime, dayTime -> LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildNormalText("尚能饭否，go go go")));
                TimerSendHelper.startTimer(dinnerSetOffTime, dayTime -> LarkRobotSendHelper.sendTextMessage(TextMessageBuilder.buildNormalText("尚能饭否，go go go")));
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(morningMenuTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(noonMenuTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(dinnerMenuTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(noonSetOffTime));
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(dinnerSetOffTime));
            }
        });
        openWeatherPushRadioButton.addActionListener(e -> {
            isOpenWeatherPush = openWeatherPushRadioButton.isSelected();
            System.out.println("isOpenWeatherPush - " + isOpenWeatherPush);
            if (isOpenWeatherPush) {
                TimerSendHelper.startTimer(weatherTime, dayTime -> WeatherFetchHelper.requestWeather());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(weatherTime));
            }
        });
        openNewsPushRadioButton.addActionListener(e -> {
            isOpenNewsPush = openNewsPushRadioButton.isSelected();
            System.out.println("isOpenNewsPush - " + isOpenNewsPush);
            if (isOpenNewsPush) {
                TimerSendHelper.startTimer(newsTime, dayTime -> NewsFetchHelper.requestNews());
            } else {
                TimerSendHelper.endTimer(TimerSendHelper.buildTimerKey(newsTime));
            }
        });
        openGushiPushRadioButton.addActionListener(e -> {
            isOpenSharesPush = openGushiPushRadioButton.isSelected();
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
