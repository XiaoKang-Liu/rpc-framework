package org.light.rpc.framework.core.common.event;

import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.event.data.URLChangeWrapper;

import java.util.List;

/**
 * 节点更新监听器
 * @author lxk
 * @date 2023/08/05 16:35
 **/
@Slf4j
public class ServiceUpdateListener implements RpcListener<RpcUpdateEvent> {

    @Override
    public void callBack(Object t) {
        // 更新本地缓存
        URLChangeWrapper wrapper = (URLChangeWrapper) t;
        final String serviceName = wrapper.getPath().split("/")[2];
        final List<ChannelFutureWrapper> channelFutureWrappers = CommonClientCache.CONNECT_MAP.get(serviceName);
        if (channelFutureWrappers.isEmpty()) {
            log.error("[ServiceUpdateListener] channelFutureWrappers is empty");
            return;
        }
        ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
        String host = serviceName.split(":")[0];
        Integer port = Integer.valueOf(serviceName.split(":")[1]);
        channelFutureWrapper.setPort(port);
        channelFutureWrapper.setHost(host);
        switch (wrapper.getType()) {
            case CHILD_ADDED:
                channelFutureWrappers.add(channelFutureWrapper);
                break;
            case CHILD_REMOVED:
                channelFutureWrappers.removeIf(future ->
                        future.getPort().equals(port) && future.getHost().equals(host));
                break;
            default: break;
        }

    }
}