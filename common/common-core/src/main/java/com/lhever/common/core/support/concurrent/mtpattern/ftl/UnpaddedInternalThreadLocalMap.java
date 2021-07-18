package com.lhever.common.core.support.concurrent.mtpattern.ftl;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/11/4 21:34
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/11/4 21:34
 * @modify by reason:{方法名}:{原因}
 */
class UnpaddedInternalThreadLocalMap {

    static final ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = new ThreadLocal<InternalThreadLocalMap>();
    static final AtomicInteger nextIndex = new AtomicInteger();

    /** Used by {@link FastThreadLocal} */
    Object[] indexedVariables;

    // Core thread-locals
    int futureListenerStackDepth;

    ThreadLocalRandom random;


    UnpaddedInternalThreadLocalMap(Object[] indexedVariables) {
        this.indexedVariables = indexedVariables;
    }
}

