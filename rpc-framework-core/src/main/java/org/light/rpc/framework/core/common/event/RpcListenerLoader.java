package org.light.rpc.framework.core.common.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 监听器加载类
 * @author lxk
 * @date 2023/08/05 16:32
 **/
@SuppressWarnings("rawtypes")
public class RpcListenerLoader {

    private static final List<RpcListener> RPC_LISTENER_LIST = new ArrayList<>();

    private static final ExecutorService EVENT_THREAD_POOL = Executors.newFixedThreadPool(2);

    public static void registerListener(RpcListener rpcListener) {
        RPC_LISTENER_LIST.add(rpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    public static void sendEvent(RpcEvent rpcEvent) {
        if (RPC_LISTENER_LIST.isEmpty()) {
            return;
        }
        for (RpcListener rpcListener : RPC_LISTENER_LIST) {
            Class<?> type = getInterfaceGeneric(rpcListener);
            if (type != null && RpcEvent.class.isAssignableFrom(type)) {
                EVENT_THREAD_POOL.execute(() -> rpcListener.callBack(rpcEvent.getData()));
            }
        }
    }

    private static Class<?> getInterfaceGeneric(RpcListener rpcListener) {
        Type[] types = rpcListener.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }
}