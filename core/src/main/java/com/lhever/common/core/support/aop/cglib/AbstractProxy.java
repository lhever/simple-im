package com.lhever.common.core.support.aop.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/6/9 11:40
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/6/9 11:40
 * @modify by reason:{方法名}:{原因}
 */
public abstract class AbstractProxy implements Proxy {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Class<?> cls = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();
        Object result = null;
        begin();
        try {
            if (intercept(cls, targetMethod, params)) {
                before(cls, targetMethod, params);
                result = proxyChain.doProxyChain();
                after(cls, targetMethod, params, result);
            } else {
                result =  proxyChain.doProxyChain();
            }
        } catch (Throwable e) {
            error(cls, targetMethod, params, e);
        } finally {
            end();
        }
        return result;
    }


    public void begin() {
    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) throws Throwable {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
    }

    public void error(Class<?> cls, Method method, Object[] params, Throwable e) {
    }

    public void end() {
    }
}
