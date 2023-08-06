package org.light.rpc.framework.core.common.event;

/**
 * 节点更新事件
 * @author lxk
 * @date 2023/08/05 16:30
 **/
public class RpcUpdateEvent implements RpcEvent {

    private Object data;

    public RpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public RpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}