package com.lhever.common.core.support.leak;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/7/31 9:21
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/7/31 9:21
 * @modify by reason:{方法名}:{原因}
 */

public interface ResourceLeakTracker<T>  {

    /**
     * Records the caller's current stack trace so that the {@link ResourceLeakDetector} can tell where the leaked
     * resource was accessed lastly. This method is a shortcut to {@link #record(Object) record(null)}.
     */
    void record();

    /**
     * Records the caller's current stack trace and the specified additional arbitrary information
     * so that the {@link ResourceLeakDetector} can tell where the leaked resource was accessed lastly.
     */
    void record(Object hint);

    /**
     * Close the leak so that {@link ResourceLeakTracker} does not warn about leaked resources.
     * After this method is called a leak associated with this ResourceLeakTracker should not be reported.
     *
     * @return {@code true} if called first time, {@code false} if called already
     */
    boolean close(T trackedObject);
}

