package com.lhever.common.core.support.initializer.ctx;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.StringUtils;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/5/15 12:20
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/5/15 12:20
 * @modify by reason:{方法名}:{原因}
 */
public abstract class InitContext {

    /**
     * Application字符串,用来计算服务名，如SearchApplication,search就是服务名
     */
    protected static final String APPLICATION_SUFFIX = "Application";


    //微服务名称
    protected String service = null;

    protected String simpleServiceName = null;
    //jar包所在目录
    protected String root = null;
    //jar包全路径名
    protected String fullJarName = null;
    //jar包名称
    protected String jarName = null;
    //端口
    protected Integer port = -1;


    public InitContext(Class clazz, String[] args) {
    }


    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSimpleServiceName() {
        return simpleServiceName;
    }

    public void setSimpleServiceName(String simpleServiceName) {
        this.simpleServiceName = simpleServiceName;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFullJarName() {
        return fullJarName;
    }

    public void setFullJarName(String fullJarName) {
        this.fullJarName = fullJarName;
    }

    public String getJarName() {
        return jarName;
    }

    public void setJarName(String jarName) {
        this.jarName = jarName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceId() {
        return getService() + "-" + getPort();
    }


    public static String parseSimpleServiceName(String serviceName) {
        return serviceName.replace("-", CommonConsts.UNDER_SCORE);
    }

    /**
     * 驼峰转中划线
     *
     * @param para
     * @return
     * @author lihong10 2019/1/5 19:21
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/5 19:21
     * @modify by reason:{原因}
     */
    public static String camelToLinethrough(String para) {
        if (StringUtils.isBlank(para)) {
            return para;
        }
        para = para.trim();
        para = String.valueOf(Character.toLowerCase(para.charAt(0))) + para.substring(1);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "-");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

}
