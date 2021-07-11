package com.lhever.common.core.support.concurrent.mtpattern.tss;

import java.lang.ref.WeakReference;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * 持统一清理不再被使用的ThreadLocal变量的ThreadLocal子类。
 *
 * @param <T> 相应的线程特有对象类型
 * @author lihong10 2019/4/11 15:58
 * @return
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/11 15:58
 * @modify by reason:{原因}
 */
public class ManagedThreadLocal<T> extends ThreadLocal<T> {

    /*
     * 使用弱引用，防止内存泄漏。
     */
    private static final Queue<WeakReference<ManagedThreadLocal<?>>> instances =
            new ConcurrentLinkedQueue<WeakReference<ManagedThreadLocal<?>>>();

    /*
     * 使用volatile修饰保证内存可见性。
     */
    private volatile ThreadLocal<T> threadLocal;

    private ManagedThreadLocal(final Supplier<T> supplier) {

        this.threadLocal = new ThreadLocal<T>() {

            @Override
            protected T initialValue() {
                return supplier.get();
            }

        };
    }

    public static <T> ManagedThreadLocal<T> newInstance(final Supplier<T> supplier) {
        ManagedThreadLocal<T> mtl = new ManagedThreadLocal<T>(supplier);

        // 使用弱引用来引用ThreadLocalProxy实例，防止内存泄漏。
        instances.add(new WeakReference<ManagedThreadLocal<?>>(mtl));
        return mtl;
    }

    public T get() {
        return threadLocal.get();
    }

    public void set(T value) {
        threadLocal.set(value);
    }

    public void remove() {
        if (null != threadLocal) {
            threadLocal.remove();
            threadLocal = null;
        }
    }


    /**
     * 清理该类所管理的所有ThreadLocal实例。
     *
     * @param
     * @return
     * @author lihong10 2019/4/11 16:08
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 16:08
     * @modify by reason:{原因}
     */
    public static void removeAll() {
        WeakReference<ManagedThreadLocal<?>> wrMtl;
        ManagedThreadLocal<?> mtl;
        while (null != (wrMtl = instances.poll())) {
            mtl = wrMtl.get();
            if (null != mtl) {
                mtl.remove();
            }
        }
    }


}
