package org.light.rpc.framework.core.common.serialize.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
import org.light.rpc.framework.core.common.message.RpcResponseMessage;
import org.light.rpc.framework.core.common.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * kryo 序列化
 * @author lxk
 * @date 2023/8/21 16:02
 */
public class KryoSerializer implements Serializer {

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcRequestMessage.class);
        kryo.register(RpcResponseMessage.class);
        return kryo;
    });

    @Override
    public <T> byte[] serialize(T object) {
        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(arrayOutputStream);) {
            final Kryo kryo = KRYO_THREAD_LOCAL.get();
            kryo.writeObject(output, object);
            KRYO_THREAD_LOCAL.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new RpcException("kryo 序列化失败", e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(arrayInputStream);) {
            final Kryo kryo = KRYO_THREAD_LOCAL.get();
            final T object = kryo.readObject(input, clazz);
            KRYO_THREAD_LOCAL.remove();
            return object;
        } catch (Exception e) {
            throw new RpcException("kryo 反序列化失败", e);
        }
    }
}
