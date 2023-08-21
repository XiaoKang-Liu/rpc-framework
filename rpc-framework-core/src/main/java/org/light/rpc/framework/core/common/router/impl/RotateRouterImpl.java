package org.light.rpc.framework.core.common.router.impl;

import org.light.rpc.framework.core.common.ChannelFuturePollingRef;
import org.light.rpc.framework.core.common.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.router.Router;
import org.light.rpc.framework.core.registry.URL;

/**
 * 轮询
 * @author lxk
 * @date 2023/8/21 11:21
 */
public class RotateRouterImpl implements Router {

    @Override
    public void refresh(String serviceName) {

    }

    @Override
    public ChannelFutureWrapper select(String serviceName) {
        return ChannelFuturePollingRef.getChannelFutureWrapper(serviceName);
    }

    @Override
    public void updateWeight(URL url) {

    }
}
