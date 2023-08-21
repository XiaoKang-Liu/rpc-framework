package org.light.rpc.framework.core.common.router;

import org.light.rpc.framework.core.common.ChannelFutureWrapper;
import org.light.rpc.framework.core.registry.URL;

/**
 * 路由选择接口
 * @author lxk
 * @date 2023/8/17 19:45
 */
public interface Router {

    /**
     * 路由数组刷新
     * @param serviceName  服务名
     */
    void refresh(String serviceName);

    /**
     * 选择服务提供者
     * @param serviceName  服务名
     * @return             连接通道
     */
    ChannelFutureWrapper select(String serviceName);

    /**
     * 更新权重信息
     * @param url  服务提供者 url
     */
    void updateWeight(URL url);
}
