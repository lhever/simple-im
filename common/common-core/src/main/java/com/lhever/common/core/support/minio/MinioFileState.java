package com.lhever.common.core.support.minio;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/6/24 13:35
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/6/24 13:35
 * @modify by reason:{方法名}:{原因}
 */
public enum MinioFileState {

    //存在
    EXISTS,

    //可能不存在，一种不确定的状态，基于具体的业务逻辑抽象出来的
    MAYBE_NOT_EXISTS,

    //不存在
    NOT_EXISTS;
}
