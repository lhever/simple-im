package com.lhever.common.core.test;

import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/12/7 11:18
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/12/7 11:18
 * @modify by reason:{方法名}:{原因}
 */
public class CollectionUtilsTest {


    /**
     我们来看看自己手写的LRU例子

     1.首先往map里面添加了5个元素，使用的是尾插法，顺序应该是1,2,3,4,5。

     2.调用了map.put("6", "6")，通过尾插法插入元素6，此时的顺序是1,2,3,4,5,6，
     然后 LinkedHashMap调用removeEldestEntry()，map里面的元素数量是6，大于指定的size，返回true。
     LinkedHashMap会删除头节点的元素，此时顺序应该是2,3,4,5,6。

     3.调用了map.get("2")，元素2被命中，元素2需要移动到链表尾部，此时的顺序是3,4,5,6,2

     4.调用了map.put("7", "7")，和步骤2一样的操作。此时的顺序是4,5,6,2,7

     5.调用了map.get("4"),和步骤3一样的操作。此时的顺序是5,6,2,7,4
     */
    @Test
    public void testLRU() {
        int size = 5;
        /**
         * false, 基于插入排序
         * true, 基于访问排序
         */
        Map<String, String> map = new LinkedHashMap<String, String>(size, .75F,
                true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                boolean tooBig = size() > size;

                if (tooBig) {
                    System.out.println("最近最少使用的key=" + eldest.getKey());
                }
                return tooBig;
            }
        };

        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        System.out.println(map.toString());

        map.put("6", "6");
        System.out.println(map.toString());
        map.get("2");
        System.out.println(map.toString());
        map.put("7", "7");
        System.out.println(map.toString());
        map.get("4");

        System.out.println(map.toString());
    }



    @Test
    public void testInsertOrder() {
        int size = 5;
        /**
         * false, 基于插入排序
         * true, 基于访问排序
         */
        Map<String, String> map = new LinkedHashMap<String, String>(size, .75F,
                false) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                boolean tooBig = size() > size;

                if (tooBig) {
                    System.out.println("最近最少使用的key=" + eldest.getKey());
                }
                return tooBig;
            }
        };

        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        System.out.println(map.toString());

        map.put("6", "6");
        System.out.println(map.toString());
        map.get("2");
        System.out.println(map.toString());
        map.put("7", "7");
        System.out.println(map.toString());
        map.get("4");

        System.out.println(map.toString());
    }



}
