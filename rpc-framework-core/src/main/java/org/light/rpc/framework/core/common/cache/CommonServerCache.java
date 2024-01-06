package org.light.rpc.framework.core.common.cache;

import org.light.rpc.framework.core.filter.impl.server.ServerFilterChain;
import org.light.rpc.framework.core.registry.URL;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端缓存类
 * @author lxk
 * @date 2023/7/12 18:11
 */
public class CommonServerCache {

    private CommonServerCache() {}

    /**
     * key   接口全限定类名
     * value 接口对应实现类
     */
    public static final Map<String, Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<>();

    /**
     * 服务中所有注册到 zookeeper 的接口的 URL
     */
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();

    /**
     * 服务端过滤器链
     */
    public static ServerFilterChain SERVER_FILTER_CHAIN;
}
