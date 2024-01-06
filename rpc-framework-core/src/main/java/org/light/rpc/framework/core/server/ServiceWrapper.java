package org.light.rpc.framework.core.server;

import lombok.Data;

/**
 * @author lxk
 * @date 2023/8/24 10:15
 */
@Data
public class ServiceWrapper {

    private Object serviceObj;

    private String group = "default";

    private String serviceToken = "";

    private Integer limit = -1;
}
