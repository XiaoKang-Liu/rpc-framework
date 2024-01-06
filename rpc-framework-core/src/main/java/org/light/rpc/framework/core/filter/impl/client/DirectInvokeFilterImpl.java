package org.light.rpc.framework.core.filter.impl.client;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.light.rpc.framework.core.common.wrapper.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.filter.ClientFilter;

import java.util.List;
import java.util.Map;

/**
 * 直连过滤器
 * @author lxk
 * @date 2023/8/24 9:36
 */
public class DirectInvokeFilterImpl implements ClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, Map<String, Object> attachments) {
        final String url = String.valueOf(attachments.get("url"));
        if (StringUtils.isEmpty(url)) {
            return;
        }
        src.removeIf(item -> !url.equals(item.getIp() + ":" + item.getPort()));
        if (CollectionUtils.isEmpty(src)) {
            throw new RpcException("no provider match for url: " + url);
        }
    }
}
