package com.lhever.common.core.support.threadpool;

import com.lhever.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The Class MyThreadPoolExecutorService.
 *
 * @author lihong 2016年4月22日 上午10:54:38
 * @version v2.0
 */
public class SimpleThreadPoolService implements ThreadPoolService {

    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(SimpleThreadPoolService.class);

    /**
     * 核心线程数目.
     */
    private int corePoolSize;

    /**
     * 最大线程数目.
     */
    private int maxPoolSize;

    /**
     * 超出核心线程数的线程数目的活跃时间.
     */
    private long keepAliveTime;


    /**
     * 任务队列大小.
     */
    private int queueCapacity;

    /**
     * 线程池使用的队列类型，目前有数组实现的有界阻塞队列，同步队列，和链表实现的无界阻塞队列三种.
     */
    private int queueType;

    /**
     * 是否启动监听器的标志字段.
     */
    private volatile boolean needStartMonitor = false;

    /**
     * 如需启动监听，该字段判断监听器是否已经启动.
     */
    private volatile boolean monitorStarted = false;

    /**
     * 任务被抛弃后的处理策略.
     */
    private TaskRejectedHandler taskRejectedHandler;

    /**
     * 线程池.
     */
    private ThreadPoolExecutor executor;

    private String threadNamePrefix;

    /**
     * The lock.
     */
    private Object lock = new Object();

    private volatile ThreadPoolMonitor threadPoolMonitor;


    @Override
    public void createThreadPool() {
        /*
         * 线程池最好保持全局唯一，没有则重新创建
         */
        if (executor != null) {
            return;
        } else {
            synchronized (lock) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(getCorePoolSize(), getMaxPoolSize(),
                            getKeepAliveTime() * ONE_MINUTE_IN_SECONDS, TimeUnit.SECONDS,
                            getQueue(getQueueType()),
                            new ThreadFactory() {
                                final AtomicInteger threadNumber = new AtomicInteger(1);

                                @Override
                                public Thread newThread(Runnable r) {
                                    String threadName = (StringUtils.isBlank(threadNamePrefix) ? "thread-pool-task-" : (threadNamePrefix.trim() + "-")) + threadNumber.getAndIncrement();
                                    Thread t;
                                    t = new Thread(Thread.currentThread().getThreadGroup(), r, threadName);
                                    t.setUncaughtExceptionHandler(new TaskExceptionHandler());
                                    return t;
                                }

                            }, getTaskRejectedHandler());
//					this.executor.allowCoreThreadTimeOut(true);

                }
            }

        }


    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }
    @Override
    public int getCorePoolSize() {
        return corePoolSize;
    }


    @Override
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }


    @Override
    public int getMaxPoolSize() {
        return maxPoolSize;
    }


    @Override
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }


    @Override
    public long getKeepAliveTime() {
        return keepAliveTime;
    }


    @Override
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;

    }


    @Override
    public int getQueueCapacity() {
        return queueCapacity;
    }


    @Override
    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }


    @Override
    public TaskRejectedHandler getTaskRejectedHandler() {
        return taskRejectedHandler;
    }


    @Override
    public void setTaskRejectedHandler(TaskRejectedHandler RejectedExecutionHandler) {
        taskRejectedHandler = RejectedExecutionHandler;
    }


    public int getQueueType() {
        return queueType;
    }

    /**
     * @param queueType void
     * @author lihong 2016年4月24日 上午9:57:03
     * @since v2.0
     */
    public void setQueueType(int queueType) {
        this.queueType = queueType;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    /**
     * @param type
     * @return BlockingQueue<Runnable>
     * @author lihong 2016年4月24日 上午9:57:10
     * @since v2.0
     */
    private BlockingQueue<Runnable> getQueue(int type) {

        if (!QueueType.contains(type)) {
            throw new IllegalArgumentException("线程池配置错误， 指定的队列类型不合法");
        }

        log.info("线程池采用的阻塞队列类型是：" + QueueType.getType(type));

        if (type == QueueType.ARRAY_BLOCKING_QUEUE.value()) {
            return new ArrayBlockingQueue<Runnable>(getQueueCapacity());
        } else if (type == QueueType.SYNCHRONOUS_QUEUE.value()) {
            return new SynchronousQueue<Runnable>();
        } else if (type == QueueType.LINKED_BLOCKING_QUEUE.value()) {
            return new LinkedBlockingQueue<Runnable>();
        }
        return null;
    }


    /**
     * Sets the need start monitor.
     *
     * @param needStartMonitor the new need start monitor
     */
    @Override
    public void setNeedStartMonitor(boolean needStartMonitor) {
        this.needStartMonitor = needStartMonitor;

    }

    @Override
    public boolean getNeedStartMonitor() {
        return needStartMonitor;
    }


    /**
     * @return boolean
     * @author lihong 2016年4月24日 上午9:57:27
     * @since v2.0
     */
    public boolean getMonitorStarted() {
        return monitorStarted;
    }


    /**
     * @param monitorStarted void
     * @author lihong 2016年4月24日 上午10:18:53
     * @since v2.0
     */
    public void setMonitorStarted(boolean monitorStarted) {
        this.monitorStarted = monitorStarted;
    }

    /**
     * @return the threadPoolMonitorService
     */
    public ThreadPoolMonitor getThreadPoolMonitor() {
        return threadPoolMonitor;
    }

    public void setThreadPoolMonitor(ThreadPoolMonitor threadPoolMonitor) {
        this.threadPoolMonitor = threadPoolMonitor;
    }

    /**
     * 提交任务并执行
     *
     * @param runnable void
     * @author lihong 2016年4月22日 下午5:17:21
     * @since v2.0
     */
    @Override
    public void submitAndExecute(Runnable runnable) {
        if (executor == null) {
            init();
        }
        executor.execute(runnable);
        log.info("a task has been submitted");
//		this.executor.submit(runnable);
    }


    public <T> Future<T> submitAndExecute(Callable<T> task) {
        if (executor == null) {
            init();
        }
        Future<T> submit = executor.submit(task);
        return submit;
    }


    @Override
    public void startMonitor() {
        /**
         * 如果需要启动监听线程
         */
        if (getNeedStartMonitor() == true) {

            /**
             * 但监听线程还没有启动
             */
            if (threadPoolMonitor != null && monitorStarted == false) {

//				this.threadPoolMonitorService = new ThreadPoolMonitorService();
                threadPoolMonitor.setExecutor(executor);
                Thread monitor = new Thread(threadPoolMonitor, "lhever-thread-pool-monitor");
                /**
                 * 设为守护线程
                 */
                monitor.setDaemon(true);
                monitor.start();
                this.monitorStarted = true;
            }

        }

    }

    @Override
    public void init() {
        createThreadPool();
        startMonitor();
    }


}
