package com.lhever.simpleim.router;

import com.lhever.simpleim.router.cfg.DataSourceConfig;
import com.lhever.simpleim.router.pojo.User;
import com.lhever.simpleim.router.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Wang pengfei
 * @date 2019/8/23 23:28
 * @ClassName: Application
 * @Description:
 */


@ComponentScan(basePackages = {"com.lhever.simpleim.router"})
public class Application {

    /**
     * 注意四点
     * ①Application使用了@ComponentScan
     * ②Application这个类的位置是不是和spring boot的启动类位置很相似
     * ③这里ComponentScan默认basePackages是com.wpf
     * ④如果你的启动类放在其他包下面记得@ComponentScan里你要写你要扫描的包路径
     * 不然@ComponentScan默认是到自己类路径的。
     * 可以见com.wpf.app.Main
     */

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = null;
        try {
            ctx = new AnnotationConfigApplicationContext(Application.class, DataSourceConfig.class);//初始化IOC容器
            UserService userService = ctx.getBean(UserService.class);//通过IOC容器获得你要执行的业务代码的类
            User user = userService.findById();//通过IOC容器获取到的类执行你的业务代码

            System.out.println(user);
        } finally {
            if (ctx != null){
                ctx.close();
                System.out.println("普通java程序执行完成,IOC容器关闭。。。");
            }
        }
    }
}
