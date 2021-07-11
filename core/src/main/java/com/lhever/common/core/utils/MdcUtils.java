package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.concurrent.*;

/**
 * <p>
 * Mapped Diagnostic Context，映射调试上下文工具类
 * </p>
 *
 * @author guoliang5 2018/12/24 20:16
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2018/12/4 20:16
 * @modify by reason:{方法名}:{原因}
 */
public class MdcUtils {


    public static String generateRequestId() {
        return StringUtils.getUuid() + CommonConsts.UNDER_SCORE + System.currentTimeMillis();
    }


    /**
     * 方法的功能说明: 往MDC中设置key为requestId的键值对
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void setRequestIdIfAbsent() {
        if (get(CommonConsts.REQUEST_ID) == null) {
            put(CommonConsts.REQUEST_ID, generateRequestId());
        }
    }

    /**
     * 方法的功能说明: 往MDC中设置key为requestId的键值对
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void setRequestIdIfAbsent(String requestId) {
        if (get(CommonConsts.REQUEST_ID) == null) {
            put(CommonConsts.REQUEST_ID, requestId);
        }
    }

    /**
     * 方法的功能说明: 往MDC中设置key为requestId的键值对
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void setRequestId() {
        put(CommonConsts.REQUEST_ID, generateRequestId());
    }

    /**
     * 方法的功能说明: 从MDC中查询key为requestId的值
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static String getRequestId() {
        return get(CommonConsts.REQUEST_ID);
    }

    /**
     * 方法的功能说明: 根据key从MDC中查询值
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static String get(String key) {
        return MDC.get(key);
    }


    /**
     * 方法的功能说明
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void setRequestId(String traceId) {
        put(CommonConsts.REQUEST_ID, traceId);
    }

    /**
     * 方法的功能说明: 插入key, value到键值对中
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 方法的功能说明，从MDC中移除key为requestId的值
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void removeRequestId() {
        remove(CommonConsts.REQUEST_ID);
    }

    /**
     * 方法的功能说明，根据key从MDC中移除值
     *
     * @param
     * @return
     * @author lihong10 2018/12/17 12:07
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/17 12:07
     * @modify by reason:{原因}
     */
    public static void remove(String key) {
        MDC.remove(key);
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setRequestIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setRequestIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    public static class ThreadPoolTaskExecutorMdcWrapper extends ThreadPoolTaskExecutor {
        @Override
        public void execute(Runnable task) {
            super.execute(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public void execute(Runnable task, long startTimeout) {
            super.execute(MdcUtils.wrap(task, MDC.getCopyOfContextMap()), startTimeout);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public ListenableFuture<?> submitListenable(Runnable task) {
            return super.submitListenable(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
            return super.submitListenable(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }
    }

    public static class ThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {
        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                            RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public void execute(Runnable task) {
            super.execute(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> Future<T> submit(Runnable task, T result) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()), result);
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }
    }

    public static class ForkJoinPoolMdcWrapper extends ForkJoinPool {
        public ForkJoinPoolMdcWrapper() {
            super();
        }

        public ForkJoinPoolMdcWrapper(int parallelism) {
            super(parallelism);
        }

        public ForkJoinPoolMdcWrapper(int parallelism, ForkJoinWorkerThreadFactory factory,
                                      Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
            super(parallelism, factory, handler, asyncMode);
        }

        @Override
        public void execute(Runnable task) {
            super.execute(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }

        @Override
        public <T> ForkJoinTask<T> submit(Runnable task, T result) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()), result);
        }

        @Override
        public <T> ForkJoinTask<T> submit(Callable<T> task) {
            return super.submit(MdcUtils.wrap(task, MDC.getCopyOfContextMap()));
        }
    }
}
