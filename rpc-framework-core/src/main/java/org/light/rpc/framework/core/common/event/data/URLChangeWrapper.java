package org.light.rpc.framework.core.common.event.data;

import lombok.Data;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * @author lxk
 * @date 2023/08/05 16:04
 **/
@Data
public class URLChangeWrapper {

    private String path;

    private PathChildrenCacheEvent.Type type;
}