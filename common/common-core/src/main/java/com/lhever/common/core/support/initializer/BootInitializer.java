package com.lhever.common.core.support.initializer;

import com.lhever.common.core.support.initializer.service.LinuxInitService;
import com.lhever.common.core.support.initializer.service.WindowsInitService;

import static com.lhever.common.core.support.initializer.service.BaseInitService.println;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/5/15 14:41
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/5/15 14:41
 * @modify by reason:{方法名}:{原因}
 */
public class BootInitializer {

    public static void init(Class clazz, String[] args, CryptoKeyProvider provider) {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.indexOf("linux") >= 0) {
            println("current os is linux");
            new LinuxInitService().init(clazz, args, provider);


        } else if (os.indexOf("windows") >= 0) {
            println("current os is windows");
            new WindowsInitService().init(clazz, args, provider);

        } else {
            println("unknow os !!!, exit");
        }

    }




}


