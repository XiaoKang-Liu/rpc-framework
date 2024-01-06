package org.light.rpc.framework.core.filter;

import org.light.rpc.framework.core.common.wrapper.ChannelFutureWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author lxk
 * @date 2023/8/24 9:26
 */
public interface ClientFilter extends Filter {

    /**
     * 执行过滤器链
     * @param src          连接
     * @param attachments  过滤参数
     */
    void doFilter(List<ChannelFutureWrapper> src, Map<String, Object> attachments);
}
