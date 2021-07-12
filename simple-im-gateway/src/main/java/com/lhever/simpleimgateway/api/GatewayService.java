package com.lhever.simpleimgateway.api;

import com.lhever.common.core.support.zookeeper.ZkCreateMode;
import com.lhever.common.core.support.zookeeper.ZkCuratorClient;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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


    @PostConstruct
    public void init() throws Exception {
        this.curatorClient = new ZkCuratorClient(zkAddress, zkNamespace, new RetryNTimes(3, 5000));
    }


    public List<String> getOnlineServers() throws Exception {

        List<String> children = curatorClient.getChildren(zkRootPath);
        logger.info("get children:{}", children);

        List<String> servers = new ArrayList<>();
        for (String child : children) {
            if (child.startsWith(zkRootPath)) {
                String server = child.substring(zkRootPath.length());
                servers.add(server);
            } else {
                servers.add(child);
            }
        }
        return servers;
    }

}
