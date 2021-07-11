package com.lhever.common.core.test;

import com.lhever.common.core.support.concurrent.mtpattern.ftl.FastThreadLocal;
import com.lhever.common.core.support.concurrent.mtpattern.ftl.FastThreadLocalThread;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class FastThreadLocalTest {

    /**
     * 测试ThreadLocal运行耗时
     *
     * @param threadLocalCount ThreadLocal的对象数量
     * @param runCount         调用get方法的次数
     * @param value            ThreadLocal的局部变量值
     * @return
     * @author lihong10 2019/2/25 19:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/25 19:19
     * @modify by reason:{原因}
     */
    public static void testThreadLocal(int threadLocalCount, int runCount, String value) {
        final ThreadLocal<String>[] caches = new ThreadLocal[threadLocalCount];
        final Thread mainThread = Thread.currentThread();
        for (int i = 0; i < threadLocalCount; i++) {
            caches[i] = new ThreadLocal();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < threadLocalCount; i++) {
                    caches[i].set(value);
                }
                long start = System.nanoTime();
                for (int i = 0; i < threadLocalCount; i++) {
                    for (int j = 0; j < runCount; j++) {
                        caches[i].get();
                    }
                }
                long end = System.nanoTime();
                System.out.println(" thread local take[" + TimeUnit.NANOSECONDS.toMillis(end - start) +
                        "]ms");
                LockSupport.unpark(mainThread);
            }

        });
        t.start();
        LockSupport.park(mainThread);
    }


    /**
     * 测试FastThreadLocal运行耗时
     *
     * @param threadLocalCount FastThreadLocal的对象数量
     * @param runCount         调用get方法的次数
     * @param value            FastThreadLocal的局部变量值
     * @return
     * @author lihong10 2019/2/25 19:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/25 19:19
     * @modify by reason:{原因}
     */
    public static void testFastThreadLocal(int threadLocalCount, int runCount, String value) {
        final FastThreadLocal<String>[] caches = new FastThreadLocal[threadLocalCount];
        final Thread mainThread = Thread.currentThread();
        for (int i = 0; i < threadLocalCount; i++) {
            caches[i] = new FastThreadLocal();
        }
        Thread t = new FastThreadLocalThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < threadLocalCount; i++) {
                    caches[i].set(value);
                }
                long start = System.nanoTime();
                for (int i = 0; i < threadLocalCount; i++) {
                    for (int j = 0; j < runCount; j++) {
                        caches[i].get();
                    }
                }
                long end = System.nanoTime();
                System.out.println("fast thread local take[" + TimeUnit.NANOSECONDS.toMillis(end - start) +
                        "]ms");
                LockSupport.unpark(mainThread);
            }

        });
        t.start();
        LockSupport.park(mainThread);
    }


    /**
     * 对比ThreadLocal和 FastThreadLocal的耗时
     * @author lihong10 2019/2/25 19:25
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/25 19:25
     * @modify by reason:{原因}
     */
    @Test
    public void test() {
        String value = "thread local value";
        testThreadLocal(1000, 1000000, value);
        testFastThreadLocal(1000, 1000000, value);
    }


}
