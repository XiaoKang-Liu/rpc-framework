package org.light.rpc.framework.core.common.router.impl;

import org.light.rpc.framework.core.common.ChannelFuturePollingRef;
import org.light.rpc.framework.core.common.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.router.Router;
import org.light.rpc.framework.core.registry.URL;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * 随机路由
 * @author lxk
 * @date 2023/8/18 10:08
 */
public class RandomRouterImpl implements Router {

    @Override
    public void refresh(String serviceName) {
        final List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(serviceName);
        final ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        // 提前生成随机调用的数组
        int[] result = createRandomIndex(channelFutureWrappers.size());
        // 生成对应服务每台机器的调用顺序
        for (int i = 0; i < result.length; i++) {
            arr[i] = channelFutureWrappers.get(result[i]);
        }
        CommonClientCache.SERVICE_ROUTE_MAP.put(serviceName, arr);
    }

    @Override
    public ChannelFutureWrapper select(String serviceName) {
        return ChannelFuturePollingRef.getChannelFutureWrapper(serviceName);
    }

    @Override
    public void updateWeight(URL url) {

    }

    private int[] createRandomIndex(int len) {
        int[] arrInt = new int[len];
        Random random = new SecureRandom();
        int index = 0;
        while (index < len) {
            int num = random.nextInt(len);
            if (!contains(arrInt, num)) {
                arrInt[index++] = num;
            }
        }
        return arrInt;
    }

    private boolean contains(int[] arr, int key) {
        for (int value : arr) {
            if (value == key) {
                return true;
            }
        }
        return false;
    }
}
