package org.light.rpc.framework.core.registry.zookeeper;

import lombok.Data;

/**
 * @author lxk
 * @date 2023/07/23 22:59
 **/
@Data
public class ProviderNodeInfo {

    private String applicationName;

    private String serviceName;

    private String address;

    private Integer weight;

    private String group;

    private String registryTime;
}