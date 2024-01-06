package org.light.rpc.framework.core.filter;

import java.util.Map;

/**
 * @author lxk
 * @date 2023/8/24 9:28
 */
public interface ServerFilter extends Filter {

    void doFilter(Map<String, Object> attachments);
}
