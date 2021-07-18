package com.lhever.common.core.support.token;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/10/13 22:16
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/10/13 22:16
 * @modify by reason:{方法名}:{原因}
 */
public class CircularCharStore implements Serializable {

    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    private static final long serialVersionUID = 42L;
    private static final String ALPHABET_AND_NUM_STRING =
             "abcdefghijklmnopqrstuvwxyz" +
             "_~`!@#$%^&*()\\-+<>=\\[\\]\\{\\}:;\'\",\\./\\?\\|\\\\" +
             "0123456789" +
             "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private char[] chars;



    public CircularCharStore() {
        this(4096);
    }

    public CircularCharStore(int size) {
        int normalSize = safeFindNextPositivePowerOfTwo(size);
        this.chars = new char[normalSize];
        for (int i = 0; i < normalSize; i++) {
            int idx = random().nextInt(ALPHABET_AND_NUM_STRING.length());
            char c = ALPHABET_AND_NUM_STRING.charAt(idx);
            chars[i] = c;
        }
    }

    public static Random random() {
        int i = atomicInteger.addAndGet(777777);
        if (i < 0) {
            atomicInteger.set(0);
            i = 0;
        }
        Random random = new Random(System.currentTimeMillis() + i );//System.currentTimeMillis()短时间内调用，值相同，所以再增加一点波动
        return random;
    }

    public CircularCharStore(String string) {
        if (safeFindNextPositivePowerOfTwo(string.length()) != string.length()) {
            throw new IllegalArgumentException("the length of string must be power of two");
        }
        this.chars = string.toCharArray();
    }

    public int getLength() {
        return chars.length;
    }

    public static int findNextPositivePowerOfTwo(final int value) {
        assert value > Integer.MIN_VALUE && value < 0x40000000;
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public static int safeFindNextPositivePowerOfTwo(final int value) {
        return value <= 0 ? 1 : value >= 0x40000000 ? 0x40000000 : findNextPositivePowerOfTwo(value);
    }


    public String slice(int offset, int len) {
        if (offset < 0 || offset > chars.length - 1) {
            throw new IllegalArgumentException("offset error");
        }

        if (len <= 0 || len > chars.length) {
            throw new IllegalArgumentException("len error");
        }

        if (offset + len > chars.length) {
            String left = new String(chars, offset, chars.length - offset);
            String right = new String(chars, 0, len - (chars.length - offset));
            return left + right;
        } else {
            String string = new String(chars, offset, len);
            return string;
        }
    }

    public static void main(String[] args) {
        CircularCharStore store = new CircularCharStore("我是中国人,我爱我的祖国。老北鼻");
        System.out.println(store.slice(0, 5));
        System.out.println(store.slice(1, 5));
        System.out.println(store.slice(2, 5));
        System.out.println(store.slice(3, 5));
        System.out.println(store.slice(4, 5));
        System.out.println(store.slice(5, 5));
        System.out.println(store.slice(6, 5));
        System.out.println(store.slice(7, 5));
        System.out.println(store.slice(8, 5));
        System.out.println(store.slice(9, 5));
        System.out.println(store.slice(10, 5));
        System.out.println(store.slice(11, 5));
        System.out.println(store.slice(0, 16));
        System.out.println(store.slice(15, 16));
    }




}
