package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2018/12/26 14:01
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2018/12/26 14:01
 * @modify by reason:{方法名}:{原因}
 */
public class RuntimeUtils {

    private static final Logger log = LoggerFactory.getLogger(Sha256Utils.class);


    /**
     * 调用操作系统命令，该方法会抛出异常。返回的int值表示执行状态
     *
     * @param scripts 操作系统命令
     * @return
     * @author lihong10 2018/12/26 14:20
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 14:20
     * @modify by reason:{原因}
     */
    public static int exec(String scripts) throws IOException, InterruptedException {
        //执行脚本
        Runtime rt = Runtime.getRuntime();
        Process ps = rt.exec(scripts);
        int status = ps.waitFor();
        return status;
    }


    /**
     * 调用操作系统命令，该方法不会抛出任何异常。
     *
     * @param scripts 操作系统命令
     * @return
     * @author lihong10 2018/12/26 14:20
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 14:20
     * @modify by reason:{原因}
     */
    public static int execQuitely(String scripts) {
        try {
            return exec(scripts);
        } catch (IOException | InterruptedException e) {
            log.error("error when exec:\n\t" + scripts, e);
        }
        return -1;
    }

    /**
     * 调用操作系统命令，该方法不会抛出任何异常。
     *
     * @param scripts 操作系统命令
     * @return
     * @author lihong10 2018/12/26 14:20
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 14:20
     * @modify by reason:{原因}
     */
    public static String execWithConsoleOutputQuitely(String scripts) {
        try {
            return execWithConsoleOutput(scripts);
        } catch (IOException e) {
            log.error("error when exec:\n\t" + scripts, e);
        }
        return "";
    }

    /**
     * 调用操作系统命令，该方法会抛出异常。返回命令执行后在控制台显示的字符串
     *
     * @param scripts 操作系统命令
     * @return
     * @author lihong10 2018/12/26 14:20
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 14:20
     * @modify by reason:{原因}
     */
    public static String execWithConsoleOutput(String scripts) throws IOException {
        //执行脚本
        Runtime rt = Runtime.getRuntime();
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        Process process = null;
        try {
            process = rt.exec(scripts);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
        } finally {
            IOUtils.closeQuietly(br);
            if (process != null) {
                try {
                    process.destroyForcibly();
                } catch (Exception e) {
                    log.error("destroy subprocess error", e);
                }
            }
        }

        return builder.toString();
    }


    /**
     * 根据key获取启动程序时候传入的参数值
     *
     * @param key
     * @return
     * @author lihong10 2018/12/26 14:21
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 14:21
     * @modify by reason:{原因}
     */
    public static String getSystemProperty(String key) {
        return System.getProperty(key);
    }


}
