package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Utils {
    private static final Logger log = LoggerFactory.getLogger(Base64Utils.class);
    private static final int TYPE_URL = 0;
    private static final int TYPE_MIME = 10;
    private static final int TYPE_RFC = 20;
    private static final String ENCODING = "UTF-8";
    private static final String EMPTY_STRING = "";

    private static Base64.Encoder getEncoder(int type) {
        Base64.Encoder encoder = null;
        switch (type) {
            case TYPE_URL:
                encoder = Base64.getUrlEncoder();
                break;
            case TYPE_MIME:
                encoder = Base64.getMimeEncoder();
                break;
            case TYPE_RFC:
                encoder = Base64.getEncoder();
                break;
            default:
                encoder = Base64.getEncoder();
                break;

        }
        return encoder;

    }

    private static Base64.Decoder getDecoder(int type) {
        Base64.Decoder decoder = null;
        switch (type) {
            case TYPE_URL:
                decoder = Base64.getUrlDecoder();
                break;
            case TYPE_MIME:
                decoder = Base64.getMimeDecoder();
                break;
            case TYPE_RFC:
                decoder = Base64.getDecoder();
                break;
            default:
                decoder = Base64.getDecoder();
                break;
        }

        return decoder;
    }

    public static String encode(byte[] bytes) {
        return encode(bytes, TYPE_RFC);
    }


    public static String encode(byte[] bytes, int type) {
        if (bytes == null) {
            return null;
        }

        if (bytes.length == 0) {
            return EMPTY_STRING;
        }

        String result = null;

        try {
            result = getEncoder(type).encodeToString(bytes);
        } catch (Exception e) {
            log.error("", e);
        }

        return result;
    }


    public static String encode(String content, String encoding, int type) {
        byte[] bytes = toBytes(content, encoding);

        return encode(bytes, type);
    }

    public static String encode(String content, String encoding) {
        return encode(content, encoding, TYPE_RFC);
    }

    public static String encode(String content) {
        return encode(content, ENCODING, TYPE_RFC);
    }


    public static byte[] decode(String encoded) {
        return decode(encoded, TYPE_RFC, ENCODING);
    }


    public static byte[] decode(String encoded, String encoding) {
        return decode(encoded, TYPE_RFC, encoding);
    }


    public static byte[] decode(String encoded, int type, String encoding) {
        if (StringUtils.isBlank(encoded)) {
            return null;
        }

        byte[] bytes = toBytes(encoded, encoding);

        return decode(bytes, type);

    }


    public static byte[] decode(byte[] encoded, int type) {
        if (encoded == null || encoded.length == 0) {
            return null;
        }

        byte[] result = null;

        try {
            result = getDecoder(type).decode(encoded);
        } catch (Exception e) {

            log.error("", e);
        }

        return result;
    }

    public static byte[] decode(byte[] encoded) {
        return decode(encoded, TYPE_RFC);
    }


    private static byte[] toBytes(String content, String encoding) {
        if (content == null) {
            return null;
        }

        if (StringUtils.isBlank(content)) {
            return new byte[0];
        }

        encoding = StringUtils.isBlank(encoding) ? ENCODING : encoding;

        byte[] bytes = null;
        try {
            bytes = content.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }

        return bytes;
    }


}
