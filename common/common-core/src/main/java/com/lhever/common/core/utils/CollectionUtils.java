package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.page.RepeatablePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtils {

    private static final Logger log = LoggerFactory.getLogger(Bean2MapUtil.class);

    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }


    public static boolean isNotEmpty(Collection coll) {
        return !CollectionUtils.isEmpty(coll);
    }


    @SuppressWarnings("Map generic type missing")
    @Deprecated
    public static boolean mapEmpty(Map map) {
        return (map == null || map.size() == 0);
    }

    @SuppressWarnings("Map generic type missing")
    @Deprecated
    public static boolean mapNotEmpty(Map map) {

        return !mapEmpty(map);
    }

    public static boolean isEmpty(Map map) {
        return (map == null || map.size() == 0);
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static <T> int nullSafeSize(Collection<T> collection) {
        if (collection == null) {
            return 0;
        }
        return collection.size();
    }

    public static <T extends Object> T getValueSafely(Map map, Object key, Class<? extends Object> T) {

        if (map == null || map.size() == 0) {
            return null;
        }

        T value = null;
        try {
            value = (T) map.get(key);
        } catch (Exception e) {
            log.error("从map取值, key=[" + key + "], 转换为类型" + T + "错误", e);
        }

        return value;
    }

    public static <T extends Object> T getValue(Map map, Object key, Class<? extends Object> T) {

        T value = null;
        try {
            value = (T) map.get(key);
        } catch (Exception e) {
            log.error("从map取值, key=[" + key + "], 转换为类型" + T + "错误", e);
        }

        return value;
    }

    /**
     * 移除链表中的重复元素，注意：链表中的元素必须正确重写equals和hashCode方法。该方法会改变原来链表的大小
     *
     * @param list
     * @return
     * @author lihong10 2017年11月3日 下午15:42:30
     */
    public static <E> List<E> removeRepeat(List<E> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;

        }
        Set<E> timeSet = new HashSet<E>();
        timeSet.addAll(list); //去重
        list.clear();//清空列表
        list.addAll(timeSet);//去重后追加到列表
        return list;
    }

    public static <ID, A> Map<ID, A> mapping(Collection<A> values, Function<A, ID> idFunc) {
        if (CollectionUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }

        Map<ID, A> map = new HashMap<>();
        for (A a : values) {
            ID id = idFunc.apply(a);
            map.put(id, a);
        }
        return map;
    }

    public static <A, K, V> Map<K, V> mapping(Collection<A> values, Function<A, K> keyFunc, Function<A, V> valueFunc) {
        if (CollectionUtils.isEmpty(values)) {
            return new HashMap<>(0);
        }

        Map<K, V> map = new HashMap<>();
        for (A a : values) {
            K key = keyFunc.apply(a);
            V value = valueFunc.apply(a);
            map.put(key, value);
        }
        return map;
    }

    public static <T> List<T> asList(T... args) {
        if (args == null) {
            return new ArrayList<>(0);
        }
        List<T> result = new ArrayList<>(args.length);
        for (T arg : args) {
            result.add(arg);
        }
        return result;
    }

    public static <T> List<T> addTo(List<T> list, T... args) {
        addTo(list, true, args);
        return list;
    }

    public static <T> Set<T> addTo(Set<T> set, T... args) {
        addTo(set, true, args);
        return set;
    }

    public static <T> Collection<T> addTo(Collection<T> set, boolean ignoreNull, T... args) {
        if (args == null) {
            return set;
        }
        for (T t : args) {
            if (ignoreNull && t == null) {
                continue;
            }
            set.add(t);
        }
        return set;
    }

    public static <K, V> Map<K, V> putTo(Map<K, V> map, K key , V value) {
        map.put(key, value);
        return map;
    }



    public  static List<String> getNotBlank(List<String> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }

        List<String> filtered = list.stream().filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.toList());
        return filtered;
    }

    public static <T> List<String> getNotBlank(List<T> list, Function<T, String> func) {
        if (list == null) {
            return new ArrayList<>(0);
        }

        List<String> filtered = list.stream()
                .filter(i -> i != null)
                .map(i -> func.apply(i))
                .filter(i -> StringUtils.isNotBlank(i))
                .collect(Collectors.toList());
        return filtered;
    }

    public static  <T> List<T> getNotNull(List<T> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }

        List<T> filtered = list.stream().filter(i -> i != null).collect(Collectors.toList());
        return filtered;
    }

    public static <T> List<String> getNotNull(List<T> list, Function<T, String> func) {
        if (list == null) {
            return new ArrayList<>(0);
        }

        List<String> filtered = list.stream()
                .filter(i -> i != null)
                .map(i -> func.apply(i))
                .filter(i -> i != null)
                .collect(Collectors.toList());
        return filtered;
    }



    public static <T> void consumeInPage(List<T> items, int pageSize, Consumer<List<T>> consumer) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize need > 0");
        }

        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        if (items.size() > pageSize) {
            RepeatablePage<T> page = new RepeatablePage<>(items, pageSize);
            for (List<T> subPage : page) {
                consumer.accept(subPage);
            }
        } else {
            consumer.accept(items);
        }

    }


    /**
     * 拆分集合
     * @author lihong10 2020/7/8 11:15
     * @param collection
     * @param batch
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/7/8 11:15
     * @modify by reason:{原因}
     */
    public static <T> List<List<T>> splitCollection(Collection<T> collection, Integer batch) {
        List<List<T>> listList = new ArrayList<>();
        if (isEmpty(collection)) {
            return listList;
        }
        if ((batch == null) || (batch <= 0)) {
            batch = collection.size();
        }

        RepeatablePage<T> page = new RepeatablePage<>(new ArrayList<>(collection), batch);
        for (List<T> subPage : page) {
            List<T> subList = new ArrayList<>();
            for (T t : subPage) {
                subList.add(t);
            }
            listList.add(subList);
        }
        return listList;
    }


    public static <T> List<T> merge(List<List<T>> listOfLists) {
        if (isEmpty(listOfLists)) {
            return new ArrayList<>(0);
        }
        List<T> collect = listOfLists.stream().filter(li -> isNotEmpty(li)).flatMap(
                subList -> subList.stream().map(Function.identity())
        ).collect(Collectors.toList());
        return collect;
    }

    public static String repeatN(String obj, int n, String delimiter) {
        String  result = String.join(delimiter, Collections.nCopies(n, obj));
        return result;
    }

    public static String repeatN(int n, String obj) {
        return repeatN(obj, n, CommonConsts.EMPTY);
    }

    public static <E> void predicateAndAdd(Collection<E> collection, E e, Predicate<E> predicate) {
        if (predicate.test(e)) {
            collection.add(e);
        }
    }


    /**
     * 先过滤，再映射
     * @author lihong10 2020/11/13 10:18
     * @param list
     * @param predicate
     * @param mapper
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/11/13 10:18
     * @modify by reason:{原因}
     */
    public static <T, R> List<R> filterAndMap(List<T> list, Predicate<T> predicate, Function<? super T, ? extends R> mapper) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>(0);
        }

        List<R> filtered = list.stream()
                .filter(predicate)
                .map(mapper)
                .collect(Collectors.toList());
        return filtered;
    }

    /**
     * 先映射，再过滤
     * @author lihong10 2020/11/13 10:18
     * @param list
     * @param predicate
     * @param mapper
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/11/13 10:18
     * @modify by reason:{原因}
     */
    public static <T, R> List<R> mapAndFilter(List<T> list, Function<? super T, ? extends R> mapper, Predicate<R> predicate) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>(0);
        }

        List<R> filtered = list.stream()
                .map(mapper)
                .filter(predicate)
                .collect(Collectors.toList());
        return filtered;
    }


    public static <A> List<A> filter(Collection<A> list, Predicate<A> predicate) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<A> filtered = list.stream().filter(predicate).collect(Collectors.toList());
        return filtered;
    }

    public static <A, B> List<B> map(Collection<A> collection, Function<A, B> func) {
        if (CollectionUtils.isEmpty(collection)) {
            return new ArrayList<>(0);
        }
        ArrayList<B> collect = collection.stream().map(i -> func.apply(i)).collect(Collectors.toCollection(() -> new ArrayList<>(collection.size())));
        return collect;
    }

    public static <A> List<A> addAll(Collection<A>...  srcs) {
        if (srcs == null) {
            return new ArrayList<>(0);
        }
        int total = 0;
        for (Collection<A> src : srcs) {
            if (src == null) {
                continue;
            }
            total += src.size();
        }
        List<A> all = new ArrayList<>(total);
        for (Collection<A> src : srcs) {
            if (src == null) {
                continue;
            }
            all.addAll(src);
        }
        return all;
    }













}











