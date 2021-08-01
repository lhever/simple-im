package com.lhever.simpleim.router;

import com.lhever.common.kafka.SimpleKafkaManager;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import com.lhever.simpleim.router.basic.cfg.RouterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/28 21:34
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/28 21:34
 * @modify by reason:{方法名}:{原因}
 */
public class RouterInitializer {
    private static final Logger logger = LoggerFactory.getLogger(RouterInitializer.class);

    public static void init() {
         initRedis();
         initTopic();
         initKafkaProducer();
    }

    private static void initRedis() {
        RedisUtils.RedisProp redisProp = new RedisUtils.RedisProp();
        redisProp.setHost(RouterConfig.REDIS_IP);
        redisProp.setPort(RouterConfig.REDIS_PORT);
        redisProp.setPass(RouterConfig.REDIS_PWD);
        redisProp.setTimeout(20000);
        redisProp.setMaxIdle(5);
        redisProp.setMaxTotal(20);
        redisProp.setMaxWaitMillis(20000);
        redisProp.setTestOnBorrow(true);
        RedisUtils.init(redisProp);
    }

    private static void initKafkaProducer() {
        KafkaUtils.init(RouterConfig.KAFKA_ADDRESS);
    }


    public static void initTopic() {
        SimpleKafkaManager simpleKafkaManager = new SimpleKafkaManager(RouterConfig.KAFKA_ADDRESS);
        List<String> routerTopics = KafkaUtils.ROUTER_TOPICS;
        for (String topic : routerTopics) {
            doCreateTopic(simpleKafkaManager, topic);
        }
    }

    public static void doCreateTopic(SimpleKafkaManager simpleKafkaManager, String topic) {
        try {
            simpleKafkaManager.createTopic(topic, Optional.of(2), Optional.empty());
        } catch (Throwable e) {
            logger.error("create topic:{} error", topic, e);

        }
    }


}
