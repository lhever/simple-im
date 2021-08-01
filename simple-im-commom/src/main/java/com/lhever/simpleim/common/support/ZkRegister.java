package com.lhever.simpleim.common.support;


import com.lhever.common.core.exception.CommonException;
import com.lhever.common.core.support.zookeeper.ZkCreateMode;
import com.lhever.common.core.support.zookeeper.ZkCuratorClient;
import com.lhever.common.core.utils.FileUtils;
import com.lhever.common.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ZkRegister {

    private static final Logger logger = LoggerFactory.getLogger(ZkRegister.class);

    private ZkCuratorClient client;
    private boolean register = true;
    private TreeCache treeCache;
    private ZkProp zkProp;

    private Set<String> registerPath = new HashSet<>();

    public ZkRegister(ZkProp zkProp) {
        this.zkProp = zkProp;
        RetryNTimes retry = new RetryNTimes(zkProp.getRetry(), zkProp.getSleepMsBetweenRetries());
        this.client = new ZkCuratorClient(zkProp.getAddress(), zkProp.getNamespace(),retry);
        if (register) {
            startCache();
        }
    }

    private void startCache() {
        this.treeCache = new TreeCache(client.getClient(), zkProp.getRootpath());
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                TreeCacheEvent.Type eventType = event.getType();
                String fullChildPath = null;
                if (eventType == null || event.getData() == null || (fullChildPath = event.getData().getPath()) == null) {
                    return;
                }
                logger.info("事件类型是:{}, 路径是:{}", eventType, fullChildPath);
                switch (eventType) {
                    case NODE_REMOVED:
                        String rootpath = zkProp.getRootpath();
                        if (fullChildPath.startsWith(rootpath)) {
                            String node = FileUtils.trim(fullChildPath.substring(rootpath.length()));
                            if (registerPath.contains(node)) {
                                logger.info("节点:{}被移除，重新注册:{}到根节点:{}", fullChildPath, node, rootpath);
                                register(node);
                            }
                        }
                        break;
                    default:
                }
            }
        });
        treeCache.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            @Override
            public void unhandledError(String message, Throwable e) {
                logger.error("" + message, e);
            }
        });

        try {
            treeCache.start();
        } catch (Exception e) {
//            treeCache.close();
            throw new CommonException("start tree cache error", e);
        }

    }


    public void register(String childPath) {
        try {
            childPath = FileUtils.trim(childPath);
            registerPath.add(childPath);
            doRregister(childPath);
        } catch (CommonException e) {
            logger.error("register path:{} erro", childPath, e);
            throw e;
        } catch (Throwable e) {
            logger.error("register path:{} erro", childPath, e);
            throw new CommonException("register eror", e);
        }
    }


    private void doRregister(String childPath) throws Exception {
        String rootPath = zkProp.getRootpath();
        ZkCreateMode createRootResult = client.createNode(CreateMode.PERSISTENT, rootPath, null);
        int n = 0;
        while (ZkCreateMode.FAILED == createRootResult && (n++ < 3)) {
            createRootResult = client.createNode(CreateMode.PERSISTENT, rootPath, null);
        }
        client.createNode(CreateMode.EPHEMERAL, StringUtils.appendAll(rootPath, "/", childPath), null);
        List<String> children = client.getChildren(rootPath);
        Set<String> childrenSet = new HashSet<>(children);
        logger.info("根路径:{}下存在子节点:{}", rootPath, children);
        if (!childrenSet.contains(childPath)) {
            throw new CommonException("server register:" + childPath + " to zookeeper failed");
        }
    }


}
