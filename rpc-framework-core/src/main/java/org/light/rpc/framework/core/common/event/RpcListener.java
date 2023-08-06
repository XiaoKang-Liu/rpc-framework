package org.light.rpc.framework.core.common.event;

/**
 * 监听器接口
 * @author lxk
 * @date 2023/08/05 16:33
 **/
public interface RpcListener<T> {

    /**
     * 事件监听回调
     * @param t  事件
     */
    void callBack(Object t);
}