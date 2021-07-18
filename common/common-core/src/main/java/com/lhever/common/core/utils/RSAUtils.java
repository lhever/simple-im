package com.lhever.common.core.utils;


import com.lhever.common.core.support.tuple.TwoPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * {@code RSAUtils}: 该类是RSA加密的工具类
 *
 * @author lihong10 2018/9/4 15:35:00
 * return
 */

public class RSAUtils {

    private static final Logger log = LoggerFactory.getLogger(RSAUtils.class);

    //    private static Cipher clipher = null;
    public static String ALGORITHM = "RSA";
    public static int SIZE = 1024;
    private static final String CHARSET = "UTF-8";
//    private static Lock lock = new ReentrantLock();

    /*static {
        try {
            clipher = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            log.error("加解密算法" + ALGORITHM + "不存在",e);
            throw new CryptoException("加解密算法" + ALGORITHM + "不存在");
        }
    }*/


    public static TwoPair<RSAPublicKey, RSAPrivateKey> getKeyPair() {
        try {
            KeyPairGenerator keyPairGeno = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGeno.initialize(SIZE);
            KeyPair keyPair = keyPairGeno.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            TwoPair<RSAPublicKey, RSAPrivateKey> pair = new TwoPair<>(publicKey, privateKey);
            return pair;
        } catch (Exception e) {
            log.error("generate rsa key pair error", e);
        }

        return null;
    }


    public static String getPublicKeyBase64Str(RSAPublicKey key) {
        byte[] bytes = key.getEncoded();
        return Base64Utils.encode(bytes);
    }

    public static byte[] getPublicKeyBytes(RSAPublicKey key) {
        byte[] bytes = key.getEncoded();
        return bytes;
    }

    public static String getPrivateKeyBase64Str(RSAPrivateKey key) {
        byte[] bytes = key.getEncoded();
        return Base64Utils.encode(bytes);
    }

    public static byte[] getPrivateKeyBytes(RSAPrivateKey key) {
        byte[] bytes = key.getEncoded();
        return bytes;
    }

