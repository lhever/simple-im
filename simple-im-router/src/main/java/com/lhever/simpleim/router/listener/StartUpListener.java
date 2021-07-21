package com.lhever.simpleim.router.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class StartUpListener implements ApplicationListener<ApplicationContextEvent> {


    public void onApplicationEvent(ApplicationContextEvent event) {
        System.out.println("系统启动：" + event.getClass().getName());

    }
}
