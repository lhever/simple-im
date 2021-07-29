package com.lhever.common.kafka.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/8/22 12:03
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/8/22 12:03
 * @modify by reason:{方法名}:{原因}
 */
public class CommonUtils {

    public static boolean isAnyBlank(final CharSequence... css) {
        //注意，此处是对apache common lang3中对应方法的bug修复
        if (css == null) {
            return true;
        }
        if (Array.getLength(css) == 0) {
            //注意，此处与apache common lang3中逻辑一致
            return false;
        }

        for (final CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }



    public static boolean isEmpty(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }



    public static boolean isEmpty(Collection coll) {
        return (coll == null || coll.isEmpty());
    }

    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);

    }

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public final static String getUuid() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }


    public static String join(List<String> list, String joiner) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(joiner);
            }
        }
        return sb.toString();
    }

    public static <ID, A> Map<ID, A> mapping(Collection<A> values, Function<A, ID> idFunc) {
        if (isEmpty(values)) {
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
        if (isEmpty(values)) {
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

    public static  <T> List<T> getNotNull(List<T> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }

        List<T> filtered = list.stream().filter(i -> i != null).collect(Collectors.toList());
        return filtered;
    }

    public static <E> List<E> removeRepeat(List<E> list) {
        if (isEmpty(list)) {
            return new ArrayList<>(0);
        }
        Set<E> timeSet = new HashSet<>(list);//放入集合去重
        return new ArrayList<>(timeSet);
    }


    public static int findNextPositivePowerOfTwo(final int value) {
        assert value > Integer.MIN_VALUE && value < 0x40000000;
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    /**
     * Fast method of finding the next power of 2 greater than or equal to the supplied value.
     * <p>This method will do runtime bounds checking and call {@link #findNextPositivePowerOfTwo(int)} if within a
     * valid range.
     *
     * @param value from which to search for next power of 2
     * @return The next power of 2 or the value itself if it is a power of 2.
     * <p>Special cases for return values are as follows:
     * <ul>
     * <li>{@code <= 0} -> 1</li>
     * <li>{@code >= 2^30} -> 2^30</li>
     * </ul>
     */
    public static int safeFindNextPositivePowerOfTwo(final int value) {
        return value <= 0 ? 1 : value >= 0x40000000 ? 0x40000000 : findNextPositivePowerOfTwo(value);
    }


}
