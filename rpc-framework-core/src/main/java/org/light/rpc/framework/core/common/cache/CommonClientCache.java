package org.light.rpc.framework.core.common.cache;

import io.netty.util.concurrent.Promise;
import org.light.rpc.framework.core.common.wrapper.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
import org.light.rpc.framework.core.filter.impl.client.ClientFilterChain;
import org.light.rpc.framework.core.registry.URL;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lxk
 * @date 2023/7/14 14:42
 */
public class CommonClientCache {

    private CommonClientCache() {

    }

    /**
     * 请求队列
     */
    public static final BlockingQueue<RpcRequestMessage> SEND_QUEUE = new ArrayBlockingQueue<>(100);

    /**
     * 接受响应结果
     */
    public static final Map<String, Promise<Object>> RESP_MAP = new ConcurrentHashMap<>();

    /**
     * 服务要消费的接口
     */
    public static final List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    public static final Map<String, Map<String, String>> URL_MAP = new ConcurrentHashMap<>();

    /**
     *
     */
    public static final Set<String> SERVER_ADDRESS = new HashSet<>();

    /**
     * 每次进行远程调用的时候都是从这里面去选择服务提供者
     */
    public static final Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();


    public static final Map<String, ChannelFutureWrapper[]> SERVICE_ROUTE_MAP = new ConcurrentHashMap<>();

    /**
     * 客户端过滤器链
     */
    public static ClientFilterChain CLIENT_FILTER_CHAIN;
}
