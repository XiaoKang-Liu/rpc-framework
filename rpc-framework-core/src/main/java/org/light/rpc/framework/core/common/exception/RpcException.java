package org.light.rpc.framework.core.common.exception;

/**
 * @author lxk
 * @date 2023/8/21 10:00
 */
public class RpcException extends RuntimeException {

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
