package com.lhever.common.core.support.singleton;

import java.util.function.Supplier;

/**
 * 单例的构造类/工厂类，使用该类可以极其方便的构造单例对象
 *
 * @author lihong10 2019/1/22 10:04
 * @return
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/1/22 10:04
 * @modify by reason:{原因}
 */
public final class Singleton<R> implements Supplier<R> {

    private volatile boolean initialized = false;
    private volatile Supplier<R> instanceSupplier;

    /**
     * 通过函数式接口Supplier构造单例
     *
     * @param original
     * @return
     * @author lihong10 2019/1/22 10:20
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/22 10:20
     * @modify by reason:{原因}
     */
    public Singleton(final Supplier<R> original) {
        instanceSupplier = () -> {
            synchronized (original) {
                if (!initialized) {
                    final R singletonInstance = original.get();
                    instanceSupplier = () -> singletonInstance;
                    initialized = true;
                }
                return instanceSupplier.get();
            }
        };
    }

    /**
     * 返回单例对象
     *
     * @param
     * @return
     * @author lihong10 2019/1/22 10:21
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/22 10:21
     * @modify by reason:{原因}
     */
    @Override
    public R get() {
        return instanceSupplier.get();
    }
}
