package com.lhever.common.core.support.zookeeper;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 13:21
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 13:21
 * @modify by reason:{方法名}:{原因}
 */

import com.lhever.common.core.support.logger.LogFactory;
import com.lhever.common.core.utils.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;

import java.util.List;

/**
 * <p>
 * Curator客户端基础使用
 * <p/>
 *
 * @author smilehappiness
 * @Date 2020/6/21 9:41
 */
public class ZkCuratorClient {

    private static final Logger logger = LogFactory.getLogger(ZkCuratorClient.class);

    private CuratorFramework client = null;

    /**
     * 重试策略
     * baseSleepTimeMs：初始的重试等待时间，单位毫秒
     * maxRetries：最多重试次数
     */
    private RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    /**
     * 重试策略
     * n：最多重试次数
     * sleepMsBetweenRetries：重试时间间隔，单位毫秒
     */
    private RetryPolicy retry = new RetryNTimes(3, 2000);

    public CuratorFramework getClient() {
        return client;
    }

    public ZkCuratorClient(String address) {
        this.client = createCuratorClient(address, null, null, null, null);
    }

    public ZkCuratorClient(String address, String namespace) {

        this.client = createCuratorClient(address, namespace, null, null, null);
    }


    public ZkCuratorClient(String address, String namespace, RetryPolicy retryPolicy) {

        this.client = createCuratorClient(address, namespace, retryPolicy, null, null);
    }


    public ZkCuratorClient(String address, String namespace, RetryPolicy retryPolicy, Integer sessionTimeOutMs, Integer connecTimeOutMs) {

        this.client = createCuratorClient(address, namespace, retryPolicy, sessionTimeOutMs, connecTimeOutMs);
    }


    /**
     * <p>
     * 创建Curator连接对象
     * <p/>
     *
     * @param
     * @return
     * @Date 2020/6/21 12:29
     */
    public CuratorFramework createCuratorClient(String address, String namespace, RetryPolicy retryPolicy,
                                                Integer sessionTimeOutMs, Integer connecTimeOutMs) {

        sessionTimeOutMs = (sessionTimeOutMs == null || sessionTimeOutMs < 0) ? (50 * 1000) : sessionTimeOutMs;
        connecTimeOutMs = (connecTimeOutMs == null || connecTimeOutMs < 0) ? (15 * 1000) : sessionTimeOutMs;
        //老版本的方式，创建zookeeper连接客户端
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

        builder.connectString(address);
        if (StringUtils.isNotBlank(namespace)) {
            builder.namespace(namespace);
        }
        builder.sessionTimeoutMs(sessionTimeOutMs).connectionTimeoutMs(connecTimeOutMs);

        if (retryPolicy != null) {
            builder.retryPolicy(retryPolicy);
        }
        this.client = builder.build();
        //create client, new api
//        this.client = CuratorFrameworkFactory.newClient(ZK_ADDRESS, retryPolicy);

        client.start();
        logger.info("zookeeper initialize success");

        return client;
    }


    /**
     * <p>
     * 创建节点，并支持赋值数据内容
     * <p/>
     *
     * @param nodePath
     * @param data
     * @return void
     * @Date 2020/6/21 12:39
     */
    public ZkCreateMode createNode(CreateMode createMode, String nodePath, byte[] data) throws Exception {
        if (StringUtils.isEmpty(nodePath)) {
            throw new IllegalArgumentException("node path cannot be null");
        }

        Stat exists = client.checkExists().forPath(nodePath);
        if (null != exists) {
            logger.info("节点{}已存在,不能新增", nodePath);
            return ZkCreateMode.ALREADY_EXISTS;
        }

        //创建节点，并为当前节点赋值内容
        String node = null;
        try {
            if (data != null) {
                node = client.create().creatingParentsIfNeeded().withMode(createMode).forPath(nodePath, data);
            } else {
                node = client.create().creatingParentsIfNeeded().withMode(createMode).forPath(nodePath);
            }
            logger.info("节点{}创建成功", node);
            return ZkCreateMode.SUCCESS;
        } catch (Exception e) {
            logger.info("节点{}创建失败", node, e);
            return ZkCreateMode.FAILED;
        }
    }

    /**
     * 方法描述:删除指定节点
     *
     * @param nodePath
     * @param force
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人}
     * @modify by reason:{原因}
     */
    public void deleteNode(String nodePath, boolean force) throws Exception {
        Object deleteResult = null;
        if (force) {
            deleteResult = client.delete().guaranteed().deletingChildrenIfNeeded().forPath(nodePath);
        } else {
            deleteResult = client.delete().deletingChildrenIfNeeded().forPath(nodePath);
        }
        logger.info("delete node:{} success, detail:{}", nodePath, deleteResult);
    }


    public byte[] getData(String nodePath) throws Exception {
        //获取某个节点数据
        byte[] bytes = client.getData().forPath(nodePath);
        return bytes;
    }


    public Stat getDataAndStore(String nodePath) throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(nodePath);
        return stat;
    }

    /**
     * <p>
     * 设置（修改）节点数据
     * <p/>
     *
     * @param nodePath
     * @param data
     * @return void
     * @Date 2020/6/21 13:46
     */
    public void updateData(String nodePath, byte[] data, boolean isAsync) throws Exception {
        //异步设置某个节点数据
        if (isAsync) {
            client.setData().inBackground().forPath(nodePath, data);
        } else {
            Stat stat = client.setData().forPath(nodePath, data);
            logger.info("set data success for path:{}, detail:{}", nodePath, stat);
        }
    }


    public List<String> getChildren(String nodePath) throws Exception {
        //获取某个节点的所有子节点
        List<String> stringList = client.getChildren().forPath(nodePath);
        return stringList;
    }


    public static void main(String[] args) throws Exception {
        ZkCuratorClient client = new ZkCuratorClient("127.0.0.1:2181", "im", new RetryNTimes(3, 2000));
        //创建节点
        client.deleteNode("/root", true);

        ZkCreateMode createRootResult = client.createNode(CreateMode.PERSISTENT, "/root", null);
        int n = 0;
        while (ZkCreateMode.FAILED == createRootResult && (n++ < 3)) {
            createRootResult = client.createNode(CreateMode.PERSISTENT, "/root", null);
        }

        client.createNode(CreateMode.EPHEMERAL, "/root/127.0.0.1" + ":" + 8080, null);
        List<String> root = client.getChildren("/root");
        root.stream().forEach(s -> System.out.println(s));
    }


}

