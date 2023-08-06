package org.light.rpc.framework.core.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * 节点操作
 * @author lxk
 * @date 2023/07/24 22:45
 **/
public class CuratorClient {

    private CuratorFramework client;

    public CuratorClient(String zkAddress) {
        this(zkAddress, 3000, 1);
    }

    public CuratorClient(String zkAddress, int baseSleepTimes, int maxRetryTimes) {
        ExponentialBackoffRetry retryPolicy= new ExponentialBackoffRetry(baseSleepTimes, maxRetryTimes);
        client = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
    }

    public CuratorFramework getClient() {
        return client;
    }

    /**
     * 获取节点数据
     * @param path  节点路径
     * @return      节点数据
     */
    public String getNodeData(String path) {
        try {
            byte[] bytes = client.getData().forPath(path);
            if (bytes != null) {
                return new String(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 设置节点数据
     * @param path  节点路径
     * @param data  节点数据
     */
    public void updateNodeData(String path, String data) {
        try {
            client.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getChildrenData(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建持久节点
     * @param path  节点路径
     * @param data  节点数据
     */
    public void creatPersistentNode(String path, String data) {
        try {
            client.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建持久顺序节点
     * @param path  节点路径
     * @param data  节点数据
     */
    public void creatPersistentSeqNode(String path, String data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建临时节点
     * @param path  节点路径
     * @param data  节点数据
     */
    public void creatEphemeralNode(String path, String data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建临时顺序节点
     * @param path  节点路径
     * @param data  节点数据
     */
    public void creatEphemeralSeqNode(String path, String data) {
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        client.close();
    }

    /**
     * 删除节点
     * @param path   节点路径
     * @return       结果
     */
    public boolean deleteNode(String path) {
        try {
            client.delete().forPath(path);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断节点是否存在
     * @param path  节点路径
     * @return      结果
     */
    public boolean existNode(String path) {
        final Stat stat;
        try {
            stat = client.checkExists().forPath(path);
            return stat != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 监听指定节点
     * @param path    节点路径
     * @param listener
     */
    public void watchNodeData(String path, NodeCacheListener listener) {
        try {
            NodeCache nodeCache = new NodeCache(client, path);
            nodeCache.getListenable().addListener(listener);
            nodeCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void watchChildNodeData(String path, PathChildrenCacheListener listener) {
        try {
            PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
            pathChildrenCache.getListenable().addListener(listener);
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}