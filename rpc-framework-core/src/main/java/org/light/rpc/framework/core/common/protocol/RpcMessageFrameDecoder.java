package org.light.rpc.framework.core.common.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 长度字段解码器，解决半包问题
 * @author lxk
 * @date 2024/1/7 16:07
 */
public class RpcMessageFrameDecoder extends LengthFieldBasedFrameDecoder {

    public RpcMessageFrameDecoder() {
        this(2048, 8, 4, 0, 0);
    }

    public RpcMessageFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
