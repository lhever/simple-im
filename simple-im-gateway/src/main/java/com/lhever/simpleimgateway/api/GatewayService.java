package com.lhever.simpleimgateway.api;

import com.lhever.common.core.support.timer.HashedWheelTimer;
import com.lhever.common.core.support.timer.Timeout;
import com.lhever.common.core.support.timer.Timer;
import com.lhever.common.core.support.timer.TimerTask;
import com.lhever.common.core.support.zookeeper.ZkCreateMode;
import com.lhever.common.core.support.zookeeper.ZkCuratorClient;
import com.lhever.common.core.utils.CollectionUtils;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.CacheOperationInvoker;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GatewayService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayService.class);

    @Value("${zookeeper.address}")
    private String zkAddress;

    @Value("${zookeeper.namespace}")
    private String zkNamespace;

    @Value("${zookeeper.rootPath}")
    private String zkRootPath;

    private ZkCuratorClient curatorClient;

    private Timer timer;

    private final AtomicReference<List<String>> reference = new AtomicReference();


    @PostConstruct
    public void init() throws Exception {
        this.curatorClient = new ZkCuratorClient(zkAddress, zkNamespace, new RetryNTimes(3, 5000));
        reference.set(doGetOnlineServers());

        this.timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 5, TimeUnit.SECONDS, 360);
        TimerTask getServerTask = new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                List<String> onlineServers = doGetOnlineServers();
                if (CollectionUtils.isNotEmpty(onlineServers)) {
                    reference.set(onlineServers);
                }
                logger.info("get children will run after {} seconds", 30);
                timer.newTimeout(this, 30, TimeUnit.SECONDS);//结束时候再次注册
            }
        };
        timer.newTimeout(getServerTask, 30, TimeUnit.SECONDS);
    }

    public List<String> getOnlineServers() {
        return reference.get();
    }


    private List<String> doGetOnlineServers() {

        List<String> children = null;
        try {
            children = curatorClient.getChildren(zkRootPath);
        } catch (Throwable e) {
            logger.error("get children error", e);
        }
        logger.info("get children:{}", children);
        if (CollectionUtils.isEmpty(children)) {
            return Collections.unmodifiableList(new ArrayList<>(0));
        }

        List<String> servers = new ArrayList<>();
        for (String child : children) {
            if (child.startsWith(zkRootPath)) {
                String server = child.substring(zkRootPath.length());
                servers.add(server);
            } else {
                servers.add(child);
            }
        }
        return Collections.unmodifiableList(servers);
    }

}
