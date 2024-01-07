package org.light.rpc.framework.core.common.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
import org.light.rpc.framework.core.server.ServerChannelDispatchData;

import java.lang.reflect.InvocationTargetException;

/**
 * @author lxk
 * @date 2023/7/13 15:52
 */
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final ServerChannelDispatchData serverChannelDispatchData = new ServerChannelDispatchData();
        serverChannelDispatchData.setRpcRequestMessage(rpcRequestMessage);
        serverChannelDispatchData.setChannelHandlerContext(channelHandlerContext);
        // 消息分发
        CommonServerCache.DISPATCHER.add(serverChannelDispatchData);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
