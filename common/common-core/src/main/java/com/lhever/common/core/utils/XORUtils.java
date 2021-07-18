package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihong10 2019/4/2 15:17
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/2 15:17
 * @modify by reason:{原因}
 */
public class XORUtils {
    private final static Logger log = LoggerFactory.getLogger(XORUtils.class);
    // 16进制字符
    public static final String HEXSTR = "0123456789ABCDEF";


    /**
     * 根据输入得到加密后的字符
     *
     * @param data
     * @param key1
     * @param key2
     * @return 加密后的字符
     * @author lihong10 2019/4/2 15:11
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 15:11
     * @modify by reason:{原因}
     */
    public static String encrypt(String data, String key1, String key2) {
        // 先跟key1进行按位亦或操作
        String encrypted = bitwise(data, key1);
        // 再将返回值跟key2进行按位亦或操作
        if (StringUtils.isNotBlank(key2)) {
            encrypted = bitwise(encrypted, key2);
        }
        // 转换成十六进制字符
        String encoded = null;
        try {
            //UTF-8编码
            encoded = binaryToHexString(encrypted.getBytes(CommonConsts.CHARSET_UTF8));
        } catch (Exception e) {
            log.error("编码失败", e);
        }
        return encoded;
    }


    /**
     * 根据输入得到解密后的字符
     *
     * @param encoded
     * @param key1
     * @param key2
     * @return 解密后的字符
     * @author lihong10 2019/4/2 15:13
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 15:13
     * @modify by reason:{原因}
     */
    public static String decrypt(String encoded, String key1, String key2) {
        // 得到二进制字符串
        String decoded = null;
        try {
            //UTF-8编码解决乱码
            decoded = new String(hexStringToBinary(encoded), CommonConsts.CHARSET_UTF8);
        } catch (Exception e) {
            log.error("解密失败", e);
        }

        String decrypted = null;
        if (StringUtils.isNotBlank(key2)) {
            // 解密时先与key2进行位运算, 再与key1进行位运算
            decrypted = bitwise(bitwise(decoded, key2), key1);
        } else {
            decrypted = bitwise(decoded, key1);
        }

        return decrypted;
    }


    /**
     * 将二进制转换为十六进制字符
     *
     * @param bytes
     * @return 十六进制字符串
     * @author lihong10 2019/4/2 15:14
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 15:14
     * @modify by reason:{原因}
     */
    public static String binaryToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEXSTR.charAt((bytes[i] & 0xF0) >> 4));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEXSTR.charAt(bytes[i] & 0x0F));
        }
        return sb.toString();
    }

    /**
     * 按位异或操作
     *
     * @param str 输入值
     * @param key 密钥
     * @return 操作后的值
     * @author lihong10 2019/4/2 15:16
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 15:16
     * @modify by reason:{原因}
     */
    public static String bitwise(String str, String key) {
        // 得到字符数组
        char[] strChar = str.toCharArray();
        char[] keyChar = key.toCharArray();
        //Character是char的包装类型
        List<Character> list = new ArrayList<Character>();
        for (int i = 0, j = 0; i < strChar.length; i++) {
            if (j >= keyChar.length) {
                j = 0;
            }
            //按位亦或, 异或操作后是int型，必须强制转换成char型
            list.add((char) (strChar[i] ^ keyChar[j]));//解决乱码
            j++;
        }
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < list.size(); k++) {
            // 转换得到字符
            sb.append(String.format("%c", list.get(k)));
        }
        return sb.toString();
    }


    /**
     * 将十六进制字符串转换成二进制字节数组
     *
     * @param hexString 十六进制字符串
     * @return 二进制字节数组
     * @author lihong10 2019/4/2 15:17
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/2 15:17
     * @modify by reason:{原因}
     */
    public static byte[] hexStringToBinary(String hexString) {
        // hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            // 右移四位得到高位
            byte high = (byte) ((HEXSTR.indexOf(hexString.charAt(2 * i))) << 4);
            // 得到低位
            byte low = (byte) HEXSTR.indexOf(hexString.charAt(2 * i + 1));
            // 高低位做或运算
            bytes[i] = (byte) (high | low);
        }
        return bytes;
    }


    public static void main(String[] args) throws DecoderException {

        String key1 = StringUtils.getUuid();
        String key2 = StringUtils.getUuid();
        String a = "123123adasdasas飒飒!!#%^&**((";
        System.out.println("加密前: " + a);

        String prev = encrypt(a, key1, key2);
        System.out.println("加密后: " + prev);

        System.out.println("解密后: " + decrypt(prev, key1, key2));


        String result1 = null;
        System.out.println(result1 = encrypt(a, key1, null));
        System.out.println(decrypt(result1, key1, null));
        System.out.println(decrypt(result1, key1, null).equals(a));

    }
}

