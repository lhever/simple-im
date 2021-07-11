package com.lhever.common.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 利用枚举定义上下文工具类
 */
public enum ContextUtils {
    //INSTANCE是ContextUtils的唯一实例
    INSTANCE;

    private ContextUtils() {
    }

    private ApplicationContext context;

    static {
        ContextUtils[] values = ContextUtils.values();
        if (values.length != 1) {
            throw new IllegalArgumentException("ContextUtils 是单列工具类， 不允许有多个枚举实例");
        }
    }

    public static void setApplicationContext(ApplicationContext context) {
        if (context == null) {
            throw new IllegalArgumentException("ApplicationContext null");
        }
        INSTANCE.context = context;
    }

    public static ApplicationContext getContext() {
        return INSTANCE.context;
    }

    public final static Object getBean(String beanName) {
        return INSTANCE.context.getBean(beanName);
    }

    public final static Object getBean(String beanName, Class<?> requiredType) {
        return INSTANCE.context.getBean(beanName, requiredType);
    }

    public final static <T> T getBean(Class<T> requiredType, String beanName) {
        return INSTANCE.context.getBean(beanName, requiredType);
    }

    public final static <T> T getBean(Class<T> requiredType) {
        return INSTANCE.context.getBean(requiredType);
    }

    public final static boolean containsBean(String beanName) {
        return INSTANCE.context.containsBean(beanName);
    }

    public static String[] getBeanNamesForType(@Nullable Class<?> type) {
        return INSTANCE.context.getBeanNamesForType(type);
    }

    public static <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) {
        return INSTANCE.context.getBeansOfType(type);
    }







}
