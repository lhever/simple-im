package com.lhever.common.core.test;

import com.lhever.common.core.support.initializer.wrapper.StringBuilderWrapper;
import org.junit.Test;

public class ServiceWrapperTest {

    @Test
    public void testGenDeployBat() {
        StringBuilderWrapper stWrapper = new StringBuilderWrapper(new StringBuilder());
        stWrapper.appendln("@echo off")
                .append("cd /d ").appendln("xxxxx")
                .appendln("echo stop service...")
                .append("net stop ").appendln("xxxxx") //停止服务
                .appendln("echo uninstall service...")
                .append("sc delete ").appendln("xxxxx") //卸载同名服务
//                .append("wrapper.exe uninstall ") //卸载同名服务
                .appendln("echo install service...")
                .appendln("wrapper.exe install ") //注册服务
                .appendln("ping -n 3 127.0.0.1>nul ") //达到停顿片刻的效果
                .appendln("echo start service...")
                .append("net start ").appendln("xxxxx")
                .append("pause ");

        System.out.println(stWrapper.toString());


    }


    @Test
    public void testGenRunBat() {
        //拼接bat脚本
        StringBuilderWrapper stWrapper = new StringBuilderWrapper(new StringBuilder());
        stWrapper.appendln("@echo off")
                .append("set SERVICE_NAME=").appendln("xxxxxxxxxxxxxxxxx")
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

        System.out.println(stWrapper.toString());


    }
}
