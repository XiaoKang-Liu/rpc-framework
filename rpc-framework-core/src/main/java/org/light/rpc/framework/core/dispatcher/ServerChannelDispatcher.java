package org.light.rpc.framework.core.dispatcher;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;
import org.light.rpc.framework.core.common.message.RpcResponseMessage;
import org.light.rpc.framework.core.server.ServerChannelDispatchData;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 服务端消息处理分发器
 * @author lxk
 * @date 2024/1/6 16:00
 */
public class ServerChannelDispatcher {

    private BlockingQueue<ServerChannelDispatchData> dispatchDataQueue;

    private ExecutorService executorService;

    public void init(int queueSize, int bizThreadNums) {
        dispatchDataQueue = new ArrayBlockingQueue<>(queueSize);
        executorService = new ThreadPoolExecutor(bizThreadNums, bizThreadNums,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(512),
                new DefaultThreadFactory("server-channel-dispatcher"));
        final Thread thread = new Thread(new ServerJobCoreHandle());
        thread.start();

    }

    public void add(ServerChannelDispatchData serverChannelDispatchData) {
        dispatchDataQueue.add(serverChannelDispatchData);
    }


    class ServerJobCoreHandle implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    final ServerChannelDispatchData serverChannelDispatchData = dispatchDataQueue.take();
                    executorService.execute(() -> {
                        try {
                            final RpcRequestMessage rpcRequestMessage = serverChannelDispatchData.getRpcRequestMessage();
                            // 服务端过滤器链
//                            CommonServerCache.SERVER_FILTER_CHAIN.doFilter(rpcRequestMessage.getAttachments());

                            RpcResponseMessage responseMessage = new RpcResponseMessage();
                            responseMessage.setSequenceId(rpcRequestMessage.getSequenceId());
                            final String targetServiceName = rpcRequestMessage.getTargetServiceName();
                            final Object targetService = CommonServerCache.PROVIDER_CLASS_MAP.get(targetServiceName);
                            final Method method;
                            method = targetService.getClass().getDeclaredMethod(rpcRequestMessage.getTargetMethodName(), rpcRequestMessage.getParameterTypes());
                            final Object invoke = method.invoke(targetService, rpcRequestMessage.getParameterValue());
                            responseMessage.setReturnValue(invoke);
                            serverChannelDispatchData.getChannelHandlerContext().writeAndFlush(responseMessage);
                        } catch (Exception e) {
                            throw new RpcException(e.getCause());
                        }
                    });
                } catch (Exception e) {
                    if (e instanceof RpcException) {
                        throw (RpcException) e;
                    } else {
                        throw new RpcException(e.getCause());
                    }
                }
            }
        }
    }
}


