package org.light.rpc.framework.core.common.config;

/**
 * 客户端配置
 * @author lxk
 * @date 2023/7/14 14:28
 */
public class ClientConfig {

    private Integer port;

    private String serverAddress;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
