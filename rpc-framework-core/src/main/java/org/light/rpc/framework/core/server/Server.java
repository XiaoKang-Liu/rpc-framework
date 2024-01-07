package org.light.rpc.framework.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.common.exception.RpcException;
import org.light.rpc.framework.core.common.handler.RpcRequestMessageHandler;
import org.light.rpc.framework.core.common.protocol.RpcMessageCodec;
import org.light.rpc.framework.core.common.protocol.RpcMessageFrameDecoder;
import org.light.rpc.framework.core.common.util.RpcCommonUtil;
import org.light.rpc.framework.core.registry.RegistryService;
import org.light.rpc.framework.core.registry.URL;
import org.light.rpc.framework.core.registry.zookeeper.ZookeeperRegister;

/**
 * @author lxk
 * @date 2023/7/12 17:32
 */
@Slf4j
public class Server {

    private RegistryService registryService;

    private final String port = System.getProperty("server.port");

    public void startApplication() throws InterruptedException {
        final NioEventLoopGroup boss = new NioEventLoopGroup(1);
        final NioEventLoopGroup worker = new NioEventLoopGroup();
        final ServerBootstrap bootstrap = new ServerBootstrap();
        final RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        final RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new RpcMessageFrameDecoder());
                nioSocketChannel.pipeline().addLast(rpcMessageCodec);
                nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                nioSocketChannel.pipeline().addLast(rpcRequestMessageHandler);
            }
        });
        bootstrap.bind( Integer.parseInt(System.getProperty("server.port"))).sync();
    }
    
    public void registerService(ServiceWrapper serviceWrapper) {
        final Object serviceBean = serviceWrapper.getServiceObj();
        final Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RpcException("service must have interfaces!");
        }
        if (interfaces.length > 1) {
            throw new RpcException("service must only have one interface!");
        }
        if (registryService == null) {
            registryService = new ZookeeperRegister("120.25.155.123:2181");
        }

        final Class<?> interfaceClass = interfaces[0];
        CommonServerCache.PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        final URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName("provider1");
        url.addParameter("ip", RpcCommonUtil.getLocalIpAddress());
        url.addParameter("port", port);
        url.addParameter("group", serviceWrapper.getGroup());
        url.addParameter("limit", String.valueOf(serviceWrapper.getLimit()));
        registryService.registry(url);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        final ServiceWrapper serviceWrapper = new ServiceWrapper();
        serviceWrapper.setServiceObj(new UserServiceImpl());
        serviceWrapper.setServiceToken("userToken");
        serviceWrapper.setGroup("user");
        server.registerService(serviceWrapper);

        CommonServerCache.DISPATCHER.init(100, 5);
        server.startApplication();
    }
}
