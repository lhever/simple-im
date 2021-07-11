package com.lhever.common.core.support.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略
 *
 * @author lihong 2016年4月22日 上午10:41:50
 * @version v2.0
 */
public class TaskRejectedHandler implements RejectedExecutionHandler {

    private static Logger log = LoggerFactory.getLogger(TaskRejectedHandler.class);


    /**
     * 线程池拒绝任务的处理方式：此处自定义为重新添加
     *
     * @see RejectedExecutionHandler#rejectedExecution(Runnable, ThreadPoolExecutor)
     * @since v2.0
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()) {
            return;
        }

        try {
            executor.getQueue().put(r);
            log.info("有任务被拒后又被重新加入线程池：该任务对象是： " + r);
        } catch (InterruptedException e) {
            log.error("TaskRejectedHandler error", e);
        }

    }


}
