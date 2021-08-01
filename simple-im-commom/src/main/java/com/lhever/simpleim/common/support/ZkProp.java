package com.lhever.simpleim.common.support;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/8/1 16:35
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/8/1 16:35
 * @modify by reason:{方法名}:{原因}
 */
@Getter
@Setter
@NoArgsConstructor
public class ZkProp {
    private String address;
    private String namespace;
    private String rootpath;
    private Integer retry = 3;
    private Integer sleepMsBetweenRetries = 15000;

    public ZkProp(String address, String namespace, String rootpath) {
        this.address = address;
        this.namespace = namespace;
        this.rootpath = rootpath;
    }
}
