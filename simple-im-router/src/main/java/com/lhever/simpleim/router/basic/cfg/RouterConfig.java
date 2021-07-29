package com.lhever.simpleim.router.basic.cfg;

import com.lhever.common.core.config.YamlPropertiesReader;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.YamlUtils;
import com.lhever.simpleim.common.util.KafkaUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/23 22:09
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/23 22:09
 * @modify by reason:{方法名}:{原因}
 */
public class RouterConfig {

    public static final String CONFIG_FILE_NAME = "/router-config.yml";
    private final static YamlPropertiesReader reader = new YamlPropertiesReader(CONFIG_FILE_NAME,false);

    public static final boolean USE_ACTUAL_PARAM_NAME = true;
    public static final Integer SERVER_PORT = reader.getIntProperty("server.port", 8889);
    public static final List<DataSourceProp> dataSources;
    public static final KafkaUtils.KafkaProp kafkaProp;




    public static final String ZK_IP = reader.getProperty("zookeeper.ip", "127.0.0.1");
    public static final Integer ZK_PORT = reader.getIntProperty("zookeeper.port", 2181);
    public static final String ZK_ADDRESS = reader.getProperty("zookeeper.address", "127.0.0.1:2181");
    public static final String ZK_NAMESPACE = reader.getProperty("zookeeper.namespace", "im");
    public static final String ZK_ROOTPATH = reader.getProperty("zookeeper.rootPath", "/root");


    public static final String REDIS_IP = reader.getProperty("redis.ip", "127.0.0.1");
    public static final Integer REDIS_PORT = reader.getIntProperty("redis.port", 6379);
    public static final String REDIS_PWD = reader.getProperty("redis.pwd", null);

    public static final String KAFKA_ADDRESS = reader.getProperty("kafka.address", "127.0.0.1:9092");

    static {
        Map<String, Object> map = YamlUtils.yaml2Map(CONFIG_FILE_NAME);
        Map mybatis = CollectionUtils.getValue(map, "mybatis", Map.class);
        List datasource = CollectionUtils.getValue(mybatis, "datasource", List.class);
        String s = JsonUtils.object2Json(datasource);
        dataSources = JsonUtils.json2Object(s, List.class, DataSourceProp.class);


        Map kafka = CollectionUtils.getValue(map, "kafka", Map.class);
        String cs = JsonUtils.object2Json(kafka);
        kafkaProp = JsonUtils.json2Object(cs, KafkaUtils.KafkaProp.class);
    }


}
