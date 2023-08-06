package org.light.rpc.framework.core.common.event;

/**
 * @author lxk
 * @date 2023/08/05 16:28
 **/
public interface RpcEvent {

    Object getData();

    RpcEvent setData(Object data);
}