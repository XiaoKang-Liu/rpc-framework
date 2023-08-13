package org.light.rpc.framework.core.proxy;

import io.netty.channel.Channel;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author lxk
 * @date 2023/7/14 15:42
 */
public class JdkProxyFactory {

    public static  <T> T getProxy(Class<T> serviceClass) {
        final Object proxyInstance = Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage();
                        rpcRequestMessage.setTargetServiceName(serviceClass.getName());
                        rpcRequestMessage.setTargetMethodName(method.getName());
                        rpcRequestMessage.setParameterTypes(method.getParameterTypes());
                        rpcRequestMessage.setParameterValue(args);
                        // 设置消息序号
                        final String sequenceId = UUID.randomUUID().toString();
                        rpcRequestMessage.setSequenceId(sequenceId);
                        // 消息放入到发送队列中
                        CommonClientCache.SEND_QUEUE.add(rpcRequestMessage);
                        // 使用 promise 接收结果
                        final DefaultPromise<Object> promise = new DefaultPromise<>(new DefaultEventLoop());
                        CommonClientCache.RESP_MAP.put(sequenceId, promise);
                        // 超时 3 秒
                        promise.await(3000);
                        if (promise.isSuccess()) {
                            return promise.getNow();
                        } else {
                            final Throwable cause = promise.cause();
                            throw new RuntimeException(cause);
                        }
                    }
                });
        return (T) proxyInstance;
    }
}
