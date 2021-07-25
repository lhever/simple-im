package com.lhever.simpleim.client.config;

import com.lhever.common.core.support.lb.RoundRobinStrategy;
import com.lhever.common.core.support.timer.HashedWheelTimer;
import com.lhever.common.core.support.timer.Timeout;
import com.lhever.common.core.support.timer.Timer;
import com.lhever.common.core.support.timer.TimerTask;
import com.lhever.common.core.support.zookeeper.ZkCuratorClient;
import com.lhever.common.core.utils.CollectionUtils;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ClientZkRegister {
    private static final Logger logger = LoggerFactory.getLogger(ClientZkRegister.class);

    private String zkAddress;

    private String zkNamespace;

    private String zkRootPath;

    private ZkCuratorClient curatorClient;

    private Timer timer;

    private final AtomicReference<List<String>> reference = new AtomicReference();

    private static final int interval = 15;

    private RoundRobinStrategy<String> roundRobin = new RoundRobinStrategy<>();


    public ClientZkRegister(String zkAddress, String zkNamespace, String zkRootPath) {
        this.zkAddress = zkAddress;
        this.zkNamespace = zkNamespace;
        this.zkRootPath = zkRootPath;
    }

    public void init() {
        this.curatorClient = new ZkCuratorClient(zkAddress, zkNamespace, new RetryNTimes(3, 5000));
        reference.set(doGetOnlineServers());

        this.timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 5, TimeUnit.SECONDS, 360);
        TimerTask getServerTask = new TimerTask() {
            public void run(Timeout timeout) {
                List<String> onlineServers = doGetOnlineServers();
                if (CollectionUtils.isNotEmpty(onlineServers)) {
                    reference.set(onlineServers);
                }
                logger.info("get children will run after {} seconds", interval);
                timer.newTimeout(this, interval, TimeUnit.SECONDS);//结束时候再次注册
            }
        };
        timer.newTimeout(getServerTask, interval, TimeUnit.SECONDS);
    }

    public String getOnlineServer() {
        // choose on by round robin strategy
        return roundRobin.choose(() -> reference.get(), null);
    }

    public List<String> getAllOnlineServer() {
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
