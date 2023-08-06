package org.light.rpc.framework.core.common.event;

import org.light.rpc.framework.core.common.event.data.URLChangeWrapper;

/**
 * 节点更新监听器
 * @author lxk
 * @date 2023/08/05 16:35
 **/
public class ServiceUpdateListener implements RpcListener<RpcUpdateEvent> {

    @Override
    public void callBack(Object t) {
        // 更新本地缓存
        URLChangeWrapper wrapper = (URLChangeWrapper) t;
        
    }
}