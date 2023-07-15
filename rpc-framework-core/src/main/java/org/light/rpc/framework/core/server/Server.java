package org.light.rpc.framework.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.light.rpc.framework.core.common.cache.CommonServerCache;
import org.light.rpc.framework.core.common.handler.RpcRequestMessageHandler;
import org.light.rpc.framework.core.common.protocol.RpcMessageCodec;

/**
 * @author lxk
 * @date 2023/7/12 17:32
 */
public class Server {

    public static void startApplication() {
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
                nioSocketChannel.pipeline().addLast(new LoggingHandler());
                nioSocketChannel.pipeline().addLast(rpcMessageCodec);
                nioSocketChannel.pipeline().addLast(rpcRequestMessageHandler);
            }
        });
        bootstrap.bind(8080);
    }
    
    public static void registerService(Object serviceBean) {
        final Class<?>[] interfaces = serviceBean.getClass().getInterfaces();
        if (interfaces.length == 0) {
            throw new RuntimeException("service must have interfaces!");
        }
        if (interfaces.length > 1) {
            throw new RuntimeException("service must only have one interface!");
        }
        final Class<?> interfaceClass = interfaces[0];
        CommonServerCache.PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
    }

    public static void main(String[] args) {
        startApplication();
        registerService(new UserServiceImpl());
    }
}
