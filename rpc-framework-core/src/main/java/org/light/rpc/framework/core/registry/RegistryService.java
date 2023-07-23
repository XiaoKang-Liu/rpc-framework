package org.light.rpc.framework.core.registry;

/**
 * 注册相关接口
 * @author lxk
 * @date 2023/07/16 22:16
 **/
public interface RegistryService {

    /**
     * 服务注册
     * 将 rpc 服务写入注册中心节点，出现网络抖动时需要进行适当的重试做法
     * 并写入持久化文件
     * @param url url
     */
    void registry(URL url);

    /**
     * 服务下线
     * 移除持久化文件中对应的信息
     * @param url url
     */
    void unRegistry(URL url);

    /**
     * 消费方订阅服务
     * @param url url
     */
    void subscribe(URL url);


    /**
     * 执行取消订阅内部的逻辑
     * @param url url
     */
    void doUnSubscribe(URL url);
}