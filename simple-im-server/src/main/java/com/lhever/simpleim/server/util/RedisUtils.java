package com.lhever.simpleim.server.util;

import com.lhever.common.core.support.jedis.CustomJedisClient;
import com.lhever.simpleim.server.config.ServerConfig;

public class RedisUtils {

    private static CustomJedisClient jedisClient;

    static {
        init();
    }


    public static  void init() {
        jedisClient = new CustomJedisClient(ServerConfig.REDIS_IP,
                ServerConfig.REDIS_PORT, ServerConfig.REDIS_PWD, 20000, 5, 20, 20000, true);
    }

    public static void set(String key, String value, long cacheSeconds) {
        jedisClient.set(key, value, cacheSeconds);
    }




}
