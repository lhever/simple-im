package com.lhever.common.core.support.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 自定义线程池接口.
 *
 * @author lihong 2016年4月22日 上午10:34:34
 * @version v2.0
 */
public interface ThreadPoolService {

    /**
     * 一分钟，以秒为单位.
     */
    public static final long ONE_MINUTE_IN_SECONDS = 60;

    /**
     * 创建线程池
     */
    public void createThreadPool();

    /**
     * Gets the core pool size.
     *
     * @return the core pool size
     */
    public int getCorePoolSize();

    /**
     * Sets the core pool size.
     *
     * @param corePoolSize the new core pool size
     */
    public void setCorePoolSize(int corePoolSize);

    /**
     * Gets the max pool size.
     *
     * @return the max pool size
     */
    public int getMaxPoolSize();

    /**
     * Sets the max pool size.
     *
     * @param maxPoolSize the new max pool size
     */
    public void setMaxPoolSize(int maxPoolSize);

    /**
     * 以秒为单位
     * Gets the keep alive time.
     *
     * @return the keep alive time
     */
    public long getKeepAliveTime();

    /**
     * 以秒为单位
     * Sets the keep alive time.
     *
     * @param keepAliveTime the new keep alive time
     */
    public void setKeepAliveTime(long keepAliveTime);

    /**
     * Gets the queue capacity.
     *
     * @return the queue capacity
     */
    public int getQueueCapacity();

    /**
     * Sets the queue capacity.
     *
     * @param queueCapacity the new queue capacity
     */
    public void setQueueCapacity(int queueCapacity);

    /**
     * Gets the test rejected execution handler.
     *
     * @return the test rejected execution handler
     */
    public TaskRejectedHandler getTaskRejectedHandler();

    /**
     * Sets the test rejected execution handler.
     *
     * @param myRejectedExecutionHandler the new my rejected execution handler
     */
    public void setTaskRejectedHandler(TaskRejectedHandler myRejectedExecutionHandler);


    /**
     * 获取线程池队列类型.
     *
     * @return int
     * @author lihong 2016年4月22日 上午11:40:02
     * @since v2.0
     */
    public int getQueueType();

    /**
     * 设置是否启动监听线程.
     *
     * @param isStartMonitor void
     * @author lihong 2016年4月22日 下午3:00:55
     * @since v2.0
     */
    public void setNeedStartMonitor(boolean isStartMonitor);

    /**
     * 获取是否启动了监听线程.
     *
     * @return boolean
     * @author lihong 2016年4月22日 下午3:02:05
     * @since v2.0
     */
    public boolean getNeedStartMonitor();


    /**
     * Gets the thread pool monitor service.
     *
     * @return the thread pool monitor service
     */
    public ThreadPoolMonitor getThreadPoolMonitor();


    /**
     * Sets the thread pool monitor service.
     *
     * @param threadPoolMonitorService the new thread pool monitor service
     */
    public void setThreadPoolMonitor(ThreadPoolMonitor threadPoolMonitorService);


    /**
     * 提交任务并执行
     *
     * @param runnable void
     * @author lihong 2016年4月22日 下午5:18:41
     * @since v2.0
     */
    public void submitAndExecute(Runnable runnable);


    public <T> Future<T> submitAndExecute(Callable<T> task);

    /**
     * 启动监听线程
     *
     * @author lihong 2016年4月23日 上午9:28:41
     * @since v2.0
     */
    public void startMonitor();

    /**
     * 初始化生成线程池的对象
     *
     * @author lihong 2016年4月23日 上午9:38:46
     * @since v2.0
     */
    public void init();


}
