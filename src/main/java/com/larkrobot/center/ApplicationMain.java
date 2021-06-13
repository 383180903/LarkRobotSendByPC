package com.larkrobot.center;

import com.larkrobot.center.view.MainViewDesigner;

import javax.swing.*;
import java.awt.*;

public class ApplicationMain {

    private static int windowWidth =  500;
    private static int windowHeight =  150;

    public static void main(String[] args){
        JFrame container = new JFrame("飞书机器人控制中心");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        container.setLocation((screenSize.width - windowWidth) / 2, (screenSize.height - windowHeight) / 2);
        container.setSize(windowWidth,windowHeight);
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        container.setResizable(false);
        container.setContentPane((new MainViewDesigner(container)).panelMain);
        container.setVisible(true);
        System.out.println("创建成功");
    }
}