    /**
     * 通过公钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }


    /**
     * 通过私钥byte[]将私钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    /**
     * 私钥解密，待解密字符串, 解密过程不分组
     *
     * @param base64Str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decodeByPrivateKeyNoGroup(String base64Str, PrivateKey privateKey) {
        String res = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            res = new String(cipher.doFinal(Base64Utils.decode(base64Str)), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("decodeByPrivateKeyNoGroup error", e);
        }

        return res;
    }

    /**
     * 公钥加密, 加密过程不分组
     *
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encodeByPublicKeyAndBase64NoGroup(String str, PublicKey publicKey) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String msg = null;

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] temp = cipher.doFinal(StringUtils.stringToByteArray(str, null));
            msg = Base64Utils.encode(temp);
        } catch (Exception e) {
            throw new RuntimeException("encodeByPublicKeyAndBase64NoGroup error", e);
        }
        return msg;
    }


    public static byte[] decodeByPrivateKey(String base64Str, PrivateKey privateKey) {
        return decryptWidthGroup(Base64Utils.decode(base64Str), privateKey);
    }

    public static byte[] decodeByPublickey(String base64Str, PublicKey publicKey) {
        return decryptWidthGroup(Base64Utils.decode(base64Str), publicKey);
    }

    public static String decodeToStringByPrivateKey(String base64Str, PrivateKey privateKey) {
        byte[] bytes = decryptWidthGroup(Base64Utils.decode(base64Str), privateKey);
        String result = null;
        try {
            result = new String(bytes, CHARSET);
        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }

    public static String decodeToStringByPublickey(String base64Str, PublicKey publicKey) {
        byte[] bytes = decryptWidthGroup(Base64Utils.decode(base64Str), publicKey);


        return bytesToString(bytes, CHARSET);

    }


    private static String bytesToString(byte[] bytes, String charset) {
        charset = StringUtils.isBlank(charset) ? CHARSET : charset;
        String result = null;
        try {
            result = new String(bytes, charset);
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    public static byte[] encodeByPrivateKey(String message, PrivateKey privateKey) {
        byte[] bytes = StringUtils.stringToByteArray(message, null);
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        return encryptWithGroup(bytes, privateKey);
    }

    public static byte[] encodeByPublickey(String message, PublicKey publicKey) {
        byte[] bytes = StringUtils.stringToByteArray(message, null);
        if (bytes == null || bytes.length == 0) {
            return bytes;
        }
        return encryptWithGroup(bytes, publicKey);
    }

    public static String encodeToStringByPrivateKey(String message, PrivateKey privateKey) {
        byte[] bytes = StringUtils.stringToByteArray(message, null);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        byte[] result = encryptWithGroup(bytes, privateKey);
        return Base64Utils.encode(result);
    }

    public static String encodeToStringByPublickey(String message, PublicKey publicKey) {
        byte[] bytes = StringUtils.stringToByteArray(message, null);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        byte[] result = encryptWithGroup(bytes, publicKey);
        return Base64Utils.encode(result);
    }


    /**
     * 分组加密
     *
     * @param dataBytes 数据
     * @return 加密后的密文
     */
    public static byte[] encryptWithGroup(byte[] dataBytes, Key key) {
        // 加密数据长度 <= 模长-11
        int maxBlockSize = ((RSAKey) key).getModulus().bitLength() / 8 - 11;
        final int inputLen = dataBytes.length;

//        lock.lock();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Cipher clipher = Cipher.getInstance(ALGORITHM);
            clipher.init(Cipher.ENCRYPT_MODE, key);
            int offSet = 0;
            byte[] cache;
            // 剩余长度
            int remainLength = inputLen;
            // 对数据分段加密
            while (remainLength > 0) {
                cache = clipher.doFinal(dataBytes, offSet, Math.min(remainLength, maxBlockSize));
                out.write(cache, 0, cache.length);

                offSet += maxBlockSize;
                remainLength = inputLen - offSet;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//            lock.unlock();
        }
    }

    /**
     * 分组解密
     *
     * @param encryptedData 密文数据
     * @return 解密后的数据
     */
    public static byte[] decryptWidthGroup(byte[] encryptedData, Key key) {
        // 模长
        final int maxBlockSize = ((RSAKey) key).getModulus().bitLength() / 8;
        int inputLen = encryptedData.length;
//        lock.lock();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Cipher clipher = Cipher.getInstance(ALGORITHM);
            clipher.init(Cipher.DECRYPT_MODE, key);
            int offSet = 0;
            byte[] cache;
            // 剩余长度
            int remainLength = inputLen;
            // 对数据分段解密
            while (remainLength > 0) {
                cache = clipher.doFinal(encryptedData, offSet, Math.min(remainLength, maxBlockSize));
                out.write(cache, 0, cache.length);

                offSet += maxBlockSize;
                remainLength = inputLen - offSet;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
//            lock.unlock();
        }
    }


    public static void main(String[] args) throws Exception {

        TwoPair<RSAPublicKey, RSAPrivateKey> keyPair = getKeyPair();
        RSAPublicKey rSAPublicKey = keyPair.first;
        RSAPrivateKey rSAPrivateKey = keyPair.second;

        String publicKeyBase64Str = getPublicKeyBase64Str(rSAPublicKey);

        log.info("公钥Base64加密后的字符串： ");
        log.info(publicKeyBase64Str);


        String privateKeyBase64Str = getPrivateKeyBase64Str(rSAPrivateKey);
        log.info("私钥Base64加密后的字符串： ");
        log.info(privateKeyBase64Str);


        String msg = "admin";

        System.out.println("待加密的字符串是 \n\t:" + msg);

        System.out.println("md5加密后的内容是\n\t：   " + StringUtils.md5(msg));

        String encoded = encodeToStringByPublickey(msg, getPublicKey(Base64Utils.decode(publicKeyBase64Str)));
        System.out.println("加密后的内容是\n\t：   " + encoded);

        String decoded = decodeToStringByPrivateKey(encoded, getPrivateKey(Base64Utils.decode(privateKeyBase64Str)));
        System.out.println("解密后的内容是\n\t" + decoded);

        System.out.println(decoded.equals(msg));

//        test();

    }


