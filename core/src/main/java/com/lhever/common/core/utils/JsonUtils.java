package com.lhever.common.core.utils;

import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lhever.common.core.support.jackson.CustomDateDeSerializer;
import com.lhever.common.core.support.jackson.CustomDateSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        objectMapper.setDateFormat(smt);

        //自定义日期序列化、反序列化类
        SimpleModule serializerModule = new SimpleModule("DateSerializer", PackageVersion.VERSION);
        serializerModule.addSerializer(Date.class, new CustomDateSerializer());
        serializerModule.addDeserializer(Date.class, new CustomDateDeSerializer());
        objectMapper.registerModule(serializerModule);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
        objectMapper.setTimeZone(timeZone);

    }

    public static String toJsonWithFormat(Object obj, String dateFormat) {
        if (obj == null) {
            return null;
        }
        String result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //备份老的日期格式
            //DateFormat oldFormat = objectMapper.getDateFormat();
            if (StringUtils.isNotBlank(dateFormat)) {
                objectMapper.setDateFormat(new SimpleDateFormat(dateFormat));
                //不设置时区，会与系统当前时间相差8小时
                TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
                objectMapper.setTimeZone(timeZone);

                SimpleModule serializerModule = new SimpleModule("DateSerializer", PackageVersion.VERSION);
                serializerModule.addSerializer(Date.class, new CustomDateSerializer());
                serializerModule.addDeserializer(Date.class, new CustomDateDeSerializer());
                objectMapper.registerModule(serializerModule);
            }
            result = objectMapper.writeValueAsString(obj);
            //恢复日期格式
            //objectMapper.setDateFormat(oldFormat);
        } catch (Exception e) {
        }
        return result;
    }

    public static String object2Json(Object obj) {
        if (obj == null) {
            return null;
        }
        String result = null;
        try {
            result = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            LOG.error("对象转JSON字符串异常", e);
        }
        return result;
    }

    public static String object2Json(Object obj, boolean indented) {

        if (obj == null) {
            return null;
        }
        String result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (indented) {
                result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } else {
                result = objectMapper.writeValueAsString(obj);
            }
        } catch (Exception e) {
            LOG.error("error when object to json", e);
        }
        return result;
    }

    public static Map<?, ?> jsonToMap(String json) {
        return json2Object(json, Map.class);
    }

    public static <T> T json2Object(String json, Class<T> cls) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        T result = null;
        try {
            result = objectMapper.readValue(json, cls);
        } catch (Exception e) {
            LOG.error("JSON字符串转对象异常", e);
        }

        return result;
    }


    public static <T> T json2Object(String json, TypeReference<T> reference) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        T result = null;
        try {
            result = objectMapper.readValue(json, reference);
        } catch (Exception e) {
            LOG.error("JSON字符串转对象异常", e);
        }

        return result;
    }

    public static <T> T json2Object(String json, Class<T> parametrized, Class<?>... parameterClasses) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        T result = null;
        try {
            JavaType javaType = objectMapper
                    .getTypeFactory()
                    .constructParametricType(parametrized, parameterClasses);

            result = objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            LOG.error("JSON字符串转对象异常", e);
        }

        return result;
    }


    public static <T> T conveterObject(Object srcObject, Class<T> destObjectType) {
        String jsonContent = object2Json(srcObject);
        return json2Object(jsonContent, destObjectType);
    }

    public static <T> List<T> fromJsonList(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }

}
