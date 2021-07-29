package com.lhever.simpleim.router;

import com.lhever.simpleim.common.util.KafkaUtils;
import com.lhever.simpleim.common.util.RedisUtils;
import com.lhever.simpleim.router.basic.cfg.RouterConfig;

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
         initKafka();
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

    private static void initKafka() {
        KafkaUtils.init(RouterConfig.KAFKA_ADDRESS);
    }


}
