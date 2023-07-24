package org.light.rpc.framework.core.common.cache;

import io.netty.util.concurrent.Promise;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
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

    /**
     * 请求队列
     */
    public static BlockingQueue<RpcRequestMessage> SEND_QUEUE = new ArrayBlockingQueue<>(100);

    public static Map<String, Promise<Object>> RESP_MAP = new ConcurrentHashMap<>();

    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();

    public static Set<String> SERVER_ADDRESS = new HashSet<>();

    //每次进行远程调用的时候都是从这里面去选择服务提供者
//    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();
}
