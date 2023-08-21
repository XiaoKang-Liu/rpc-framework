package org.light.rpc.framework.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;

/**
 * @author lxk
 * @date 2023/8/9 14:45
 */
@Data
public class ChannelFutureWrapper {

    private ChannelFuture channelFuture;

    private String ip;

    private Integer port;

    private Integer weight;
}
