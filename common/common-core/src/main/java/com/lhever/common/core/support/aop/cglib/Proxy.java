package com.lhever.common.core.support.aop.cglib;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/6/9 11:04
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/6/9 11:04
 * @modify by reason:{方法名}:{原因}
 */
public interface Proxy {

    public Object doProxy(ProxyChain proxyChain) throws Throwable;
}


