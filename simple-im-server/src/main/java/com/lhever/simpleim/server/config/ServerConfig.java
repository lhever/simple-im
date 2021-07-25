package com.lhever.simpleim.server.config;

import com.lhever.common.core.config.YamlPropertiesReader;
import com.lhever.simpleim.common.consts.ImConsts;

public class ServerConfig {

    private static YamlPropertiesReader reader = new YamlPropertiesReader("/server-config.yml",false);

    public static final String SERVER_IP = reader.getProperty("server.ip", ImConsts.SERVER_IP);

    public static final Integer SERVER_PORT = reader.getIntProperty("server.port", ImConsts.SERVER_PORT);


    public static final String ZK_IP = reader.getProperty("zookeeper.ip", "127.0.0.1");
    public static final Integer ZK_PORT = reader.getIntProperty("zookeeper.port", 2181);
    public static final String ZK_ADDRESS = reader.getProperty("zookeeper.address", "127.0.0.1:2181");
    public static final String ZK_NAMESPACE = reader.getProperty("zookeeper.namespace", "im");
    public static final String ZK_ROOTPATH = reader.getProperty("zookeeper.rootPath", "/root");

    public static final String REDIS_IP = reader.getProperty("redis.ip", "127.0.0.1");
    public static final Integer REDIS_PORT = reader.getIntProperty("redis.port", 6379);
    public static final String REDIS_PWD = reader.getProperty("redis.pwd", null);

    public static final String LOGIN_KEY = "im.login.uid";










}
