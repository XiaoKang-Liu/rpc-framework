package org.light.rpc.framework.core.registry.zookeeper;

import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.registry.RegistryService;
import org.light.rpc.framework.core.registry.URL;

import java.util.List;

/**
 * @author lxk
 * @date 2023/07/24 22:31
 **/
public abstract class AbstractRegister implements RegistryService {

    @Override
    public void registry(URL url) {
        CommonServerCache.PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegistry(URL url) {
        CommonServerCache.PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        CommonClientCache.SUBSCRIBE_SERVICE_LIST.add(url.getServiceName());
    }

    @Override
    public void doUnSubscribe(URL url) {
        CommonClientCache.SUBSCRIBE_SERVICE_LIST.remove(url.getServiceName());
    }

    public abstract void doAfterSubscribe(URL url);

    public abstract void doBeforeSubscribe(URL url);

    public abstract List<String> getProviderIps(String serviceName);
}