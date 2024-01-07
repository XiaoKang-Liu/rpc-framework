package org.light.rpc.framework.core.common.wrapper;

import lombok.Data;
import org.light.rpc.framework.core.common.message.RpcRequestMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求包装类，包含过滤器要使用的信息
 *
 * @author lxk
 * @date 2023/8/22 15:54
 */
@Data
public class RpcRequestMessageWrapper<T> {

    private Class<T> serviceClass;

    private Map<String, Object> attachments = new ConcurrentHashMap<>();

    public Class<T> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<T> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, Object> attachments) {
        this.attachments = attachments;
    }

    public String getUrl() {
        return String.valueOf(attachments.get("url"));
    }

    public void setUrl(String url) {
        attachments.put("url", url);
    }

    public String getServiceToken() {
        return String.valueOf(attachments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken) {
        attachments.put("serviceToken", serviceToken);
    }

    public String getGroup() {
        return String.valueOf(attachments.get("group"));
    }

    public void setGroup(String group) {
        attachments.put("group", group);
    }
}
