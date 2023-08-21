package org.light.rpc.framework.core.common.util;

import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.exception.RpcException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * RPC 通用工具类
 * @author lxk
 * @date 2023/8/18 14:32
 */
@Slf4j
public class RpcCommonUtil {

    private RpcCommonUtil() {

    }

    public static String getLocalIpAddress() {
        List<String> list = new ArrayList<>();
        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            // 遍历网络接口
            while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 排除掉回调、虚拟、非开启运行的接口
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                // 遍历网络地址
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress addr = inetAddresses.nextElement();
                    if (addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
                        continue;
                    }
                    list.add(addr.getHostAddress());
                }
            }
            return list.get(0);
        } catch (SocketException e) {
            throw new RpcException("本地ip地址获取失败");
        }
    }
}
