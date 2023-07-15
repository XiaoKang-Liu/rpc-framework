package org.light.rpc.framework.core.common.cache;

import io.netty.util.concurrent.Promise;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

import java.util.Map;
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
}
