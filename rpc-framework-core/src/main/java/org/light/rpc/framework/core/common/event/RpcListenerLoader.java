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
public class RpcListenerLoader {

    private static final List<RpcListener> rpcListenerList = new ArrayList<>();

    private static final ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(RpcListener rpcListener) {
        rpcListenerList.add(rpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    public static void sendEvent(RpcEvent rpcEvent) {
        if (rpcListenerList.isEmpty()) {
            return;
        }
        for (RpcListener rpcListener : rpcListenerList) {
            Class<?> type = getInterfaceGeneric(rpcListener);
            if (type != null && type.equals(RpcEvent.class)) {
                eventThreadPool.execute(() -> rpcListener.callBack(rpcEvent.getData()));
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