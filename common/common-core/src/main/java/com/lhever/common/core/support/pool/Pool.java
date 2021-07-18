/*
 * @copyright Copyright (c) 2015-2016 iWonCloud Tech Co., LTD
 * @license http://www.iwoncloud.com/code/license
 * @author lihong
 * @date 2016年6月8日 上午11:44:25
 * @version v2.0
 */
package com.lhever.common.core.support.pool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对象池的一种简单并且通用的实现
 *
 * @author lihong 2016年6月8日 上午11:44:25
 * @version v2.0
 */
public class Pool {
    public static volatile AtomicInteger count = new AtomicInteger(0);

    /**
     * 工厂接口，定义对象池中对象的产生方式
     *
     * @author lihong 2016年6月8日 上午11:45:40
     * @version v2.0
     */
    public interface Factory {

        /**
         * 生成对象池中对象实例的方法
         *
         * @return Object
         * @author lihong 2016年6月8日 上午11:46:17
         * @since v2.0
         */
        public Object newInstance();
    }

    private final int initialPoolSize;
    private final int maxPoolSize;
    private final Factory factory;
    private transient Object[] pool;
    private transient int nextAvailable;
    private transient Object lock = new Object();

    /**
     * 构造
     *
     * @param initialPoolSize 初始池大小
     * @param maxPoolSize     最大池大小
     * @param factory         工厂实现
     * @author lihong 2016年6月8日 上午11:48:20
     * @since v2.0
     */
    public Pool(int initialPoolSize, int maxPoolSize, Factory factory) {
        this.initialPoolSize = initialPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.factory = factory;
    }

    /**
     * 从对象池中获取对象
     *
     * @return Object
     * @author lihong 2016年6月8日 上午11:49:03
     * @since v2.0
     */
    public Object fetchFromPool() {
        Object result;
        synchronized (lock) {
            /*
             *对象池尚未初始化，则初始化对象池
             */
            if (pool == null) {
                pool = new Object[maxPoolSize];
                for (nextAvailable = initialPoolSize; nextAvailable > 0; ) {
                    putInPool(factory.newInstance());
                }
            }
            while (nextAvailable == maxPoolSize)//对象池内部没有空闲对象，则暂停获取对象的线程
            {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted whilst waiting for a free item in the pool : " + e.getMessage());
                }
            }
            result = pool[nextAvailable++];
            if (result == null) {
                result = factory.newInstance();
                putInPool(result);
                ++nextAvailable;
            }
//			System.out.println("取出后当前指针是：" + nextAvailable);
        }
        return result;
    }

    /**
     * 从新存入对象
     *
     * @param object void
     * @author lihong 2016年6月8日 下午3:25:25
     * @since v2.0
     */
    public void putInPool(Object object) //  protected void putInPool(Object object)
    {
        synchronized (lock) {
            pool[--nextAvailable] = object;
//			System.out.println("归还后当前指针是：" + nextAvailable);
            lock.notifyAll(); // notify() ?????
        }
    }

    /**
     * @return the pool
     */
    public Object[] getPool() {
        return pool;
    }

    /**
     * 实例化锁
     *
     * @return Object
     * @author lihong 2016年6月8日 下午5:26:30
     * @since v2.0
     */
    private Object readResolve() {
        lock = new Object();
        return this;
    }


    /**
     * 测试
     *
     * @param args void
     * @author lihong 2016年6月8日 下午9:41:40
     * @since v2.0
     */
    public static void main(String... args) {

        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Task task = new Task();
            task.setName("task-" + i);
            taskList.add(task);
        }

        taskList.forEach( t -> {
            t.start();
        });

        taskList.forEach( t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}


class Task extends Thread {
    protected static String format = "yyyy-MM-dd";
    public static Pool p = new Pool(2, 5, new Pool.Factory() {
        public Object newInstance() {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df;
        }

    });


    @SuppressWarnings("deprecation")
    public void run() {
        for (int i = 0; i < 15; i++) {
            SimpleDateFormat df = (SimpleDateFormat) p.fetchFromPool();


            String[] dateStr = new String[]{"1994-09-23", "2010-10-10", "2011-11-11", "2012-12-12", "2013-12-13", "2014-02-14", "2015-08-12", "1990-09-09", "2000-08-23"};
            Random random = new Random();
            int index = random.nextInt(7);
            try {
                Date date = df.parse(dateStr[index]);
                System.out.println(date.toLocaleString());
            } catch (ParseException e) {
                System.out.print("解析日期出错");
                e.printStackTrace();
            } finally {
                p.putInPool(df);
            }

            System.out.println(Arrays.toString(p.getPool()));

        }


    }
}
