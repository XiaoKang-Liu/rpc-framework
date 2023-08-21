package org.light.rpc.framework.core.common.serialize.impl;

import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.common.serialize.Serializer;

import java.io.*;

/**
 * jdk 序列化，无法跨语言，序列化后的码流过大
 * @author lxk
 * @date 2023/8/21 15:23
 */
public class JdkSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        try(ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);) {
            outputStream.writeObject(object);
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RpcException("jdk 序列化失败", e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));) {
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RpcException("jdk 反序列化失败", e);
        }
    }
}
