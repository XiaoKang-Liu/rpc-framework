package org.light.rpc.framework.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.light.rpc.framework.core.common.cache.CommonClientCache;
import org.light.rpc.framework.core.common.config.ClientConfig;
import org.light.rpc.framework.core.common.event.RpcListenerLoader;
import org.light.rpc.framework.core.common.handler.RpcResponseMessageHandler;
import org.light.rpc.framework.core.common.protocol.RpcMessageCodec;
import org.light.rpc.framework.core.common.protocol.RpcMessageFrameDecoder;
import org.light.rpc.framework.core.common.util.RpcCommonUtil;
import org.light.rpc.framework.core.common.wrapper.RpcRequestMessageWrapper;
import org.light.rpc.framework.core.proxy.JdkProxyFactory;
import org.light.rpc.framework.core.registry.URL;
import org.light.rpc.framework.core.registry.zookeeper.AbstractRegister;
import org.light.rpc.framework.core.registry.zookeeper.ZookeeperRegister;
import org.light.rpc.framework.core.server.UserService;

import java.util.List;

/**
 * @author lxk
 * @date 2023/7/13 17:16
 */
@Slf4j
public class Client {

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private RpcListenerLoader rpcListenerLoader;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public Bootstrap initClientApplication() throws InterruptedException {
        final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        final RpcMessageCodec rpcMessageCodec = new RpcMessageCodec();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(new RpcMessageFrameDecoder());
                        nioSocketChannel.pipeline().addLast(rpcMessageCodec);
                        nioSocketChannel.pipeline().addLast(rpcResponseMessageHandler);
                    }
                });
        // 加载监听器器
        rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();
        return bootstrap;
    }

    private void startClient() {
        final Thread thread = new Thread(new AsyncSendJob(), "sendThead");
        thread.start();
    }

    public void doSubscribeService(Class serviceBean) {
        if (abstractRegister == null) {
            abstractRegister = new ZookeeperRegister("120.25.155.123:2181");
        }
        URL url = new URL();
        url.setApplicationName("consumer1");
        url.setServiceName(serviceBean.getName());
        url.addParameter("ip", RpcCommonUtil.getLocalIpAddress());
        abstractRegister.subscribe(url);
    }

    public void doConnectServer() {
        for (String providerServiceName : CommonClientCache.SUBSCRIBE_SERVICE_LIST) {
            final List<String> providerAddrs = abstractRegister.getProviderAddrs(providerServiceName);
            for (String providerAddr : providerAddrs) {
                try {
                    ConnectionHandler.connect(providerServiceName, providerAddr);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("[doConnectServer] connect fail ", e);
                }
            }
            URL url = new URL();
            url.setServiceName(providerServiceName);
            //客户端在此新增一个订阅的功能
            abstractRegister.doAfterSubscribe(url);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setServerAddress(RpcCommonUtil.getLocalIpAddress());
        clientConfig.setPort(8080);
        client.setClientConfig(clientConfig);
        final Bootstrap bootstrap = client.initClientApplication();
        ConnectionHandler.setBootstrap(bootstrap);
        // 请求包装
        RpcRequestMessageWrapper<UserService> requestMessageWrapper = new RpcRequestMessageWrapper<>();
        requestMessageWrapper.setServiceClass(UserService.class);
        requestMessageWrapper.setGroup("dev");
        final UserService proxy = JdkProxyFactory.getProxy(requestMessageWrapper);
        client.doSubscribeService(UserService.class);
        client.doConnectServer();
        client.startClient();
        final String hello = proxy.hello();
        System.out.println(hello);
    }
}
