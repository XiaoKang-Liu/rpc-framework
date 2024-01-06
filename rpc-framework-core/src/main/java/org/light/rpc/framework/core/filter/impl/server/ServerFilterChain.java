package org.light.rpc.framework.core.filter.impl.server;

import org.light.rpc.framework.core.filter.ClientFilter;
import org.light.rpc.framework.core.filter.ServerFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 服务端过滤器链
 * @author lxk
 * @date 2023/10/22 21:18
 */
public class ServerFilterChain {

    private final List<ServerFilter> serverFilterList = new ArrayList<>();

    public void addClientFilter(ServerFilter serverFilter) {
        serverFilterList.add(serverFilter);
    }

    public void doFilter(Map<String, Object> attachments) {
        for (ServerFilter serverFilter : serverFilterList) {
            serverFilter.doFilter(attachments);
        }
    }
}
