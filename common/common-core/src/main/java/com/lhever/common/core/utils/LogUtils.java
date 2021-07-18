package com.lhever.common.core.utils;


import com.lhever.common.core.support.log.Slf4jProxyLogger;
import org.slf4j.Marker;

import java.util.Date;

/**
 * <p>日志记录工具类，有了该类：不用在每个需要记录日志的类中添加类似下面的这句话了：<p/>
 *
 * <p><code>private static final Logger logger = LoggerFactory.getLogger(XXX.class);</code></p>
 *
 * <p>该类性能不高，因为每次记录日志都需要动态获取方法调用栈, 然后推断类名，
 * 然后再根据类名查找日志对象， 最后再使用日志对象记录日志，性能较差，仅推荐在单元测试类中使用<p/>
 *
 * @author lihong10 2019/4/12 14:08
 * @return
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/12 14:08
 * @modify by reason:{原因}
 */
public class LogUtils {


    public static String getName() {
        return Slf4jProxyLogger.INSTANCE.getName();
    }

    public static boolean isTraceEnabled() {
        return Slf4jProxyLogger.INSTANCE.isTraceEnabled();
    }


    public static void debug(String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.debug(format, arguments);
    }

    public static void info(String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.info(format, arguments);
    }


    public static void warn(String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.warn(format, arguments);
    }

    public static void trace(String msg) {
        Slf4jProxyLogger.INSTANCE.trace(msg);
    }

    public static void trace(String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.trace(format, arg);

    }

    public static void trace(String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.trace(format, arg1, arg2);

    }

    public static void trace(String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.trace(format, arguments);
    }

    public static void trace(String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.trace(msg, t);
    }

    public static boolean isTraceEnabled(Marker marker) {
        return Slf4jProxyLogger.INSTANCE.isTraceEnabled();
    }

    public static void trace(Marker marker, String msg) {
        Slf4jProxyLogger.INSTANCE.trace(marker, msg);

    }

    public static void trace(Marker marker, String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.trace(marker, format, arg);
    }

    public static void trace(Marker marker, String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.trace(marker, format, arg1, arg2);

    }

    public static void trace(Marker marker, String format, Object... argArray) {
        Slf4jProxyLogger.INSTANCE.trace(marker, format, argArray);
    }

    public static void trace(Marker marker, String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.trace(marker, msg, t);
    }

    public static boolean isDebugEnabled() {
        return Slf4jProxyLogger.INSTANCE.isDebugEnabled();
    }

    public static void debug(String msg) {
        Slf4jProxyLogger.INSTANCE.debug(msg);
    }

    public static void debug(String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.debug(format, arg);
    }

    public static void debug(String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.debug(format, arg1, arg2);
    }

    public static void debug(String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.debug(msg, t);
    }

    public static boolean isDebugEnabled(Marker marker) {
        return Slf4jProxyLogger.INSTANCE.isDebugEnabled(marker);
    }

    public static void debug(Marker marker, String msg) {
        Slf4jProxyLogger.INSTANCE.debug(marker, msg);
    }

    public static void debug(Marker marker, String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.debug(marker, format, arg);
    }

    public static void debug(Marker marker, String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.debug(marker, format, arg1, arg2);
    }

    public static void debug(Marker marker, String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.debug(marker, format, arguments);
    }

    public static void debug(Marker marker, String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.debug(marker, msg, t);
    }

    public static boolean isInfoEnabled() {
        return Slf4jProxyLogger.INSTANCE.isInfoEnabled();
    }

    public static void info(String msg) {
        Slf4jProxyLogger.INSTANCE.info(msg);
    }

    public static void info(String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.info(format, arg);
    }

    public static void info(String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.info(format, arg1, arg2);
    }

    public static void info(String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.info(msg, t);
    }

    public static boolean isInfoEnabled(Marker marker) {
        return Slf4jProxyLogger.INSTANCE.isInfoEnabled();
    }

    public static void info(Marker marker, String msg) {
        Slf4jProxyLogger.INSTANCE.info(marker, msg);
    }

    public static void info(Marker marker, String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.info(marker, format, arg);
    }

    public static void info(Marker marker, String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.info(marker, format, arg1, arg2);
    }

    public static void info(Marker marker, String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.info(marker, format, arguments);
    }

    public static void info(Marker marker, String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.info(marker, msg, t);
    }

    public static boolean isWarnEnabled() {
        return Slf4jProxyLogger.INSTANCE.isWarnEnabled();
    }

    public static void warn(String msg) {
        Slf4jProxyLogger.INSTANCE.warn(msg);
    }

    public static void warn(String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.warn(format, arg);
    }

    public static void warn(String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.warn(format, arg1, arg2);
    }

    public static void warn(String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.warn(msg, t);
    }

    public static boolean isWarnEnabled(Marker marker) {
        return Slf4jProxyLogger.INSTANCE.isWarnEnabled(marker);
    }

    public static void warn(Marker marker, String msg) {
        Slf4jProxyLogger.INSTANCE.warn(marker, msg);
    }

    public static void warn(Marker marker, String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.warn(marker, format, arg);
    }

    public static void warn(Marker marker, String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.warn(marker, format, arg1, arg2);
    }

    public static void warn(Marker marker, String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.warn(marker, format, arguments);
    }

    public static void warn(Marker marker, String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.warn(marker, msg, t);
    }

    public static boolean isErrorEnabled() {
        return Slf4jProxyLogger.INSTANCE.isErrorEnabled();
    }

    public static void error(String msg) {
        Slf4jProxyLogger.INSTANCE.error(msg);
    }

    public static void error(String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.error(format, arg);
    }

    public static void error(String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.error(format, arg1, arg2);
    }

    public static void error(String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.error(format, arguments);
    }

    public static void error(String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.error(msg, t);
    }

    public static boolean isErrorEnabled(Marker marker) {
        return Slf4jProxyLogger.INSTANCE.isErrorEnabled();
    }

    public static void error(Marker marker, String msg) {
        Slf4jProxyLogger.INSTANCE.error(marker, msg);

    }

    public static void error(Marker marker, String format, Object arg) {
        Slf4jProxyLogger.INSTANCE.error(marker, format, arg);
    }

    public static void error(Marker marker, String format, Object arg1, Object arg2) {
        Slf4jProxyLogger.INSTANCE.error(marker, format, arg1, arg2);
    }

    public static void error(Marker marker, String format, Object... arguments) {
        Slf4jProxyLogger.INSTANCE.error(marker, format, arguments);
    }

    public static void error(Marker marker, String msg, Throwable t) {
        Slf4jProxyLogger.INSTANCE.error(marker, msg, t);
    }

    public static String logRespMsg(String url, String params, String resp,long startTime) {
        StringBuilder builder = new StringBuilder();
        String lfcr = "\n\r";
        builder.append("hikyun url: ")
                .append(url)
                .append(lfcr)
                .append("with param: ")
                .append(params)
                .append(lfcr)
                .append("get response: ")
                .append(resp)
                .append(lfcr)
                .append("time: ")
                .append(new Date().getTime()-startTime).append(lfcr);
        return builder.toString();
    }
}
