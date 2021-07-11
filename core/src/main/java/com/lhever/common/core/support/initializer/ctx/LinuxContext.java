package com.lhever.common.core.support.initializer.ctx;

import com.lhever.common.core.utils.FileUtils;

import java.io.File;

import static com.lhever.common.core.support.initializer.service.BaseInitService.println;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/5/15 12:24
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/5/15 12:24
 * @modify by reason:{方法名}:{原因}
 */
public class LinuxContext extends InitContext {

    public LinuxContext(Class clazz, String[] args) {
        super(clazz, args);

        //classPath的值为：“ file:/home/hik/tpe/tpe-1.0.0-SNAPSHOT.jar!/BOOT-INF/classes!/”
        String classPath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
        println("boot class path: ", classPath);

        //path的值为：/home/hik/tpe/file:/home/hik/tpe/
        String path = new File(classPath).getParentFile().getParentFile().getParentFile().getAbsolutePath();
        String dic = "file:";
        int index = path.lastIndexOf(dic);
        if (index > 0) {
            //path的值为：/home/hik/tpe/
            path = path.substring(0, index);
            println("path is: " + path);
            path = FileUtils.trimTail(path);
            println("trimed path is: " + path);
        }
        this.root = path;

        File jarFile = new File(classPath).getParentFile().getParentFile();
        String jarName = jarFile.getName();
        //这里是去掉末尾的！
        jarName = jarName.replace("!", "");
        this.jarName = jarName;
        this.fullJarName = root + File.separator + jarName;


        int i = clazz.getSimpleName().indexOf(APPLICATION_SUFFIX);
        if (i > 0) {
            this.service = camelToLinethrough(clazz.getSimpleName().substring(0, i));
            this.simpleServiceName = parseSimpleServiceName(this.service);
        } else {
            this.service = camelToLinethrough(clazz.getSimpleName().toLowerCase());
            this.simpleServiceName = parseSimpleServiceName(this.service);
        }

    }

















}
