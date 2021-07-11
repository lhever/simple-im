package com.lhever.common.core.support.initializer.service;

import com.lhever.common.core.config.OrderedPropertiesReader;
import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.initializer.CryptoKeyProvider;
import com.lhever.common.core.support.initializer.ctx.WindowsContext;
import com.lhever.common.core.support.initializer.wrapper.StringBuilderWrapper;
import com.lhever.common.core.utils.*;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.*;

/**
 * <p>
 * 服务包装的工具类。
 * 这里的服务包装是指通过wrapper.exe将jar包装为windows服务，这样就可以通过系统服务的方式启动、停止、重启该jar
 * </p>
 *
 * @author hehaoneng 2018/12/24 14:47
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2018/12/24 14:47
 * @modify by reason:{方法名}:{原因}
 */
public class WindowsInitService extends BaseInitService {

    /**
     * wrapper.xml文件名
     */
    public static final String WRAPPER_FILE_NAME = "wrapper.xml";

    /**
     * wrapper.xml模版
     */
    private static final String WRAPPER_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<configuration>\n" +
                    "  <id>{serviceId}</id>\n" +
                    "  <name>{service}-{port}</name>\n" +
                    "  <description>{port}端口启动的{service}应用</description>\n" +
                    "  <executable>java</executable>\n" +
                    "  <arguments>-Xms512M -Xmx1024M -Xss1024k -XX:MaxNewSize=768M -XX:PermSize=512M -XX:MaxPermSize=512M -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath={dump-path} -jar {jar-file}</arguments>\n" +
                    "  <startmode>Automatic</startmode>\n" +
                    "</configuration>";


    public void init(Class clazz, String[] args, CryptoKeyProvider provider) {
        this.provider = provider;

        if (TRUE_STRING.equals(System.getProperty("init"))) {

            //添加回调，退出时候启动服务
            Runtime.getRuntime().addShutdownHook(new Thread(() -> registerAndRun()));

            //创建上下文，上下文的部分参数需要等待配置文件生成后才能加载
            ctx = new WindowsContext(clazz, args);

            //动态生成yml配置文件，该方法依赖上下文对象ctx提供的部分参数,尽管上下文还没有完全初始化
            generateConf();

            //配置文件生成后，再从配置文件获取端口值，完成上下文的完全初始化
            ctx.setPort(getPort());

            println("the ctx is: \n\t", JsonUtils.object2Json(ctx, true));

            //动态生成wrapper.xml
            generateWrapperXml(WRAPPER_FILE_NAME);


            modifyLogXml();

            genRunBat();

            System.exit(0);
        }
    }




