package org.light.rpc.framework.core.registry;

import lombok.Data;

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
}