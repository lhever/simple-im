package com.lhever.common.core.support.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定义监视线程池运行状况的接口
 *
 * @author lihong 2016年4月22日 下午2:51:03
 * @version v2.0
 */
public interface ThreadPoolMonitor extends Runnable {

    /**
     * 监控线程池方法
     */
    public void monitorThreadPool();

    /**
     * 设置需要被监视的线程池
     *
     * @param executor void
     * @author lihong 2016年4月22日 下午2:52:08
     * @since v2.0
     */
    public void setExecutor(ThreadPoolExecutor executor);

}
