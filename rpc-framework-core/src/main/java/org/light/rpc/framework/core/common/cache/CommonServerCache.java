package org.light.rpc.framework.core.common.cache;

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

    public static final Map<String, Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<String, Object>();

    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
}
