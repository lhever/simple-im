package com.lhever.common.core.support.concurrent.mtpattern.ftl;

import com.lhever.common.core.support.logger.LogFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.BitSet;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/11/4 21:33
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/11/4 21:33
 * @modify by reason:{方法名}:{原因}
 */
public final class InternalThreadLocalMap extends UnpaddedInternalThreadLocalMap {

    private static final Logger logger = LogFactory.getLogger(InternalThreadLocalMap.class);

    private static final int INDEXED_VARIABLE_TABLE_INITIAL_SIZE = 32;

    public static final Object UNSET = new Object();

    private BitSet cleanerFlags;


    public static InternalThreadLocalMap getIfSet() {
        Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            return ((FastThreadLocalThread) thread).threadLocalMap();
        }
        return slowThreadLocalMap.get();
    }

    public static InternalThreadLocalMap get() {
        Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            return fastGet((FastThreadLocalThread) thread);
        } else {
            return slowGet();
        }
    }

    private static InternalThreadLocalMap fastGet(FastThreadLocalThread thread) {
        InternalThreadLocalMap threadLocalMap = thread.threadLocalMap();
        if (threadLocalMap == null) {
            thread.setThreadLocalMap(threadLocalMap = new InternalThreadLocalMap());
        }
        return threadLocalMap;
    }

    private static InternalThreadLocalMap slowGet() {
        ThreadLocal<InternalThreadLocalMap> slowThreadLocalMap = UnpaddedInternalThreadLocalMap.slowThreadLocalMap;
        InternalThreadLocalMap ret = slowThreadLocalMap.get();
        if (ret == null) {
            ret = new InternalThreadLocalMap();
            slowThreadLocalMap.set(ret);
        }
        return ret;
    }

    public static void remove() {
        Thread thread = Thread.currentThread();
        if (thread instanceof FastThreadLocalThread) {
            ((FastThreadLocalThread) thread).setThreadLocalMap(null);
        } else {
            slowThreadLocalMap.remove();
        }
    }

    public static void destroy() {
        slowThreadLocalMap.remove();
    }

    public static int nextVariableIndex() {
        int index = nextIndex.getAndIncrement();
        if (index < 0) {
            nextIndex.decrementAndGet();
            throw new IllegalStateException("too many thread-local indexed variables");
        }
        return index;
    }

    public static int lastVariableIndex() {
        return nextIndex.get() - 1;
    }

    // Cache line padding (must be public)
    // With CompressedOops enabled, an instance of this class should occupy at least 128 bytes.
    public long rp1, rp2, rp3, rp4, rp5, rp6, rp7, rp8, rp9;

    private InternalThreadLocalMap() {
        super(newIndexedVariableTable());
    }

    private static Object[] newIndexedVariableTable() {
        Object[] array = new Object[INDEXED_VARIABLE_TABLE_INITIAL_SIZE];
        Arrays.fill(array, UNSET);
        return array;
    }

    public int size() {
        int count = 0;

        if (futureListenerStackDepth != 0) {
            count ++;
        }

        if (random != null) {
            count ++;
        }

        for (Object o: indexedVariables) {
            if (o != UNSET) {
                count ++;
            }
        }

        // We should subtract 1 from the count because the first element in 'indexedVariables' is reserved
        // by 'FastThreadLocal' to keep the list of 'FastThreadLocal's to remove on 'FastThreadLocal.removeAll()'.
        return count - 1;
    }


    public int futureListenerStackDepth() {
        return futureListenerStackDepth;
    }

    public void setFutureListenerStackDepth(int futureListenerStackDepth) {
        this.futureListenerStackDepth = futureListenerStackDepth;
    }

    public ThreadLocalRandom random() {
        ThreadLocalRandom r = random;
        if (r == null) {
            random = r = new ThreadLocalRandom();
        }
        return r;
    }



    public Object indexedVariable(int index) {
        Object[] lookup = indexedVariables;
        return index < lookup.length? lookup[index] : UNSET;
    }

    /**
     * @return {@code true} if and only if a new thread-local variable has been created
     */
    public boolean setIndexedVariable(int index, Object value) {
        Object[] lookup = indexedVariables;
        if (index < lookup.length) {
            Object oldValue = lookup[index];
            lookup[index] = value;
            return oldValue == UNSET;
        } else {
            expandIndexedVariableTableAndSet(index, value);
            return true;
        }
    }

    private void expandIndexedVariableTableAndSet(int index, Object value) {
        Object[] oldArray = indexedVariables;
        final int oldCapacity = oldArray.length;
        int newCapacity = index;
        newCapacity |= newCapacity >>>  1;
        newCapacity |= newCapacity >>>  2;
        newCapacity |= newCapacity >>>  4;
        newCapacity |= newCapacity >>>  8;
        newCapacity |= newCapacity >>> 16;
        newCapacity ++;

        Object[] newArray = Arrays.copyOf(oldArray, newCapacity);
        Arrays.fill(newArray, oldCapacity, newArray.length, UNSET);
        newArray[index] = value;
        indexedVariables = newArray;
    }

    public Object removeIndexedVariable(int index) {
        Object[] lookup = indexedVariables;
        if (index < lookup.length) {
            Object v = lookup[index];
            lookup[index] = UNSET;
            return v;
        } else {
            return UNSET;
        }
    }

    public boolean isIndexedVariableSet(int index) {
        Object[] lookup = indexedVariables;
        return index < lookup.length && lookup[index] != UNSET;
    }

    public boolean isCleanerFlagSet(int index) {
        return cleanerFlags != null && cleanerFlags.get(index);
    }

    public void setCleanerFlag(int index) {
        if (cleanerFlags == null) {
            cleanerFlags = new BitSet();
        }
        cleanerFlags.set(index);
    }
}

