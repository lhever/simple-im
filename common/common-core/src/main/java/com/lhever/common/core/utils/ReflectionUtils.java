package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T> T newInstance(String className, Class<?> instanceType, ClassLoader classLoader) {
        T instance = null;
        try {
            Class<?> connectorClass = classLoader.loadClass(className).asSubclass(instanceType);
            instance = (T) connectorClass.newInstance();
        } catch (Throwable t) {
            LOG.error("fail to create instance", t);
        }
        return instance;
    }
}
