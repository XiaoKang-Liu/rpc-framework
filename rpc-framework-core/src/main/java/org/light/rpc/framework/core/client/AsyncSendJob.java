package org.light.rpc.framework.core.client;

import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

/**
 * 消息发送线程
 * @author lxk
 * @date 2023/7/14 14:39
 */
@Slf4j
public class AsyncSendJob implements Runnable {

    @Override
    public void run() {
        while (true) {
            // 阻塞
            try {
                final RpcRequestMessage requestMessage = CommonClientCache.SEND_QUEUE.take();

                final ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(requestMessage);
                channelFuture.channel().writeAndFlush(requestMessage)
                        // 不加监听的话消息发送异常不会打印错误信息
                        .addListener(promise -> {
                            if (!promise.isSuccess()) {
                                log.error("send error:", promise.cause());
                            }
                        });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
