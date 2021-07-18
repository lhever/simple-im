package com.lhever.common.core.support.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/15 14:22
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/15 14:22
 * @modify by reason:{方法名}:{原因}
 */
public enum LogFactory {

    DEFALUT;

    public static <T> Logger getLogger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

}