    private void generateWrapperXml(String outputFilename) {
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("serviceId", ctx.getServiceId());
            put("service", ctx.getService());
            put("port", ctx.getPort());
            put("jar-file", ctx.getFullJarName());
            put("dump-path", ctx.getRoot());
        }};

        /*String[] fields = new String[]{"serviceId", "service", "port", "fullJarName:jar-file"};
        Map<String, Object> map = Bean2MapUtil.toMap(ctx, fields);*/

        //根据模版组装xml
        String xml = ParseUtils.parseMap(WRAPPER_TEMPLATE, map);
        String wrapperXmlName = ctx.getRoot() + File.separator + outputFilename;
        //xml写入文件
        FileUtils.writeToFileByNio(wrapperXmlName, xml);
    }

    public void modifyLogXml() {
        try {
            String modifylog = System.getProperty("modifylog");
            //如果不允许修改logbakc配置文件，直接返回
            if (StringUtils.isNotBlank(modifylog) && modifylog.equalsIgnoreCase(FALSE_STRING)) {
                return;
            }
            doModifyLogXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doModifyLogXml() throws Exception {
        String logPATH = ctx.getRoot() + File.separator + RESOURCES_DIR
                + File.separator + "logback.xml";

        File logFile = new File(logPATH);
        if (!logFile.exists() || !logFile.isFile()) {
            println("log file logback-spring.xml does not exists");
            return;
        }

        Document document = getLogDoc(logPATH);
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        if (CollectionUtils.isEmpty(elements)) {
            return;
        }

        boolean hasPort = false;
        Element logDirEle = null;
        for (Element element : elements) {

            removeConsoleAppender(element);

            //已经存在端口参数，矫正并返回
            if (isPortElement(element)) {
                modify(element, "property", "name", "port", "value", "" + ctx.getPort());
                hasPort = true;
            } else if//已经存在端口参数，但端口参数来自spring boot配置文件，修改，使其不依赖于spring配置文件
            (isSpringPortElement(element)) {
                modify(element, "property", "name", "port", "value", "" + ctx.getPort());
                hasPort = true;
            }

            if (isLogDirElement(element)) {
                logDirEle = element;
            }
        }

        if (isModfifyLogDir()) {
            changeLogDir(logDirEle, hasPort);
        }


        //将修改过的xml文档写入文件
        rewriteLogXml(document, logPATH);
    }


    private static boolean isPortElement(Element element) {
        Set<String> nameValus = new HashSet<>();
        nameValus.add("name=port");
        return testElement(element, "property", nameValus);
    }

    private static boolean isSpringPortElement(Element element) {
        Set<String> nameValus = new HashSet<>();
        nameValus.add("name=port");
        nameValus.add("source=server.port");
        return testElement(element, "springProperty", nameValus);
    }

    private static boolean isLogDirElement(Element element) {
        Set<String> nameValus = new HashSet<>();
        nameValus.add("name=logHome");
        return testElement(element, "property", nameValus);
    }


    private void changeLogDir(Element element, boolean hasPort) {
        if (element == null) {
            return;
        }
        //拼接日志文件名
        StringBuilder builder = new StringBuilder();
        builder.append("../logs/")
                .append(ctx.getSimpleServiceName())
                .append(CommonConsts.UNDER_SCORE);
        if (hasPort) {
            builder.append("${port}");
        } else {
            builder.append(ctx.getPort());
        }

        modify(element, "property",
                "name", "logHome",
                "value", "./log" /*builder.toString()*/);
    }

    private static void removeConsoleAppender(Element parent) {
        if (parent == null) {
            return;
        }

        Set<String> nameValus = new HashSet<>();
        nameValus.add("ref=console");

        if (parent.getName().equals("logger") || parent.getName().equals("root")) {
            parent.addAttribute("level", "info");
            List<Element> children = parent.elements();
            List<Element> selected = getByNameValue(children, "appender-ref", nameValus);
            remove(parent, selected);
        }
    }




    private void generateConf() {
        try {
            String use2Conf = System.getProperty("useCustom2Conf");
            /**
              如果该配置项为true， 则使用约定的双配置文件：
                 一个配置文件名为bootstrap.yml   (对应的模板是bootstrap-tpl.yml),
               另一个配置文件名为application.yml（对应的模板是application-tpl.yml）
             */
            if (TRUE_STRING.equalsIgnoreCase(use2Conf)) {
                doGenerateCustom2Conf();
            } else {
                doGenerateConf();
            }
        } catch (Exception e) {
            println("generate conf file error", e);
        }

    }

    private Properties crypto(Properties properties) {
        String crypto = System.getProperty("crypto");
        //如果不需要加密，直接返回
        if (StringUtils.isNotBlank(crypto) && crypto.equalsIgnoreCase(FALSE_STRING)) {
            return properties;
        }

        //生成盐值
        String salt = properties.getProperty(SALT);
        if (StringUtils.isNotBlank(salt)) {
            salt = salt.trim();
        } else {
            salt = StringUtils.getUuid();
        }
        properties.put(SALT, salt);

        if (provider == null || CollectionUtils.isEmpty(provider.croptoKeys())) {
            return properties;
        }

        List<String> keys = provider.croptoKeys();

        for (String ck : keys) {
            String key = ck;
            //不包含指定的key，跳过
            if (!properties.containsKey(key)) {
                continue;
            }
            //对应的值为空，跳过
            if (StringUtils.isBlank(properties.getProperty(key))) {
                continue;
            }
            String pwd = properties.getProperty(key);
            pwd = pwd.trim();

            //已经加过密，跳过
            if (JasyptUtils.isEncryptedValue(pwd)) {
                continue;
            }
            String encrypted = JasyptUtils.encryptAndWrap(salt, pwd);
            //将值更新为加密过了的
            properties.put(key, encrypted);
        }

        return properties;
    }


    /**
     * 动态生成配置文件
     *
     * @param
     * @return
     * @author lihong10 2018/12/29 12:02
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/29 12:02
     * @modify by reason:{原因}
     */
    private void doGenerateConf() {

        //读取模版文件名 template file
        String templateFileName = RuntimeUtils.getSystemProperty("tf");// template file

        //读取属性文件名  values file
        String propFileName = RuntimeUtils.getSystemProperty("vf");// values file
        //生成的配置文件名  configuration file
        String confFileName = RuntimeUtils.getSystemProperty("cf");//  configuration file
        //任意一个文件名为空，返回
        if (StringUtils.isAnyBlank(templateFileName, propFileName, confFileName)) {
            return;
        }
        templateFileName = trimFileName(templateFileName);
        propFileName = trimFileName(propFileName);
        confFileName = trimFileName(confFileName);

        //得到模版文件完整路径
        String tplPath = ctx.getRoot() + File.separator + templateFileName;
        //得到属性文件完整路径
        String propPath = ctx.getRoot() + File.separator + propFileName;
        //得到配置文件完整路径
        String confPath = ctx.getRoot() + File.separator + RESOURCES_DIR + File.separator + confFileName;
        //获取模版完整内容
        String template = FileUtils.readByPath(tplPath);

        //模版内容为空，返回
        if (StringUtils.isBlank(template)) {
            println("template file: " + template + "is empty !");
            return;
        }

        //属性文件转map
        OrderedPropertiesReader propertiesFile = new OrderedPropertiesReader(propPath, true);
        Properties props = propertiesFile.getProps();
        //对密码进行统一加密
        crypto(props);
        modifyHost(props);
        //覆盖values文件中的内容，覆盖后的文件，里面得密码全部是加密过了的
        boolean success = propertiesFile.writeNoEscape(propPath);
        if (!success) {
            println("rewrite values file failed!!!");
        }

        Map<String, Object> map = toMap(propertiesFile.getProps());
        //替换属性文件值到模版
        //替换 $[{xxx]形式的占位符，避免与 ${xxx}形式占位符相冲突
        String finalTemplate = ParseUtils.parseMap("$[", "]", template, map);

        FileUtils.writeToFileByNio(confPath, finalTemplate);
    }


    private void doGenerateCustom2Conf() {
        //读取属性文件名  values file
        String propFileName = RuntimeUtils.getSystemProperty("vf");// values file
        if (StringUtils.isBlank(propFileName)) {
            println("values file not defined !");
            return;
        }
        propFileName = trimFileName(propFileName);

        //得到属性文件完整路径
        String propPath = ctx.getRoot() + File.separator + propFileName;


        //bootstrap.yml的模本路径
        String bootstrapTplPath = ctx.getRoot() + File.separator + "bootstrap-tpl.yml";
        //application.yml的模本路径
        String applicationTplPath = ctx.getRoot() + File.separator + "application-tpl.yml";

        //bootstrap.yml配置文件路径
        String bootstrapConfPath = ctx.getRoot() + File.separator + RESOURCES_DIR + File.separator + "bootstrap.yml";
        //application.yml配置文件路径
        String applicationConfPath = ctx.getRoot() + File.separator + RESOURCES_DIR + File.separator + "application.yml";

        if(!new File(bootstrapTplPath).exists()) {
            throw new RuntimeException("bootstrap-tpl.yml not exists");
        }

        if(!new File(applicationTplPath).exists()) {
            throw new RuntimeException("application-tpl.yml not exists");
        }

        //属性文件转map
        OrderedPropertiesReader propertiesFile = new OrderedPropertiesReader(propPath, true);
        Properties props = propertiesFile.getProps();
        //对密码进行统一加密
        crypto(props);
        modifyHost(props);
        //覆盖values文件中的内容，覆盖后的文件，里面得密码全部是加密过了的
        boolean success = propertiesFile.writeNoEscape(propPath);
        if (!success) {
            println("rewrite values file failed!!!");
        }

        Map<String, Object> map = toMap(propertiesFile.getProps());
        //获取模版完整内容
        String bootstrapTemplate = FileUtils.readByPath(bootstrapTplPath);
        if (StringUtils.isNotBlank(bootstrapTemplate)) {
            //替换属性文件值到模版
            //替换 $[{xxx]形式的占位符，避免与 ${xxx}形式占位符相冲突
            String finalTemplate = ParseUtils.parseMap("$[", "]", bootstrapTemplate, map);
            FileUtils.writeToFileByNio(bootstrapConfPath, finalTemplate);

        } else {
            println(bootstrapTplPath + " is empty !");
        }

        //获取模版完整内容
        String applicationTemplate = FileUtils.readByPath(applicationTplPath);
        if (StringUtils.isNotBlank(applicationTemplate)) {
            //替换属性文件值到模版
            //替换 $[{xxx]形式的占位符，避免与 ${xxx}形式占位符相冲突
            String finalTemplate = ParseUtils.parseMap("$[", "]", applicationTemplate, map);
            FileUtils.writeToFileByNio(applicationConfPath, finalTemplate);
        } else {
            println(applicationTplPath + " is empty !");
        }
    }





    public void registerAndRun() {
        //拼接bat脚本
        StringBuilderWrapper stWrapper = new StringBuilderWrapper(new StringBuilder());
        stWrapper.appendln("@echo off")
                .append("cd /d ").appendln(ctx.getRoot())
                .appendln("echo stop service...")
                .append("net stop ").appendln(ctx.getServiceId()) //停止服务
                .appendln("echo uninstall service...")
                .append("sc delete ").appendln(ctx.getServiceId()) //卸载同名服务
//                .append("wrapper.exe uninstall ") //卸载同名服务
                .appendln("echo install service...")
                .appendln("wrapper.exe install ") //注册服务
                .appendln("ping -n 3 127.0.0.1>nul ") //达到停顿片刻的效果
                .appendln("echo start service...")
                .append("net start ").appendln(ctx.getServiceId())
                .appendln("ping -n 25 127.0.0.1>nul ")
                .append("exit ");

        String fileName = "deploy.bat";
        String fileFullName = ctx.getRoot() + File.separator + fileName;

        //脚本内容写入文件
        FileUtils.writeToFileByNio(fileFullName, stWrapper.toString(), CommonConsts.CHARSET_UTF8);

        //运行脚本
        int status = RuntimeUtils.execQuitely("cmd.exe /C start " + fileFullName);
        if (status == 0) {
            println("服务" + ctx.getServiceId() + "启动成功");
        } else {
            println("服务" + ctx.getServiceId() + "启动失败");
        }

    }

    public void genRunBat() {
        //拼接bat脚本
        StringBuilderWrapper stWrapper = new StringBuilderWrapper(new StringBuilder());
        stWrapper.appendln("@echo off")
                .append("set SERVICE_NAME=").appendln(ctx.getServiceId())
                .appendln("cd /d %~dp0")
                .appendln("for /F %%v in ('echo %1^|findstr \"^start$ ^stop$ ^restart$ ^install$ ^uninstall$ ^install_start$\"') do set COMMAND=%%v")
                .appendln("if \"%COMMAND%\" == \"start\" (")
                .appendln("    net start %SERVICE_NAME%")
                .appendln(") else if \"%COMMAND%\" == \"stop\" (")
                .appendln("    net stop %SERVICE_NAME%")
                .appendln(") else if \"%COMMAND%\" == \"restart\" (")
                .appendln("    net stop %SERVICE_NAME%")
                .appendln("    net start %SERVICE_NAME%")
                .appendln(") else if \"%COMMAND%\" == \"install\" (")
                .appendln("    wrapper.exe install ")
                .appendln(") else if \"%COMMAND%\" == \"uninstall\" (")
                .appendln("    net stop %SERVICE_NAME%")
                .appendln("    sc delete %SERVICE_NAME%")
                .appendln(") else if \"%COMMAND%\" == \"install_start\" (")
                .appendln("    wrapper.exe install ")
                .appendln("    net start %SERVICE_NAME%")
                .appendln(")else (")
                .appendln("    echo Usage: %0 { start : stop : restart : install : uninstall : install_start }")
                .append(")");

        String fileName = "run.bat";
        String fileFullName = ctx.getRoot() + File.separator + fileName;

        //脚本内容写入文件
        FileUtils.writeToFileByNio(fileFullName, stWrapper.toString(), CommonConsts.CHARSET_UTF8);
    }




}
