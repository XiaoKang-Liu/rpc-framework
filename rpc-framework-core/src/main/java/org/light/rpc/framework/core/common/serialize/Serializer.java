package org.light.rpc.framework.core.common.serialize;

/**
 * 序列化接口
 * @author lxk
 * @date 2023/8/21 15:17
 */
public interface Serializer {

    /**
     * 序列化接口
     * @param object  数据
     * @param <T>     泛型
     * @return        数据序列化的字节数组
     */
    <T> byte[] serialize(T object);


    /**
     * 反序列化接口
     * @param clazz  数据类
     * @param bytes   字节数组
     * @param <T>    类型
     * @return       反序列化的对象
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
