package org.light.rpc.framework.core.registry.zookeeper;

import org.light.rpc.framework.core.registry.URL;

import java.util.List;

/**
 * @author lxk
 * @date 2023/7/25 15:22
 */
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
        super.subscribe(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        super.doUnSubscribe(url);
    }

    @Override
    public void doAfterSubscribe(URL url) {

    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }
}
