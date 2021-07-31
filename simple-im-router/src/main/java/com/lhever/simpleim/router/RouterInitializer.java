package com.lhever.simpleim.router;

import com.lhever.common.core.utils.ParseUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.kafka.SimpleKafkaManager;
import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import com.lhever.simpleim.router.basic.cfg.RouterConfig;

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
        for (int i = 0; i < KafkaUtils.ROUTER_TOPIC_TOTAL; i++) {
            String topic = KafkaUtils.ROUTER_TOPIC_PREFIX + i;
            doCreateTopic(simpleKafkaManager, topic);
        }
    }

    public static void doCreateTopic(SimpleKafkaManager simpleKafkaManager, String topic) {
        try {
            simpleKafkaManager.createTopic(topic, Optional.of(3), Optional.empty());
        } catch (Throwable e) {

        }
    }


}
