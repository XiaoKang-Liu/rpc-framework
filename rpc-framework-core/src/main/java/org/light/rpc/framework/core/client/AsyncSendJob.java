package org.light.rpc.framework.core.client;

import io.netty.channel.ChannelFuture;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

/**
 * 消息发送线程
 * @author lxk
 * @date 2023/7/14 14:39
 */
public class AsyncSendJob implements Runnable {

    private ChannelFuture channelFuture;

    public AsyncSendJob(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    @Override
    public void run() {
        while (true) {
            // 阻塞
            try {
                final RpcRequestMessage requestMessage = CommonClientCache.SEND_QUEUE.take();
                channelFuture.channel().writeAndFlush(requestMessage);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
