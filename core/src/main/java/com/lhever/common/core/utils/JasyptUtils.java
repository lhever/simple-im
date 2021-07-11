package com.lhever.common.core.utils;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptUtils {

    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
    private static final String ENCRYPTED_VALUE_SUFFIX = ")";

    private static SimpleStringPBEConfig getDefaultConfig(String salt) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        // jasypt.encryptor.password
        config.setPassword(salt); //设置盐值
        //jasypt.encryptor.algorithm
        config.setAlgorithm("PBEWithMD5AndDES");
        //jasypt.encryptor.keyObtentionIterations
        config.setKeyObtentionIterations(1000);
        //jasypt.encryptor.poolSize
        config.setPoolSize("1");
        //jasypt.encryptor.providerName
        config.setProviderName((String) null);
        //jasypt.encryptor.providerClassName
        config.setProviderClassName((String) null);
        //jasypt.encryptor.saltGeneratorClassname
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        //jasypt.encryptor.stringOutputType
        config.setStringOutputType("base64");

        return config;

    }


    public static String decrypt(String salt, String encryptPassword) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(getDefaultConfig(salt));
        String decrypt = encryptor.decrypt(encryptPassword);
        return decrypt;
    }

    public static String encrypt(String salt, String pwd) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(getDefaultConfig(salt));
        String encrypt = encryptor.encrypt(pwd);
        return encrypt;
    }

    public static String encryptAndWrap(String salt, String pwd) {
        return ENCRYPTED_VALUE_PREFIX + encrypt(salt, pwd) + ENCRYPTED_VALUE_SUFFIX;
    }

    public static boolean isEncryptedValue(final String value) {
        if (value == null) {
            return false;
        }
        final String trimmedValue = value.trim();
        return (trimmedValue.startsWith(ENCRYPTED_VALUE_PREFIX) &&
                trimmedValue.endsWith(ENCRYPTED_VALUE_SUFFIX));
    }

    public static String getInnerEncryptedValue(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        value = value.trim();
        if (isEncryptedValue(value)) {
            return value.substring(
                    ENCRYPTED_VALUE_PREFIX.length(),
                    (value.length() - ENCRYPTED_VALUE_SUFFIX.length()));

        } else {
            return value;
        }
    }

}
