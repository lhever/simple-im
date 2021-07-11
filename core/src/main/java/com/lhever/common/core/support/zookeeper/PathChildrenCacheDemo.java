package com.lhever.common.core.support.zookeeper;

import com.lhever.common.core.support.logger.LogFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;

import java.util.List;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 14:44
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 14:44
 * @modify by reason:{方法名}:{原因}
 */

public class PathChildrenCacheDemo {

    private static final Logger logger = LogFactory.getLogger(ZkCuratorClient.class);


    public static void main(String[] args) throws Exception {
        ZkCuratorClient curatorClient = new ZkCuratorClient("10.33.65.9:2181", "im", new RetryNTimes(3, 2000));
        //删除节点
        curatorClient.deleteNode("/root", true);

        //创建节点
        ZkCreateMode createRootResult = curatorClient.createNode(CreateMode.PERSISTENT, "/root", null);
        int n = 0;
        while (ZkCreateMode.FAILED == createRootResult && (n++ < 3)) {
            createRootResult = curatorClient.createNode(CreateMode.PERSISTENT, "/root", null);
        }

        //创建PathChildrenCache
        //参数：true代表缓存数据到本地
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorClient.getClient(), "/root", true);
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        //PathChildrenCacheListener
        pathChildrenCache.getListenable().addListener((cf, event) -> {
            PathChildrenCacheEvent.Type eventType = event.getType();
            switch (eventType) {
                case CONNECTION_RECONNECTED:
                    pathChildrenCache.rebuild();
                    break;
                case CONNECTION_SUSPENDED:
                    break;
                case CONNECTION_LOST:
                    System.out.println("Connection lost");
                    break;
                case CHILD_ADDED:
                    System.out.println(event.getData().getPath() + " added");
                    break;
                case CHILD_UPDATED:
                    System.out.println(event.getData().getPath() + " updated");
                    break;
                case CHILD_REMOVED:
                    System.out.println(event.getData().getPath() + " removed");
                    break;
                case INITIALIZED:
                    System.out.println("cache initialized");
                    break;
                default:
            }
        });

        curatorClient.createNode(CreateMode.EPHEMERAL, "/root/127.0.0.1" + ":" + 8080, null);
        Thread.sleep(1000L);
        curatorClient.createNode(CreateMode.EPHEMERAL, "/root/127.0.0.1" + ":" + 8081, null);
        Thread.sleep(1000L);
        curatorClient.createNode(CreateMode.EPHEMERAL, "/root/127.0.0.1" + ":" + 8082, null);
        Thread.sleep(1000L);

        curatorClient.deleteNode("/root/127.0.0.1" + ":" + 8081, true);
        Thread.sleep(1000L);

        List<ChildData> currentData = pathChildrenCache.getCurrentData();
        currentData.stream().forEach(c -> System.out.println(c.getPath()));
        pathChildrenCache.close();

    }
}
