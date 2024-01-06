package org.light.rpc.framework.core.common.message;

import java.util.Map;

/**
 * RPC 请求报文
 * @author lxk
 * @date 2023/7/13 11:04
 */
public class RpcRequestMessage extends AbstractMessage {

    /**
     * 目标服务全限定类名
     */
    private String targetServiceName;

    /**
     * 目标服务方法名
     */
    private String targetMethodName;

    /**
     * 方法参数类型
     */
    private Class[] parameterTypes;

    /**
     * 方法参数值
     */
    private Object[] parameterValue;

    /**
     * 过滤器参数
     */
    private Map<String, Object> attachments;

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public String getTargetServiceName() {
        return targetServiceName;
    }

    public void setTargetServiceName(String targetServiceName) {
        this.targetServiceName = targetServiceName;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Object[] parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_REQUEST;
    }
}
