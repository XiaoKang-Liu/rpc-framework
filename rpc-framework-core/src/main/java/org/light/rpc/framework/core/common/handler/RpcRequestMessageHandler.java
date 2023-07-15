package org.light.rpc.framework.core.common.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
import org.light.rpc.framework.core.common.message.RpcResponseMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lxk
 * @date 2023/7/13 15:52
 */
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        RpcResponseMessage responseMessage = new RpcResponseMessage();
        responseMessage.setSequenceId(rpcRequestMessage.getSequenceId());
        final String targetServiceName = rpcRequestMessage.getTargetServiceName();
        final Object targetService = CommonServerCache.PROVIDER_CLASS_MAP.get(targetServiceName);
        final Method method;
        method = targetService.getClass().getDeclaredMethod(rpcRequestMessage.getTargetMethodName(), rpcRequestMessage.getParameterTypes());
        final Object invoke = method.invoke(targetService, rpcRequestMessage.getParameterValue());
        responseMessage.setReturnValue(invoke);
        channelHandlerContext.writeAndFlush(responseMessage);
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
