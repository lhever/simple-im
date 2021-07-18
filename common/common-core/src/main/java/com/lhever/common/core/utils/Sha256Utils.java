package com.lhever.common.core.utils;

//import org.apache.commons.codec.binary.Hex;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * {@code Sha256Utils}： sha256加密工具类
 *
 * @author lihong10 2018/9/4 14:29:00
 * return
 */

public class Sha256Utils {

    private static final Logger log = LoggerFactory.getLogger(Sha256Utils.class);
    private static final String ENCODING = "UTF-8";


    /**
     * SHA 256加密， 十六进制编码
     *
     * @param str
     * @return
     */
    public static String getSHA256HexStr(String str) {
        MessageDigest messageDigest;
        String encoded = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(stringToByteArray(str, ENCODING));
            encoded = Hex.encodeHexString(messageDigest.digest());
        } catch (Exception e) {
            log.error("sha256 encoding error ", e);
        }
        return encoded;
    }


    /**
     * @param message 待加密内容的字节数组
     * @param secret
     * @return
     */
    public static byte[] getHmacSha256Bytes(byte[] message, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(stringToByteArray(secret, ENCODING), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] hmacBytes = sha256_HMAC.doFinal(message);
            return hmacBytes;
        } catch (Exception e) {
            log.error("get hmacSha256Bytes error", e);
        }

        return null;
    }

    private static byte[] stringToByteArray(String msg, String encoding) {
        if (StringUtils.isBlank(msg)) {
            return null;
        }

        encoding = StringUtils.isBlank(encoding) ? ENCODING : encoding;

        byte[] bytes = null;
        try {
            bytes = msg.getBytes(encoding);
        } catch (Exception e) {
            log.error("string to bytes error", e);
        }
        return bytes;
    }

    /**
     * HAMC-SHA256加密， BASE64编码
     *
     * @param message
     * @param secret
     * @param encoding
     * @return
     */
    public static String getHmacSHA256Base64String(String message, String secret, String encoding) {
        byte[] msgBytes = stringToByteArray(message, encoding);
        if (msgBytes == null || msgBytes.length == 0) {
            return null;
        }


        byte[] hmacSha256Bytes = getHmacSha256Bytes(msgBytes, secret);
        if (hmacSha256Bytes == null || hmacSha256Bytes.length == 0) {
            return null;
        }
        return Base64Utils.encode(hmacSha256Bytes);
    }

    /**
     * HAMC-SHA256加密， BASE64编码
     *
     * @param message
     * @param secret
     * @return
     */
    public static String getHmacSHA256Base64String(String message, String secret) {
        return getHmacSHA256Base64String(message, secret, ENCODING);
    }

    /**
     * HAMC-SHA256加密， 十六进制编码
     *
     * @param message
     * @param secret
     * @param encoding
     * @return
     */
    public static String getHmacSHA256HexString(String message, String secret, String encoding) {
        byte[] msgBytes = stringToByteArray(message, encoding);
        if (msgBytes == null || msgBytes.length == 0) {
            return null;
        }

        byte[] hmacSha256Bytes = getHmacSha256Bytes(msgBytes, secret);
        if (hmacSha256Bytes == null || hmacSha256Bytes.length == 0) {
            return null;
        }
        return Hex.encodeHexString(hmacSha256Bytes);
    }

    /**
     * HAMC-SHA256加密， 十六进制编码
     *
     * @param message
     * @param secret
     * @return
     */
    public static String getHmacSHA256HexString(String message, String secret) {
        return getHmacSHA256HexString(message, secret, ENCODING);
    }


    public static void main(String[] args) {

        System.out.println(getHmacSHA256HexString("key", "key"));


        System.out.println(getHmacSHA256HexString("Message to hash", "key"));
        System.out.println(getHmacSHA256HexString("我是中国人1234&&&", "hikvision"));


        System.out.println(getSHA256HexStr("The quick brown fox jumps over the lazy dog"));
        System.out.println(getSHA256HexStr("The quick brown fox jumps over the lazy dog").
                equals("d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592"));

    }


}
