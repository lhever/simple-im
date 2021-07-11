package com.lhever.common.core.support.concurrent.mtpattern.ftl;

import com.lhever.common.core.utils.ObjectUtils;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/11/4 21:38
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/11/4 21:38
 * @modify by reason:{方法名}:{原因}
 */

final class FastThreadLocalRunnable implements Runnable {
    private final Runnable runnable;

    private FastThreadLocalRunnable(Runnable runnable) {
        this.runnable = ObjectUtils.checkNotNull(runnable, "runnable");
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } finally {
            FastThreadLocal.removeAll();
        }
    }

    static Runnable wrap(Runnable runnable) {
        return runnable instanceof FastThreadLocalRunnable ? runnable : new FastThreadLocalRunnable(runnable);
    }
}

