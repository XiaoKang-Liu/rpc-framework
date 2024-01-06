package org.light.rpc.framework.core.filter.impl.client;

import org.light.rpc.framework.core.common.wrapper.ChannelFutureWrapper;
import org.light.rpc.framework.core.filter.ClientFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 客户端过滤器链
 * @author lxk
 * @date 2023/10/22 21:18
 */
public class ClientFilterChain {

    private final List<ClientFilter> clientFilterList = new ArrayList<>();

    public void addClientFilter(ClientFilter clientFilter) {
        clientFilterList.add(clientFilter);
    }

    public void doFilter(List<ChannelFutureWrapper> src, Map<String, Object> attachments) {
        for (ClientFilter clientFilter : clientFilterList) {
            clientFilter.doFilter(src, attachments);
        }
    }
}
