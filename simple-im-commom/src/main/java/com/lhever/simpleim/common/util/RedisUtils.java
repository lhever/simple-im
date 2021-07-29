package com.lhever.simpleim.common.util;

import com.lhever.common.core.support.jedis.CustomJedisClient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RedisUtils {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RedisProp {
        private String host;
        private Integer port;
        private String pass;
        private int timeout;
        private int maxIdle;
        private int maxTotal;
        private long maxWaitMillis;
        private boolean testOnBorrow;

        public boolean getTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }
    }

    private static CustomJedisClient jedisClient;


    public static void init(RedisProp prop) {
        jedisClient = new CustomJedisClient(prop.getHost(), prop.getPort(), prop.getPass(),
                prop.getTimeout(), prop.getMaxIdle(), prop.getMaxTotal(), prop.getMaxWaitMillis(), prop.getTestOnBorrow());
    }

    public static void set(String key, String value, long cacheSeconds) {
        jedisClient.set(key, value, cacheSeconds);
    }

    public static String get(String key) {
        String value = jedisClient.get(key);
        return value;
    }




}
