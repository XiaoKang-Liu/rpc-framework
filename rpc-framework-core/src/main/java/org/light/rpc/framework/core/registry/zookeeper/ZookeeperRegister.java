package org.light.rpc.framework.core.registry.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.light.rpc.framework.core.common.event.RpcEvent;
import org.light.rpc.framework.core.common.event.RpcListenerLoader;
import org.light.rpc.framework.core.common.event.RpcUpdateEvent;
import org.light.rpc.framework.core.common.event.data.URLChangeWrapper;
import org.light.rpc.framework.core.registry.URL;

import java.util.List;

/**
 * @author lxk
 * @date 2023/7/25 15:22
 */
@Slf4j
public class ZookeeperRegister extends AbstractRegister {

    private CuratorClient zkClient;

    private static final String ROOT  = "/rpc";

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorClient(address);
    }

    /**
     * 生成 provide 节点路径
     * @param url
     * @return
     */
    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" +
                url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    /**
     * 生成 consumer 节点路径
     * @param url
     * @return
     */
    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" +
                url.getParameters().get("host") + ":" + url.getParameters().get("port");
    }

    /**
     * 根据 serviceName 获取服务提供者 ip
     * @param serviceName
     * @return
     */
    private List<String> getProviderIps(String serviceName) {
        return zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
    }

    @Override
    public void registry(URL url) {
        if (!zkClient.existNode(ROOT)) {
            zkClient.creatPersistentNode(ROOT, "");
        }
        final String providerUrlStr = URL.buildProviderUrlStr(url);
        final String providerPath = getProviderPath(url);
        if (zkClient.existNode(providerPath)) {
            // 如已存在，需要删除
            zkClient.deleteNode(providerPath);
        }
        // 创建临时节点
        zkClient.creatEphemeralNode(providerPath, providerUrlStr);
        super.registry(url);
    }

    @Override
    public void unRegistry(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegistry(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!zkClient.existNode(ROOT)) {
            zkClient.creatPersistentNode(ROOT, "");
        }
        // 将消费者自身注册到 zookeeper
        final String consumerUrlStr = URL.buildConsumerUrlStr(url);
        final String consumerPath = getConsumerPath(url);
        if (zkClient.existNode(consumerPath)) {
            // 如已存在，需要删除
            zkClient.deleteNode(consumerPath);
        }
        // 创建临时节点
        zkClient.creatEphemeralNode(consumerPath, consumerUrlStr);
        super.subscribe(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void doAfterSubscribe(URL url) {
        // 消费者自身注册完成后，监听提供者是否有新的服务注册
        String newServerNodePath = ROOT + "/" + url.getServiceName() + "/provider";
        watchChildNodeData(newServerNodePath);
    }

    private void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                String path = event.getData().getPath();
                // 包装事件元数据
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setType(event.getType());
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                // 事件
                RpcEvent rpcEvent = new RpcUpdateEvent(urlChangeWrapper);
                // 监听
                RpcListenerLoader.sendEvent(rpcEvent);
            }
        });
    }
}
