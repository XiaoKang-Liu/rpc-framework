package org.light.rpc.framework.core.server;

import io.netty.channel.ChannelHandlerContext;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

/**
 * @author lxk
 * @date 2024/1/6 16:08
 */
public class ServerChannelDispatchData {

    private RpcRequestMessage rpcRequestMessage;

    private ChannelHandlerContext channelHandlerContext;

    public RpcRequestMessage getRpcRequestMessage() {
        return rpcRequestMessage;
    }

    public void setRpcRequestMessage(RpcRequestMessage rpcRequestMessage) {
        this.rpcRequestMessage = rpcRequestMessage;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}
