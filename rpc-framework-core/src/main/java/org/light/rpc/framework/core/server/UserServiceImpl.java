package org.light.rpc.framework.core.server;

/**
 * @author lxk
 * @date 2023/7/14 17:07
 */
public class UserServiceImpl implements UserService{

    @Override
    public String hello() {
        return "hello world!";
    }
}
