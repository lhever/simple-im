package com.lhever.common.core.support.zookeeper;

import com.lhever.common.core.support.logger.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 14:44
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 14:44
 * @modify by reason:{方法名}:{原因}
 */

public class TreeCacheDemo {

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
        TreeCache  treeCache = new TreeCache(curatorClient.getClient(), "/root");
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        treeCache.start();
        //PathChildrenCacheListener
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {

                TreeCacheEvent.Type type = event.getType();
                String path = event.getData().getPath();

                System.out.println("事件类型是：" + type + ", 路径是: " + path);

            }
        });
        treeCache.getUnhandledErrorListenable().addListener(new UnhandledErrorListener() {
            public void unhandledError(String s, Throwable throwable) {
                System.out.println("::::: " + s);
                System.out.println("错误原因：" + throwable.getMessage() + "\n==============\n");
                throwable.printStackTrace();
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

        ChildData currentData = treeCache.getCurrentData("/root");
        System.out.println(currentData.getPath());
        treeCache.close();

    }
}
