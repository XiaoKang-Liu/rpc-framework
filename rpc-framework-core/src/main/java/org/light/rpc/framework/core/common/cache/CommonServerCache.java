package org.light.rpc.framework.core.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端缓存类
 * @author lxk
 * @date 2023/7/12 18:11
 */
public class CommonServerCache {

    public static final Map<String, Object> PROVIDER_CLASS_MAP = new ConcurrentHashMap<String, Object>();
}
