package com.lhever.common.core.converter.bytes;

import com.lhever.common.core.utils.LogUtils;
import com.lhever.common.core.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

public class BytesConverter {

    public static byte[] toBytes(Object obj) {
        if (obj == null) {
            return null;
        }
        String canonicalName = obj.getClass().getCanonicalName();
        LogUtils.info(canonicalName);

        switch (canonicalName) {
            case "int":
            case "java.lang.Integer":
                Integer anInt = (int) (obj);
                return anInt == null ? null : Bytes.toBytes(anInt);
            case "java.lang.Long":
            case "long":
                Long aLong = (long) (obj);
                return aLong == null ? null : Bytes.toBytes(aLong);
            case "float":
            case "java.lang.Float":
                Float aFloat = (float) (obj);
                return aFloat == null ? null : Bytes.toBytes(aFloat);
            case "boolean":
            case "java.lang.Boolean":
                Boolean aBoolean = (boolean) (obj);
                return aBoolean == null ? null : Bytes.toBytes(aBoolean);
            case "byte":
            case "java.lang.Byte":
                Byte aByte = (byte) (obj);
                return aByte == null ? null : Bytes.toBytes(aByte);
            case "char":
            case "java.lang.Character":
                Character aChar = (char) (obj);
                return aChar == null ? null : Bytes.toBytes(aChar);
            case "double":
            case "java.lang.Double":
                Double aDouble = (double) (obj);
                return aDouble == null ? null : Bytes.toBytes(aDouble);
            case "short":
            case "java.lang.Short":
                Short aShort = (short) (obj);
                return aShort == null ? null : Bytes.toBytes(aShort);
            case "java.lang.String":
                String aString = (String) (obj);
                if (StringUtils.isBlank(aString)) {
                    return null;
                }
                byte[] bytes = null;
                try {
                    bytes = aString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtils.error("", e);
                }
                return bytes;
            default:
                throw new IllegalArgumentException(obj.getClass().getName() + "类型的对象不能直接转换成字节数组");
        }
    }

    public static Object toValue(Field field, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        if (field == null) {
            return null;
        }

        String canonicalName = field.getType().getCanonicalName();
        LogUtils.info(canonicalName);
        switch (canonicalName) {
            case "int":
            case "java.lang.Integer":
                return Bytes.toInt(bytes);
            case "java.lang.Long":
            case "long":
                return Bytes.toLong(bytes);
            case "float":
            case "java.lang.Float":
                return Bytes.toFloat(bytes);
            case "boolean":
            case "java.lang.Boolean":
                return Bytes.toBoolean(bytes);
            case "byte":
            case "java.lang.Byte":
                return bytes[0];
            case "char":
            case "java.lang.Character":
                String charString = null;
                try {
                    charString = new String(bytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtils.error("", e);
                }
                if (charString != null && charString.length() > 0) {
                    return charString.charAt(0);
                }
                return null;
            case "double":
            case "java.lang.Double":
                return Bytes.toDouble(bytes);
            case "short":
            case "java.lang.Short":
                return Bytes.toShort(bytes);
            case "java.lang.String":
                String aString = null;
                try {
                    aString = new String(bytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtils.error("", e);
                }
                return aString;
            default:
                throw new IllegalArgumentException("无法根据byte 数组还原" + canonicalName + "类型的对象");
        }
    }


}
