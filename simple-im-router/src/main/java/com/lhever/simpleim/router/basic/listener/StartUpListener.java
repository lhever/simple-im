package com.lhever.simpleim.router.basic.listener;

import com.lhever.common.core.utils.ContextUtils;
import com.lhever.simpleim.router.basic.http.HttpRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

@Component
public class StartUpListener implements ApplicationListener<ApplicationContextEvent> {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);


    public void onApplicationEvent(ApplicationContextEvent event) {
        logger.info("系统启动:{}", event.getClass().getName());
        ApplicationContext ctx = event.getApplicationContext();
        ContextUtils.setApplicationContext(ctx);

    }


}
