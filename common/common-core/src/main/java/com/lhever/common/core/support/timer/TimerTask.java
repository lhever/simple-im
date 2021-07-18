package com.lhever.common.core.support.timer;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/9/21 19:03
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/9/21 19:03
 * @modify by reason:{方法名}:{原因}
 */
public interface TimerTask {

    /**
     * Executed after the delay specified with
     * {@link Timer#newTimeout(TimerTask, long, TimeUnit)}.
     *
     * @param timeout a handle which is associated with this task
     */
    void run(Timeout timeout) throws Exception;

}
