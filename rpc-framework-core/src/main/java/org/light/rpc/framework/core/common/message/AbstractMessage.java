package org.light.rpc.framework.core.common.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lxk
 * @date 2023/7/13 11:28
 */
public abstract class AbstractMessage implements Serializable {

    private static final Map<Integer, Class<? extends AbstractMessage>> MESSAGE_CLASSES = new HashMap<>();

    /**
     * 魔数，自定义
     */
    public static final int MAGIC_NUM = 2023;

    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;

    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    /**
     * 消息序号
     */
    private String sequenceId;

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    static {
        MESSAGE_CLASSES.put(RPC_MESSAGE_TYPE_REQUEST, RpcRequestMessage.class);
        MESSAGE_CLASSES.put(RPC_MESSAGE_TYPE_RESPONSE, RpcResponseMessage.class);
    }

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends AbstractMessage> getMessageClass(int messageType) {
        return MESSAGE_CLASSES.get(messageType);
    }

    /**
     * 获取消息类型
     * @return  消息类型码
     */
    public abstract int getMessageType();
}
