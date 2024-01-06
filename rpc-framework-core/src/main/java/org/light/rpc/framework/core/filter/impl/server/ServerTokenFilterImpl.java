package org.light.rpc.framework.core.filter.impl.server;

import org.light.rpc.framework.core.filter.ServerFilter;

import java.util.Map;

/**
 * 服务端 token 校验
 * @author lxk
 * @date 2023/8/24 9:46
 */
public class ServerTokenFilterImpl implements ServerFilter {

    @Override
    public void doFilter(Map<String, Object> attachments) {
        final String serviceToken = String.valueOf(attachments.get("serviceToken"));
    }
}
