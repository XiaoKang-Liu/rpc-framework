package org.light.rpc.framework.core.registry;

import lombok.Data;
import org.light.rpc.framework.core.registry.zookeeper.ProviderNodeInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册信息元数据
 * @author lxk
 * @date 2023/07/16 22:36
 **/
@Data
public class URL {

    /**
     * 应用名称
     */
    private String applicationName;

    /***
     * 服务名称，全路径
     */
    private String serviceName;

    /**
     * 参数信息：分组 权重 服务提供者的地址 服务提供者的端口等
     */
    private Map<String, String> parameters = new HashMap<>();

    public void addParameter(String key, String value) {
        this.parameters.putIfAbsent(key, value);
    }

    /**
     * 将URL转换为写入zk的provider节点下的一段字符串
     * @param url url
     * @return    providerNodeStr
     */
    public static String buildProviderUrlStr(URL url) {
        String ip = url.getParameters().get("ip");
        String port = url.getParameters().get("port");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + ip + ":" + port + ";" + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将URL转换为写入zk的consumer节点下的一段字符串
     * @param url url
     * @return    consumerNodeStr
     */
    public static String buildConsumerUrlStr(URL url) {
        String ip = url.getParameters().get("ip");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + ip + ";" + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将某个节点下的信息转换为一个Provider节点对象
     * @param providerNodeStr  providerNodeStr
     * @return  providerNodeInfo
     */
    public static ProviderNodeInfo buildURLFromUrlStr(String providerNodeStr) {
        String[] items = providerNodeStr.split("/");
        ProviderNodeInfo providerNodeInfo = new ProviderNodeInfo();
        providerNodeInfo.setServiceName(items[2]);
        providerNodeInfo.setAddress(items[4]);
        return providerNodeInfo;
    }

}