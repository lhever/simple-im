package com.lhever.simpleim.server.config;

import com.lhever.common.core.config.YamlPropertiesReader;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.YamlUtils;
import com.lhever.simpleim.common.consts.ImConsts;
import com.lhever.simpleim.common.util.KafkaUtils;

import java.util.Map;

public class ServerConfig {

    public static final String CONFIG_FILE_NAME = "/server-config.yml";

    private static YamlPropertiesReader reader = new YamlPropertiesReader(CONFIG_FILE_NAME, false);

    public static final String SERVER_IP = reader.getProperty("server.ip", ImConsts.SERVER_IP);

    public static final Integer SERVER_PORT = reader.getIntProperty("server.port", ImConsts.SERVER_PORT);

    public static final Integer LOGIN_TIMEOUT_SECONDS = reader.getIntProperty("server.loginTimeOutSeconds", 6);

    public static final KafkaUtils.KafkaProp kafkaProp;


    public static final String ZK_IP = reader.getProperty("zookeeper.ip", "127.0.0.1");
    public static final Integer ZK_PORT = reader.getIntProperty("zookeeper.port", 2181);
    public static final String ZK_ADDRESS = reader.getProperty("zookeeper.address", "127.0.0.1:2181");
    public static final String ZK_NAMESPACE = reader.getProperty("zookeeper.namespace", "im");
    public static final String ZK_ROOTPATH = reader.getProperty("zookeeper.rootPath", "/root");

    public static final String REDIS_IP = reader.getProperty("redis.ip", "127.0.0.1");
    public static final Integer REDIS_PORT = reader.getIntProperty("redis.port", 6379);
    public static final String REDIS_PWD = reader.getProperty("redis.pwd", null);

    public static final String KAFKA_ADDRESS = reader.getProperty("kafka.address", null);

    static {
        Map<String, Object> map = YamlUtils.yaml2Map(CONFIG_FILE_NAME);
        Map kafka = CollectionUtils.getValue(map, "kafka", Map.class);
        String cs = JsonUtils.object2Json(kafka);
        kafkaProp = JsonUtils.json2Object(cs, KafkaUtils.KafkaProp.class);
    }










}
