package com.lhever.common.core.utils;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.MessageDigest;

public class Md5Utils {
    private static Logger logger = LoggerFactory.getLogger(Md5Utils.class);

    /**
     * MD5加密
     *
     * @param content
     * @return String
     * @throws Exception String
     * @author lihong 2016年1月1日 下午5:52:42
     * @since v1.0
     */
    public static String md5(String content) throws Exception {

        if (StringUtils.isBlank(content)) {
            return null;
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(content.getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer md5 = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            md5.append(Character.forDigit((digest[i] & 0xF0) >> 4, 16));
            md5.append(Character.forDigit((digest[i] & 0xF), 16));
        }

        String endoded = md5.toString();
        return endoded;
    }

    public static String md5Quitely(String content) {
        try {
            return md5(content);
        } catch (Exception e) {
            logger.error("md5 error", e);
        }

        return null;

    }



    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String md5(File file) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);
        return md5(fis, true);
    }


    /**
     * 获取一个文件的md5值(可处理大文件)
     * @return md5 value
     */
    public static String md5(InputStream fis, boolean close) {
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            logger.error("md5 error", e);
        } finally {
            if (close) {
                IOUtils.closeQuietly(fis);
            }
        }
        return null;
    }


}
