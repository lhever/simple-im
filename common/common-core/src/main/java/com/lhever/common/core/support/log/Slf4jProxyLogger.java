package com.lhever.common.core.support.log;


import com.lhever.common.core.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Slf4jProxyLogger implements Logger {
    private static final Logger internalLog = LoggerFactory.getLogger(Slf4jProxyLogger.class.getName());

    public static final Slf4jProxyLogger INSTANCE = new Slf4jProxyLogger();

    private Slf4jProxyLogger() {

    }

    /**
     * 获取调用类类名, 假设通过LogUtil间接调用，所以栈深度为4
     *
     * @param
     * @return
     * @author lihong10 2019/4/12 12:38
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/12 12:38
     * @modify by reason:{原因}
     */
    public static String getCallerName() {
        StackTraceElement[] stacks = (new Throwable()).getStackTrace();
        int len = 0;
        //获取栈顶端的类名
        if (stacks != null && (len = stacks.length) > 0) {
            //其他类直接调用LogUtils，栈深度为4。 junit测试类,动态代理类调用LogUtils，栈深度 > 4
            if (len >= 5) {
                return stacks[4].getClassName();
            } else {
                return stacks[stacks.length - 1].getClassName();
            }
        }
        return LogUtils.getName();
    }

    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger(getCallerName());
        return logger;
    }


    @Override
    public String getName() {
        Logger logger = getLogger();
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        Logger logger = getLogger();
        return logger.isTraceEnabled();
    }

    @Override
    public void debug(String format, Object... arguments) {
        Logger logger = getLogger();
        logger.debug(format, arguments);
    }

    @Override
    public void info(String format, Object... arguments) {
        Logger logger = getLogger();
        logger.info(format, arguments);
    }

    @Override
    public void warn(String format, Object... arguments) {
        Logger logger = getLogger();
        logger.warn(format, arguments);
    }

    @Override
    public void trace(String msg) {
        Logger logger = getLogger();
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        Logger logger = getLogger();
        logger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        Logger logger = getLogger();
        logger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        Logger logger = getLogger();
        logger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        Logger logger = getLogger();
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        Logger logger = getLogger();
        logger.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        Logger logger = getLogger();
        logger.trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        Logger logger = getLogger();
        logger.trace(marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        Logger logger = getLogger();
        logger.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        Logger logger = getLogger();
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        Logger logger = getLogger();
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        Logger logger = getLogger();
        logger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String msg, Throwable t) {
        Logger logger = getLogger();
        logger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        Logger logger = getLogger();
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        Logger logger = getLogger();
        logger.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        Logger logger = getLogger();
        logger.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        Logger logger = getLogger();
        logger.debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        Logger logger = getLogger();
        logger.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        Logger logger = getLogger();
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        Logger logger = getLogger();
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        Logger logger = getLogger();
        logger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.info(format, arg1, arg2);
    }

    @Override
    public void info(String msg, Throwable t) {
        Logger logger = getLogger();
        logger.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        Logger logger = getLogger();
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        Logger logger = getLogger();
        logger.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        Logger logger = getLogger();
        logger.info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        Logger logger = getLogger();
        logger.info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        Logger logger = getLogger();
        logger.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        Logger logger = getLogger();
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        Logger logger = getLogger();
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        Logger logger = getLogger();
        logger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        Logger logger = getLogger();
        logger.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        Logger logger = getLogger();
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        Logger logger = getLogger();
        logger.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        Logger logger = getLogger();
        logger.warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        Logger logger = getLogger();
        logger.warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        Logger logger = getLogger();
        logger.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        Logger logger = getLogger();
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        Logger logger = getLogger();
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        Logger logger = getLogger();
        logger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        Logger logger = getLogger();
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        Logger logger = getLogger();
        logger.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        Logger logger = getLogger();
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        Logger logger = getLogger();
        logger.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        Logger logger = getLogger();
        logger.error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        Logger logger = getLogger();
        logger.error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        Logger logger = getLogger();
        logger.error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        Logger logger = getLogger();
        logger.error(marker, msg, t);
    }


}
