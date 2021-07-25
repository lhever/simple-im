package com.lhever.simpleim.server.support;


import com.lhever.common.core.exception.CommonException;
import com.lhever.common.core.support.zookeeper.ZkCreateMode;
import com.lhever.common.core.support.zookeeper.ZkCuratorClient;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.server.config.ServerConfig;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZkRegister {

    private static final Logger logger = LoggerFactory.getLogger(ZkRegister.class);

    private ZkCuratorClient client;

    public ZkRegister() {
        this.client = new ZkCuratorClient(ServerConfig.ZK_ADDRESS, ServerConfig.ZK_NAMESPACE, new RetryNTimes(3, 5000));
    }


    public void register(String ip, Integer port) {
        try {
            doRregister(ip, port);
        } catch (CommonException e) {
            logger.error("register server with ip:{}, port:{} erro", ip, port, e);
            throw e;
        } catch (Throwable e) {
            logger.error("register server with ip:{}, port:{} erro", ip, port, e);
            throw new CommonException("register eror", e);
        }
    }


    public void doRregister(String ip, Integer port) throws Exception {
        String rootPath = ServerConfig.ZK_ROOTPATH;
        ZkCreateMode createRootResult = client.createNode(CreateMode.PERSISTENT, rootPath, null);
        int n = 0;
        while (ZkCreateMode.FAILED == createRootResult && (n++ < 3)) {
            createRootResult = client.createNode(CreateMode.PERSISTENT, rootPath, null);
        }

        String childPath = StringUtils.appendAll(ip, ":", String.valueOf(port));

        client.createNode(CreateMode.EPHEMERAL, StringUtils.appendAll(rootPath, "/", childPath), null);
        List<String> children = client.getChildren(rootPath);
        Set<String> childrenSet = new HashSet<>(children);
        if (!childrenSet.contains(childPath)) {
            throw new CommonException("server register self to zookeeper failed");
        }
    }


}
