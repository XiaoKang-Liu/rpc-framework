package org.light.rpc.framework.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.apache.commons.collections4.CollectionUtils;
import org.light.rpc.framework.core.common.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.exception.RpcException;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * 注册中心节点变更时，更新内存中 url
 * @author lxk
 * @date 2023/8/6 22:28
 */
public class ConnectionHandler {

    private static Bootstrap bootstrap;

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    /**
     * 构建单个连接通道和进行连接内存管理
     * @param providerServiceName
     * @param providerAddr
     */
    public static void connect(String providerServiceName, String providerAddr) throws InterruptedException {
        if (bootstrap == null) {
            throw new RpcException("bootstrap can not be null");
        }
        // 校验格式
        if(!providerAddr.contains(":")){
            return;
        }
        final String[] addr = providerAddr.split(":");
        final String ip = addr[0];
        final int port = Integer.parseInt(addr[1]);
        doConnect(providerServiceName, ip, port);
    }

    public static void doConnect(String providerServiceName, String ip, Integer port) throws InterruptedException {
        final ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(ip, port)).sync();
        final ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
        channelFutureWrapper.setChannelFuture(channelFuture);
        channelFutureWrapper.setIp(ip);
        channelFutureWrapper.setPort(port);
        // 加入本地缓存
        CommonClientCache.SERVER_ADDRESS.add(ip + ":" + port);
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerServiceName);
        if (CollectionUtils.isEmpty(channelFutureWrappers)) {
            channelFutureWrappers = new ArrayList<>();
        }
        channelFutureWrappers.add(channelFutureWrapper);
        CommonClientCache.CONNECT_MAP.put(providerServiceName, channelFutureWrappers);
    }

    public static ChannelFuture createChannelFuture(String ip,Integer port) throws InterruptedException {
        return bootstrap.connect(ip, port).sync();
    }

    public static void disconnect(String providerServiceName, String providerAddr) {
        CommonClientCache.SERVER_ADDRESS.remove(providerAddr);
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerServiceName);
        if (!channelFutureWrappers.isEmpty()) {
            channelFutureWrappers.removeIf(futureWrapper -> providerAddr.equals(futureWrapper.getIp() + ":" + futureWrapper.getPort()));
        }
    }

    /**
     * 获取连接，默认走随机策略
     * @param providerServiceName
     * @return
     */
    public static ChannelFuture getChannelFuture(String providerServiceName) {
        List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(providerServiceName);
        if (channelFutureWrappers.isEmpty()) {
            throw new RpcException("no provider exist for " +
                    providerServiceName);
        }
        return channelFutureWrappers.get(new
                Random().nextInt(channelFutureWrappers.size())).getChannelFuture();
    }
}
