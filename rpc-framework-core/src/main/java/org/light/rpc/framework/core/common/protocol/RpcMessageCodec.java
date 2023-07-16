package org.light.rpc.framework.core.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.light.rpc.framework.core.common.message.AbstractMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 消息编解码器
 * @author lxk
 * @date 2023/7/13 11:27
 */
@ChannelHandler.Sharable
public class RpcMessageCodec extends MessageToMessageCodec<ByteBuf, AbstractMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractMessage abstractMessage, List<Object> list) throws Exception {
        final ByteBuf byteBuf = channelHandlerContext.alloc().buffer();
        // 魔数
        byteBuf.writeInt(AbstractMessage.MAGIC_NUM);
        // 消息类型，header 最好为 8 字节的整数倍
        byteBuf.writeInt(abstractMessage.getMessageType());
        // 采用 JDK 序列化
        final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);
        outputStream.writeObject(abstractMessage);
        final byte[] bytes = arrayOutputStream.toByteArray();
        // 消息长度
        byteBuf.writeInt(bytes.length);
        // 消息内容
        byteBuf.writeBytes(bytes);
        list.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final int magicNum = byteBuf.readInt();
        if (magicNum != AbstractMessage.MAGIC_NUM) {
            return;
        }
        final int messageType = byteBuf.readInt();
        final int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);
        final ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream);
        final AbstractMessage abstractMessage = (AbstractMessage) objectInputStream.readObject();
        list.add(abstractMessage);
    }
}
