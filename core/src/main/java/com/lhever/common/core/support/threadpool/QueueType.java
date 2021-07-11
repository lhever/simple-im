package com.lhever.common.core.support.threadpool;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 线程池队列类型枚举常量
 *
 * @author lihong 2016年4月22日 上午11:21:43
 * @version v2.0
 */
public enum QueueType {

    /**
     * 数组实现的有界阻塞队列
     */

    ARRAY_BLOCKING_QUEUE(10, ArrayBlockingQueue.class),

    /**
     * 链表实现的无界阻塞队列
     */
    LINKED_BLOCKING_QUEUE(20, SynchronousQueue.class),

    /**
     * 同步阻塞队列，队列无容量
     */
    SYNCHRONOUS_QUEUE(30, LinkedBlockingQueue.class);

    private int val;
    private Class<?> cls;

    private QueueType(int val, Class<?> cls) {
        this.val = val;
        this.cls = cls;
    }


    /**
     * @return val
     * @author lihong 2015年9月18日 上午11:14:21
     * @since v1.0
     */
    public int value() {
        return this.val;
    }

    /**
     * @return Class<?>
     * @author lihong 2016年4月22日 下午12:04:42
     * @since v2.0
     */
    public Class<?> getType() {
        return this.cls;
    }

    /**
     * @param val
     * @return Class<?>
     * @author lihong 2016年4月22日 下午12:50:30
     * @since v2.0
     */
    public static Class<?> getType(int val) {
        QueueType[] t = QueueType.values();
        for (QueueType type : t) {
            if (val == type.value()) {
                return type.getType();
            }
        }
        throw new IllegalArgumentException("找不到枚举值");
    }


    /**
     * 检查值是否存在于枚举中
     *
     * @param args
     * @return boolean
     * @author lihong 2016年4月22日 上午11:26:54
     * @since v2.0
     */
    public static boolean contains(long args) {
        boolean isExist = false;
        QueueType[] t = QueueType.values();
        for (QueueType type : t) {
            if (args == type.value()) {
                isExist = true;
            }
        }
        return isExist;
    }

}
