package com.lhever.common.core.test;

import com.lhever.common.core.support.singleton.Singleton;
import com.lhever.common.core.utils.StringUtils;
import org.junit.Test;

import java.util.function.Supplier;

public class SingletonTest {

    //SingletonObj对象的单例类
    private static Singleton<SingletonObj> singleton = new Singleton<SingletonObj>(() -> new SingletonObj());

    @Test
    public void testSingleTon() {
        SingletonObj first = singleton.get();
        System.out.println(first);

        //永远得到同一个SingletonObj对象
        Supplier<SingletonObj> supplier = () -> {
            SingletonObj singletonObj = singleton.get();
            System.out.println(singletonObj);
            return singletonObj;
        };

        System.out.println(first == supplier.get());
        System.out.println(first == supplier.get());
        System.out.println(first == supplier.get());
    }

    public static class SingletonObj {
        private String id;

        public SingletonObj() {
            this.id = StringUtils.getUuid();
        }

        @Override
        public String toString() {
            return id;
        }
    }


}
