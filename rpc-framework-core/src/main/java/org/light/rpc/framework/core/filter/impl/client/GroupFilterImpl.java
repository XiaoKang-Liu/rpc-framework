package org.light.rpc.framework.core.filter.impl.client;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.light.rpc.framework.core.common.wrapper.ChannelFutureWrapper;
import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.filter.ClientFilter;

import java.util.List;
import java.util.Map;

/**
 * 分组过滤器
 * @author lxk
 * @date 2023/8/24 9:31
 */
public class GroupFilterImpl implements ClientFilter {

    @Override
    public void doFilter(List<ChannelFutureWrapper> src, Map<String, Object> attachments) {
        final String group = String.valueOf(attachments.get("group"));
        if (StringUtils.isEmpty(group)) {
            return;
        }
        src.removeIf(item -> !group.equals(item.getGroup()));
        if (CollectionUtils.isEmpty(src)) {
            throw new RpcException("no provider match for group: " + group);
        }
    }
}
