package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.consts.RegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    private static final String EMPTY = "";
    private static final String COMMA = ",";
    private static final String CHARSET = "UTF-8";
    private static final String ALPHABET_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHABET_AND_NUM_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static boolean isArrayEmpty(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return true;
        }

        return false;//所有元素都为空的情况
    }

    public static boolean isArrayElementEmpty(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return true;
        }
        for (Object obj : arr) {
            if (obj != null) {
                return false;
            }
        }
        return true;//所有元素都为空的情况
    }


    /**
     * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace only
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
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

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is
     * not empty and not null and not whitespace only
     * @since 2.0
     * @since 3.0 Changed signature from isNotBlank(String) to isNotBlank(CharSequence)
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * <p>Checks if any of the CharSequences are empty ("") or null or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isAnyBlank(null)             = true
     * StringUtils.isAnyBlank(null, "foo")      = true
     * StringUtils.isAnyBlank(null, null)       = true
     * StringUtils.isAnyBlank("", "bar")        = true
     * StringUtils.isAnyBlank("bob", "")        = true
     * StringUtils.isAnyBlank("  bob  ", null)  = true
     * StringUtils.isAnyBlank(" ", "bar")       = true
     * StringUtils.isAnyBlank(new String[] {})  = false
     * StringUtils.isAnyBlank(new String[]{""}) = true
     * StringUtils.isAnyBlank("foo", "bar")     = false
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if any of the CharSequences are empty or null or whitespace only
     * @since 3.2
     */
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

    /**
     * <p>Checks if none of the CharSequences are empty (""), null or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isNoneBlank(null)             = false
     * StringUtils.isNoneBlank(null, "foo")      = false
     * StringUtils.isNoneBlank(null, null)       = false
     * StringUtils.isNoneBlank("", "bar")        = false
     * StringUtils.isNoneBlank("bob", "")        = false
     * StringUtils.isNoneBlank("  bob  ", null)  = false
     * StringUtils.isNoneBlank(" ", "bar")       = false
     * StringUtils.isNoneBlank(new String[] {})  = true
     * StringUtils.isNoneBlank(new String[]{""}) = false
     * StringUtils.isNoneBlank("foo", "bar")     = true
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if none of the CharSequences are empty or null or whitespace only
     * @since 3.2
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }

    /**
     * <p>Checks if all of the CharSequences are empty (""), null or whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.isAllBlank(null)             = true
     * StringUtils.isAllBlank(null, "foo")      = false
     * StringUtils.isAllBlank(null, null)       = true
     * StringUtils.isAllBlank("", "bar")        = false
     * StringUtils.isAllBlank("bob", "")        = false
     * StringUtils.isAllBlank("  bob  ", null)  = false
     * StringUtils.isAllBlank(" ", "bar")       = false
     * StringUtils.isAllBlank("foo", "bar")     = false
     * StringUtils.isAllBlank(new String[] {})  = true
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if all of the CharSequences are empty or null or whitespace only
     * @since 3.6
     */
    public static boolean isAllBlank(final CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        }
        for (final CharSequence cs : css) {
            if (isNotBlank(cs)) {
                return false;
            }
        }
        return true;
    }

    // Empty checks
    //-----------------------------------------------------------------------

    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 如果某个字符串为null,则替换成特点的字符串
     *
     * @return
     * @author jianghaitao6 2019/3/1 21:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/1 21:44
     * @modify by reason:{原因}
     */
    public static String orElse(final String cs, String str) {
        if (StringUtils.isBlank(cs)) {
            return str;
        }
        return cs;
    }

    /**
     * <p>Checks if a CharSequence is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * <p>Checks if any of the CharSequences are empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isAnyEmpty(null)             = true
     * StringUtils.isAnyEmpty(null, "foo")      = true
     * StringUtils.isAnyEmpty("", "bar")        = true
     * StringUtils.isAnyEmpty("bob", "")        = true
     * StringUtils.isAnyEmpty("  bob  ", null)  = true
     * StringUtils.isAnyEmpty(" ", "bar")       = false
     * StringUtils.isAnyEmpty("foo", "bar")     = false
     * StringUtils.isAnyEmpty(new String[]{})   = false
     * StringUtils.isAnyEmpty(new String[]{""}) = true
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if any of the CharSequences are empty or null
     * @since 3.2
     */
    public static boolean isAnyEmpty(final CharSequence... css) {
        //注意，此处是对apache common lang3中对应方法的bug修复
        if (css == null) {
            return true;
        }
        if (Array.getLength(css) == 0) {
            //注意，此处与apache common lang3中对应方法逻辑一致
            return false;
        }
        for (final CharSequence cs : css) {
            if (isEmpty(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Checks if none of the CharSequences are empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isNoneEmpty(null)             = false
     * StringUtils.isNoneEmpty(null, "foo")      = false
     * StringUtils.isNoneEmpty("", "bar")        = false
     * StringUtils.isNoneEmpty("bob", "")        = false
     * StringUtils.isNoneEmpty("  bob  ", null)  = false
     * StringUtils.isNoneEmpty(new String[] {})  = true
     * StringUtils.isNoneEmpty(new String[]{""}) = false
     * StringUtils.isNoneEmpty(" ", "bar")       = true
     * StringUtils.isNoneEmpty("foo", "bar")     = true
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if none of the CharSequences are empty or null
     * @since 3.2
     */
    public static boolean isNoneEmpty(final CharSequence... css) {
        return !isAnyEmpty(css);
    }

    /**
     * <p>Checks if all of the CharSequences are empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isAllEmpty(null)             = true
     * StringUtils.isAllEmpty(null, "")         = true
     * StringUtils.isAllEmpty(new String[] {})  = true
     * StringUtils.isAllEmpty(null, "foo")      = false
     * StringUtils.isAllEmpty("", "bar")        = false
     * StringUtils.isAllEmpty("bob", "")        = false
     * StringUtils.isAllEmpty("  bob  ", null)  = false
     * StringUtils.isAllEmpty(" ", "bar")       = false
     * StringUtils.isAllEmpty("foo", "bar")     = false
     * </pre>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if all of the CharSequences are empty or null
     * @since 3.6
     */
    public static boolean isAllEmpty(final CharSequence... css) {
        if (ArrayUtils.isEmpty(css)) {
            return true;
        }
        for (final CharSequence cs : css) {
            if (isNotEmpty(cs)) {
                return false;
            }
        }
        return true;
    }

    // Equals
    //-----------------------------------------------------------------------

    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered to be equal. The comparison is case sensitive.</p>
     *
     * <pre>
     * StringUtils.equals(null, null)   = true
     * StringUtils.equals(null, "abc")  = false
     * StringUtils.equals("abc", null)  = false
     * StringUtils.equals("abc", "abc") = true
     * StringUtils.equals("abc", "ABC") = false
     * </pre>
     *
     * @param cs1 the first CharSequence, may be {@code null}
     * @param cs2 the second CharSequence, may be {@code null}
     * @return {@code true} if the CharSequences are equal (case-sensitive), or both {@code null}
     * @see Object#equals(Object)
     * @since 3.0 Changed signature from equals(String, String) to equals(CharSequence, CharSequence)
     */
    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, cs1.length());
    }

    /**
     * <p>Compares two CharSequences, returning {@code true} if they represent
     * equal sequences of characters, ignoring case.</p>
     *
     * <p>{@code null}s are handled without exceptions. Two {@code null}
     * references are considered equal. Comparison is case insensitive.</p>
     *
     * <pre>
     * StringUtils.equalsIgnoreCase(null, null)   = true
     * StringUtils.equalsIgnoreCase(null, "abc")  = false
     * StringUtils.equalsIgnoreCase("abc", null)  = false
     * StringUtils.equalsIgnoreCase("abc", "abc") = true
     * StringUtils.equalsIgnoreCase("abc", "ABC") = true
     * </pre>
     *
     * @param str1 the first CharSequence, may be null
     * @param str2 the second CharSequence, may be null
     * @return {@code true} if the CharSequence are equal, case insensitive, or
     * both {@code null}
     * @since 3.0 Changed signature from equalsIgnoreCase(String, String) to equalsIgnoreCase(CharSequence, CharSequence)
     */
    public static boolean equalsIgnoreCase(final CharSequence str1, final CharSequence str2) {
        if (str1 == null || str2 == null) {
            return str1 == str2;
        } else if (str1 == str2) {
            return true;
        } else if (str1.length() != str2.length()) {
            return false;
        } else {
            return CharSequenceUtils.regionMatches(str1, true, 0, str2, 0, str1.length());
        }
    }


    public static List<Integer> stringToIntegerList(List<String> inList) {
        List<Integer> iList = new ArrayList<Integer>(inList.size());
        for (int i = 0; i < inList.size(); i++) {
            try {
                iList.add(Integer.parseInt(inList.get(i)));
            } catch (Exception e) {
            }
        }
        return iList;
    }

    public static void println(String format, Object... objects) {
        String str = ParseUtils.parseArgs(format, objects);
        System.out.println(str);
    }


    public static String join(final Object[] array, final String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(final Object[] array, String separator, final int startIndex, final int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        final int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        final StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }


    public static List<Integer> stringArrayToIntegerList(String[] split) {

        List<Integer> li = new ArrayList<Integer>();
        if (StringUtils.isArrayEmpty(split)) {
            return li;
        }
        for (String str : split) {
            if (StringUtils.isBlank(str)) {
                continue;
            }

            try {
                li.add(Integer.parseInt(str.trim()));
            } catch (Exception e) {
                logger.error("", e);
            }

        }

        return li;
    }

    public static List<Integer> splitToIntegerList(String integers, String spliter) {

        if (isBlank(integers)) {
            return new ArrayList<>(0);
        }

        String[] split = integers.split(spliter);
        List<Integer> li = stringArrayToIntegerList(split);
        return li;
    }


    public static List<String> splitToList(String integers, String spliter) {
        if (isBlank(integers)) {
            return new ArrayList<>(0);
        }

        String[] split = integers.split(spliter);

        if (split == null || split.length == 0) {
            return new ArrayList<>(0);
        }

        List<String> strings = Arrays.asList(split);
        return strings;
    }

    public static List<Integer> splitToIntegerList(String integers) {

        return splitToIntegerList(integers, COMMA);
    }

    public static byte[] stringToByteArray(String msg, String encoding) {
        if (StringUtils.isBlank(msg)) {
            return null;
        }

        encoding = StringUtils.isBlank(encoding) ? CHARSET : encoding;

        byte[] bytes = null;
        try {
            bytes = msg.getBytes(encoding);
        } catch (Exception e) {
            logger.error("string to bytes error", e);
        }
        return bytes;
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


    /**
     * uuid
     *
     * @return a random uuid
     * @author lihong10 2015-4-10 下午5:10:15
     * @since v1.0
     */
    public final static String getUuid() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 对字符串进行md5加密
     *
     * @param pwd 要加密的字符串
     * @return 加密后的字符串
     * @author lihong10 2015-4-10 下午5:35:36
     * @since v1.0
     */
    public final static String md5(String pwd) {
        char hexDigits[] =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = pwd.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("md5 fail", e);
            return null;
        }
    }

    /**
     * 生成一个随机整数
     *
     * @return 随机整数
     * @author lihong10 2015-4-10 下午5:48:18
     * @since v1.0
     */
    public final static int getRandomInt() {
        Random rand = new Random(System.currentTimeMillis());
        return rand.nextInt();
    }

    /**
     * @param length 数字字符串长度
     * @return 随机数字字符串
     * @author lihong10 2015-6-8 下午6:16:48
     * @since v1.0
     */
    public final static String getRandomIntString(int length) {
        Random r = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = r.nextInt(10);
            if (i == 0 && num == 0) {
                while (num == 0) {
                    num = r.nextInt(10);
                }
            }
            builder.append(num);
        }
        return builder.toString();
    }

    /**
     * 产生指定长度的随机字符串
     *
     * @param length
     * @return String
     * @author lihong 2015年9月24日 下午5:22:16
     * @since v1.0
     */
    public static String getRandomString(int length) { // length表示生成字符串的长度
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(ALPHABET_AND_NUM_STRING.length());
            sb.append(ALPHABET_AND_NUM_STRING.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length
     * @return String
     * @author lihong 2015年10月31日 下午1:07:54
     * @since v1.0
     */
    public static String getRandomAlphabetString(int length) { // length表示生成字符串的长度
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(ALPHABET_STRING.length());
            sb.append(ALPHABET_STRING.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 检验字符串的格式是否匹配正则表达式
     *
     * @param regex 正则
     * @param str   要检验的字符串
     * @return 是否匹配
     * @author lihong10 2015-4-14 下午12:19:14
     * @since v1.0
     */
    public static boolean match(String regex, String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.lookingAt();
    }

    /**
     * 检查url格式是否正确
     *
     * @param url
     * @return url格式是否正确
     * @author lihong10 2015-4-14 下午1:03:46
     * @since v1.0
     */
    public static boolean isURL(String url) {
        return match(RegExp.URL, url);
    }

    /**
     * 检查手机号码是否合法
     *
     * @param phone
     * @return 手机号码是否合法
     * @author lihong10 2015-5-7 下午2:40:53
     * @since v1.0
     */
    public static boolean isPhoneNumber(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return match(RegExp.PHONE, phone);
    }

    /**
     * 检查日期字符串是否符合yyyyMMdd格式
     *
     * @param dateString
     * @return boolean
     * @author lihong 2016年4月1日 下午3:35:02
     * @since v2.0
     */
    public static boolean isDateString(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return false;
        }
        return match(RegExp.DateStr, dateString);

    }

    /**
     * 检查字符串是否符合hh:mm:ss格式
     *
     * @param timeStr
     * @return boolean
     * @author lihong 2016年4月5日 上午10:19:30
     * @since v2.0
     */
    public static boolean isTimeString(String timeStr) {
        if (StringUtils.isBlank(timeStr)) {
            return false;
        }
        return match(RegExp.TimeStr, timeStr);

    }

    /**
     * 将字符串解析成Boolean值
     *
     * @param str
     * @param dft default value to be returned when str is not a valid bool
     *            string
     * @return boolean
     * @author lihong10 2015-4-14 下午1:32:45
     * @since v1.0
     */
    public static boolean getBoolean(String str, boolean dft) {
        boolean v = dft;
        try {
            v = Boolean.parseBoolean(str);
        } catch (Exception e) {
            logger.warn(str + "不是一个合法的bool值");
        }
        return v;
    }

    /**
     * 将字符串解析成long
     *
     * @param str string
     * @param dft 解析异常时返回的默认值
     * @return long
     * @author lihong10 2015-4-21 下午4:20:50
     * @since v1.0
     */
    public static long getLong(String str, long dft) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            logger.warn(String.format("error parsing String[%s] to %s", str, long.class.getName()));
            return dft;
        }
    }

    /**
     * 将字符串解析成int
     *
     * @param str
     * @param dft
     * @return int
     * @author lihong10 2015-6-2 下午7:51:53
     * @since v1.0
     */
    public static int getInt(String str, int dft) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            logger.warn(String.format("error parsing String[%s] to %s", str, int.class.getName()));
            return dft;
        }
    }

    public static double getDouble(String str, double dft) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            logger.warn(String.format("error parsing String[%s] to %s", str, double.class.getName()));
            return dft;
        }
    }

    public static BigDecimal getBigDecimal(String str, BigDecimal dft) {
        try {
            return new BigDecimal(str);
        } catch (Throwable e) {
            logger.warn(String.format("error parsing String[%s] to %s", str, BigDecimal.class.getName()));
            return dft;
        }
    }


    /**
     * 是否为邮箱
     *
     * @param email
     * @return 是否为邮箱
     * @author lihong10 2015-6-8 下午7:28:06
     * @since v1.0
     */
    public static boolean isEmail(String email) {
        return match(RegExp.EMAIL, email);
    }

    /**
     * @param dateStr
     * @return 是否为合法的日期字符串
     * @author lihong10 2015-6-9 下午7:58:52
     * @since v1.0
     */
    public static boolean isBirthday(String dateStr) {
        if (dateStr == null) {
            return false;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 将字符串数组拼接成逗号连接的字符串
     *
     * @param arr
     * @return 逗号连接的字符串
     * @author lihong10 2015-6-28 下午5:46:24
     * @since v1.0
     */
    public static String join(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String str = arr[i];
            sb.append(str);
            if (i < arr.length - 1) {
                sb.append(",");
            }
        }
        String result = sb.toString();
        return result;
    }


    public static String appendAll(String... items) {
        if (items == null) {
            return CommonConsts.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            builder.append(item);
        }
        return builder.toString();
    }

    /**
     * 将list组拼接成joiner连接的字符串
     *
     * @param list
     * @param joiner
     * @return 逗号连接的字符串
     * @author lihong10 2015-6-28 下午5:46:24
     * @since v1.0
     */
    @SuppressWarnings("rawtypes")
    public static String join(List list, String joiner) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(joiner);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符数组中的每一个字符使用单引号包围
     *
     * @param array
     * @return List<String>
     * @author lihong 2016年3月8日 下午7:06:38
     * @since v2.0
     */
    public static List<String> wrapWithSingleQuote(String[] array) {
        List<String> list = new ArrayList<String>();
        for (String str : array) {
            list.add("'" + str + "'");
        }
        return list;

    }

    /**
     * 将字符列表中的每一个字符使用单引号包围
     *
     * @param list
     * @return List<String>
     * @author lihong 2016年3月8日 下午8:04:45
     * @since v2.0
     */
    public static List<String> wrapWithSingleQuote(List<String> list) {

        /*
         * for(int i = 0; i < list.size(); i++) { list.set(i, "'" + list.get(i) + "'"); } return list;
         */

        // 注：上述被注释的方法直接替换原列表中的内容，目前发现会引起某些意想不到的错误，故弃用
        List<String> newList = new ArrayList<String>();
        for (String str : list) {
            newList.add("'" + str + "'");
        }
        return newList;

    }

    /**
     * 判断一个字符串是否为纯数字
     *
     * @param str
     * @return 是否为纯数字
     * @author lihong10 2015-7-27 上午10:43:18
     * @since v1.0
     */
    public static boolean isNumber(String str) {
        return match(RegExp.NUMBER, str);
    }

    /**
     * 去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return String
     * @author lihong 2016年1月19日 下午4:35:42
     * @since v1.4
     */
    public static String replaceWhitespace(String str) {
        if (str != null && !"".equals(str)) {
            Pattern p = Pattern.compile(RegExp.WHITE_CHAR);
            Matcher m = p.matcher(str);
            String strNoBlank = m.replaceAll("");
            return strNoBlank;
        } else {
            return str;
        }
    }


    /**
     * 密码是否合法
     *
     * @param password
     * @return 密码是否合法
     * @author guoliang5 2018-09-28 下午9:28:06
     * @since v1.0
     */
    public static boolean isPasssword(String password) {
        return match(RegExp.PASSWORD, password);
    }

    /**
     * 账号是否合法
     *
     * @param account
     * @return 账号是否合法
     * @author guoliang5 2018-09-28 下午9:28:06
     * @since v1.0
     */
    public static boolean isAccount(String account) {
        return match(RegExp.ACCOUNT, account);
    }


    public static byte[] getBytes(String src, String charset) {
        try {
            return src.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error", e);
        }
        return null;
    }

    public static byte[] getBytes(String src) {
        try {
            return src.getBytes(CommonConsts.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
            logger.error("encoding error", e);
        }
        return null;
    }

    public static String[] stringSplit(String str, int size) {
        int len = str.length();
        String[] arr = new String[(len + size - 1) / size];
        for (int i = 0; i < len; i += size) {
            int n = len - i;
            if (n > size) {
                n = size;
            }
            arr[i / size] = str.substring(i, i + n);
        }
        return arr;
    }


    public static <T, R> String join(List<T> list, Function<T, R> func, String prefix, String suffix, String sep) {
        StringBuilder sb = new StringBuilder();
        //避免报告空指针
        list = (list == null) ? new ArrayList<>(0) : list;

        //只能判断是否为null
        if (prefix != null) {
            sb.append(prefix);
        }
        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                if (sep != null) {
                    //只能判断是否为null
                    sb.append(sep);
                }
            }

            T t = list.get(i);
            if (func == null) {
                sb.append(t);
            } else {
                R r = func.apply(t);
                sb.append(r);
            }
        }
        //只能判断是否为null
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    /**
     * <p>Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.deleteWhitespace(null)         = null
     * StringUtils.deleteWhitespace("")           = ""
     * StringUtils.deleteWhitespace("abc")        = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str  the String to delete whitespace from, may be null
     * @return the String without whitespaces, {@code null} if null String input
     */
    public static String deleteWhitespace(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }

    /**
     * 方法描述: 删除头部的空白符
     * @author lihong10 2021/1/20 21:32
     * @param str
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2021/1/20 21:32
     * @modify by reason:{原因}
     */
    public static String deleteHeadWhitespace(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        int offset = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                offset = i;
                break;
            }
        }
        if (offset == 0) {
            return str;
        }
        return str.substring(offset);
    }


    public static String checkNotBlank(String data, String msg) {
        if (StringUtils.isBlank(data)) {
            throw new IllegalArgumentException(msg);
        }

        return data;
    }

    public static String getNotBlankOrDefault(Supplier<String> supplier, String def) {
        return ObjectUtils.getOrDefault(supplier, s -> StringUtils.isNotBlank(s), def);
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }


    /**
     * @param args void
     * @author lihong 2016年7月19日 上午9:28:55
     * @since v2.0
     */
    public static void main(String[] args) {
        System.out.println(isAccount(null));
        System.out.println(StringUtils.isAnyEmpty(new String[]{}));
        System.out.println(StringUtils.isAnyEmpty(new String[]{""}));
        System.out.println(StringUtils.isAnyEmpty(null));
        System.out.println(StringUtils.isAnyEmpty(null, null));
        System.out.println(StringUtils.isAnyEmpty(""));
        System.out.println(StringUtils.isAnyEmpty("a"));


        List<Integer> list1 = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6});
        System.out.println(join(list1, null, "(", ")", ","));

        List<Integer> list2 = new ArrayList<>();
        System.out.println(join(list2, null, "(", ")", ","));

        List<Integer> list3 = Arrays.asList(new Integer[]{0});
        System.out.println(join(list3, null, "(", ")", ","));

        List<String> list4 = Arrays.asList(new String[]{"a", "b", "c", "d"});
        System.out.println(join(list4, i -> "'" + i + "'", "(", ")", ","));

        List<String> list5 = Arrays.asList(new String[]{"a", "b", "c", "d"});
        System.out.println(join(list5, null, "(", ")", " , "));

        List<String> list6 = Arrays.asList(new String[]{"a", "b", "c", "d"});
        System.out.println(join(list6, null, null, null, null));

        System.out.println(join(list6, i -> "'" + i + "'", "(", ")", ","));


        String a = "\n\r\n\r     bb  cc";
        System.out.println("before:" + a);
        System.out.println("after:" + deleteHeadWhitespace(a));
        String b = new String("abcdefgh");
        System.out.println(b == deleteHeadWhitespace(b));

    }
}
