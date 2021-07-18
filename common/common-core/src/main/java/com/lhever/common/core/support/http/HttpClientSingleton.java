package com.lhever.common.core.support.http;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/10/20 9:55
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/10/20 9:55
 * @modify by reason:{方法名}:{原因}
 */
public class HttpClientSingleton {

    /*private static volatile PooledCustomHttpClient client;

    public final static PooledCustomHttpClient get() {
        if (client == null) {
            synchronized (HttpClientSingleton.class) {
                if (client == null) {
                    client = new PooledCustomHttpClient();
                }
            }
        }
        return client;
    }*/


    /*构造方法不公开*/
    private HttpClientSingleton() {
    }

    private static class InstanceHolder {
        public static final CustomHttpClient client = new CustomHttpClient();
    }

    public final static CustomHttpClient get() {
        return InstanceHolder.client;
    }


}
