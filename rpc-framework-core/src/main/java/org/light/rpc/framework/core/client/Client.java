package org.light.rpc.framework.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.config.ClientConfig;
import org.light.rpc.framework.core.common.handler.RpcResponseMessageHandler;
import org.light.rpc.framework.core.common.protocol.RpcMessageCodec;
import org.light.rpc.framework.core.proxy.JdkProxyFactory;
import org.light.rpc.framework.core.server.UserService;

/**
 * @author lxk
 * @date 2023/7/13 17:16
 */
@Slf4j
public class Client {

    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public Channel startClientApplication() throws InterruptedException {
        final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        final RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(rpcMessageCodec);
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(rpcResponseMessageHandler);
                    }
                });
        final ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddress(), clientConfig.getPort()).sync();
        log.info("==============服务启动===============");
        startClient(channelFuture);
        return channelFuture.channel();
    }

    private void startClient(ChannelFuture channelFuture) {
        final Thread thread = new Thread(new AsyncSendJob(channelFuture), "sendThead");
        thread.start();
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setServerAddress("localhost");
        clientConfig.setPort(8080);
        client.setClientConfig(clientConfig);
        final Channel channel = client.startClientApplication();
        final UserService proxy = JdkProxyFactory.getProxy(channel, UserService.class);
        final String hello = proxy.hello();
        System.out.println(hello);
    }
}
