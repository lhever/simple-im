package com.lhever.simpleim.client.config;

import com.lhever.common.core.config.YamlPropertiesReader;
import com.lhever.simpleim.common.consts.ImConsts;

public class ClientConfig {

    private static YamlPropertiesReader reader = new YamlPropertiesReader("/client-config.yml", false);


    public static final  String USER_NAME = reader.getProperty("client.userName", "lhever");
    public static final  String PASS_WORD = reader.getProperty("client.passWord", "123456");


    public static final String CLIENT_IP = reader.getProperty("client.ip", ImConsts.CLIENT_IP);
    public static final Integer CLIENT_PORT = reader.getIntProperty("client.port", ImConsts.CLIENT_PORT);
    public static final Boolean ENABLE_DISCOVERY = reader.getBooleanProperty("client.enableDiscovery", false);


    public static final String SERVER_IP = reader.getProperty("server.ip", ImConsts.SERVER_IP);

    public static final Integer SERVER_PORT = reader.getIntProperty("server.port", ImConsts.SERVER_PORT);


    public static final String DISCOVERY_IP = reader.getProperty("discovery.ip", "127.0.0.1");
    public static final Integer DISCOVERY_PORT = reader.getIntProperty("discovery.port", 444);
    public static final String DISCOVERY_CONTEXT = reader.getProperty("discovery.context", "/gateway");


    public static final String ZK_IP = reader.getProperty("zookeeper.ip", "127.0.0.1");
    public static final Integer ZK_PORT = reader.getIntProperty("zookeeper.port", 2181);
    public static final String ZK_ADDRESS = reader.getProperty("zookeeper.address", "127.0.0.1:2181");
    public static final String ZK_NAMESPACE = reader.getProperty("zookeeper.namespace", "im");
    public static final String ZK_ROOTPATH = reader.getProperty("zookeeper.rootPath", "/root");


}
