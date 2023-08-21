package org.light.rpc.framework.core.common;

import org.light.rpc.framework.core.common.cache.CommonClientCache;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 路由选择
 * @author lxk
 * @date 2023/8/17 19:33
 */
public class ChannelFuturePollingRef {

    private ChannelFuturePollingRef() {

    }

    private static final AtomicInteger REFERENCE_TIMES = new AtomicInteger(0);

    public static ChannelFutureWrapper getChannelFutureWrapper(String serviceName) {
        // 轮询选择
        final ChannelFutureWrapper[] arr = CommonClientCache.SERVICE_ROUTE_MAP.get(serviceName);
        final int increment = REFERENCE_TIMES.getAndIncrement();
        int index = increment % arr.length;
        return arr[index];
    }
}
