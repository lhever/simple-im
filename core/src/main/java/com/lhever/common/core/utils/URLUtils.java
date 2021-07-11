package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class URLUtils {

    private static final Logger log = LoggerFactory.getLogger(URLUtils.class);

    private final static String CHAR_SET = "UTF-8";


    public static String urlDecode(String str) throws Exception {
        return urlDecode(str, CHAR_SET);
    }

    public static String urlDecode(String str, String charset) throws Exception {
        if (str == null) {
            return null;
        }

        charset = StringUtils.isBlank(charset) ? CHAR_SET : charset;
        String result = java.net.URLDecoder.decode(str, charset);

        return result;
    }

    public static String urlDecodeSafely(String str) {
        return urlDecodeSafely(str, CHAR_SET);
    }

    public static String urlDecodeSafely(String str, String charset) {
        if (str == null) {
            return null;
        }
        charset = StringUtils.isBlank(charset) ? CHAR_SET : charset;
        String result = null;
        try {
            result = java.net.URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            log.error("url decode error: unsupported encoding. ", e);
        } catch (Throwable e) {
            log.error("url decode error: ", e);
        }
        return result;
    }


    public static String urlEncode(String str) throws Exception {
        return urlEncode(str, CHAR_SET);
    }

    public static String urlEncode(String str, String charset) throws Exception {
        if (null == str) {
            return null;
        }
        charset = StringUtils.isBlank(charset) ? CHAR_SET : charset;
        String result = java.net.URLEncoder.encode(str, charset);

        return result;
    }

    public static String urlEncodeSafely(String str) {
        return urlEncodeSafely(str, CHAR_SET);
    }

    public static String urlEncodeSafely(String str, String charset){
        if (null == str) {
            return null;
        }
        charset = StringUtils.isBlank(charset) ? CHAR_SET : charset;
        String result = null;
        try {
            result = java.net.URLEncoder.encode(str, charset);
        }catch (UnsupportedEncodingException e) {
            log.error("url encode error: unsupported encoding. ", e);
        } catch (Throwable e) {
            log.error("url encode error: ", e);
        }
        return result;
    }


}