    public static void test() throws Exception {
        System.out.println("测试 --- 张鑫");
        String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTG2tzLEWiAI45/z+kIaG6Eg2qybSDFb0a5eyKqpylnTW/kowo9UZjfEVdUnSAPRwvRos3pR+fBNRdmi9N+WBxDpKC91oqj+UlDXjbJK+MRm7t5XM1isHf5BV1B1BJ79hLpStpaawrh5EUX7v9qRx1SyY3mWu6IgxrIlPMf8MOrwIDAQAB";
        String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJMba3MsRaIAjjn/P6QhoboSDarJtIMVvRrl7IqqnKWdNb+SjCj1RmN8RV1SdIA9HC9GizelH58E1F2aL035YHEOkoL3WiqP5SUNeNskr4xGbu3lczWKwd/kFXUHUEnv2EulK2lprCuHkRRfu/2pHHVLJjeZa7oiDGsiU8x/ww6vAgMBAAECgYEAjsXP9UoEkwSjBRr8M1oI0sRuy1FUhtz0WkTI3JRV0E7SOYoLyjyzAh88WySJuWSWFDQNLgZuuDFBw06/lpfvxo5DUmW6IVyL+XI3hT9fXknOvbv457Qsvu1cSnSVBwcHeaM1ng6BobkyHPEpbp1NcJgxo2ixbixtKK/PEkJkgeECQQDeYL+RsMz67aVpbIQPsciFyaO1QzgIeg+yQxbqYvWQTqzAc7UknbUVSUdvbF6V4UoThsexLnjLFnRz7N2tLNxnAkEAqVlGn/I/+RDQgYDwrAXIi5peTyrpjLoe3MwlZG7v4d+7bST61vbHq8+xTKl8ziIu9CiWST/HgJBG8pEScazOeQJAHuskt4T1ew3/qaDE28nEL5XRhSpLaCJQfGCHlx8bNW0656cu0GB9BPCjVgKjRAOG4SAkM6ZadSIyHswYJjPOkQJAfutO1/753ROyAOz+8x3egGowNksmAHdZPAewGoZeOD2yvYkSidsoKGcfLSJ1TBQodoqYn8syHPOEiLUbSqDFmQJAfxuBekKxfNUbjewlrxPYgQoSkmbSqpQEhnRtnLMyuLSesN+rS5gOmN3EQGHyXd5iBu2unirlGvl+0hz4VMeJ0A==";

        String msg1 = "H5G5YI4FHiqI8We2AzcpLUUUMFYqLy42qLgnM0ByDKjZlFsKoLvOFYDi22j7Wo55PxWyD35rG6ty3pBqZ9P+xeWtvTrL3Jy/s0Mb4AAH3gfJ/vgwlw+KTJ2gD4I0nnrGOREkSn0rD0AHPeE4ieNvBdWWHPHPYZKqb1drK/0Saek=";
        String msg2 = "XayazzU/YU6gKjcQtkICZVNjV5ROPxR1V04eDaX3aonJ8Ef7mr1Iw/wle3HOCC+XiEqzheaJVWYVRf4y5D+bgJ8gemHzoCu9E0uCuZp1IIxsyNNod79BbduThewmciW2Nl8xZFExaOMccucdTEca7raLBna4DrQ5CmyWelLusiM=";

        String decodedmsg1 = decodeToStringByPrivateKey(msg1, getPrivateKey(Base64Utils.decode(privateKeyBase64)));
        System.out.println(decodedmsg1);

        String decodedmsg2 = decodeToStringByPrivateKey(msg2, getPrivateKey(Base64Utils.decode(privateKeyBase64)));
        System.out.println(decodedmsg1);

    }


}