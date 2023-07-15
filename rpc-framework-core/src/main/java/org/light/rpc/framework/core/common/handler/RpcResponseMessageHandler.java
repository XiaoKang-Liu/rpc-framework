package org.light.rpc.framework.core.common.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.message.RpcResponseMessage;

/**
 * @author lxk
 * @date 2023/7/14 15:40
 */
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseMessage rpcResponseMessage) throws Exception {
        final Promise<Object> promise = CommonClientCache.RESP_MAP.get(rpcResponseMessage.getSequenceId());
        final Object returnValue = rpcResponseMessage.getReturnValue();
        if (promise != null) {
            final Exception exceptionValue = rpcResponseMessage.getExceptionValue();
            if (exceptionValue != null) {
                promise.setFailure(exceptionValue);
            }
            promise.setSuccess(returnValue);
        }
    }
}
