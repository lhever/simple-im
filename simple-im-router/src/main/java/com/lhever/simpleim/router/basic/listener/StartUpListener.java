package com.lhever.simpleim.router.basic.listener;

import com.lhever.common.core.utils.ContextUtils;
import com.lhever.simpleim.router.basic.http.HttpRouter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartUpListener implements ApplicationListener<ApplicationContextEvent> {

 private    HttpRouter router = new HttpRouter();


    public void onApplicationEvent(ApplicationContextEvent event) {
        System.out.println("系统启动：" + event.getClass().getName());
        ApplicationContext ctx = event.getApplicationContext();
        ContextUtils.setApplicationContext(ctx);

        ControllerScanner controllerScanner = new ControllerScanner(ctx);
        List<Class<?>> classes = controllerScanner.loadControllerClass();


        router.loadControllerClass(ctx);



    }


}
