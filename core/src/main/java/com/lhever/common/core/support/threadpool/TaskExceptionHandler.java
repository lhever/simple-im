
package com.lhever.common.core.support.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author lihong 2016年4月23日 下午2:55:48
 * @version v2.0
 */
public class TaskExceptionHandler implements UncaughtExceptionHandler {
    private static Logger log = LoggerFactory.getLogger(TaskRejectedHandler.class);

    /**
     * @see UncaughtExceptionHandler#uncaughtException(Thread, Throwable)
     * @since v2.0
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("提交到线程池的任务发生异常，异常线程是：" + t.toString() + "<--> 原因是： " + e);
    }

}
