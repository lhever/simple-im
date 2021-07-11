package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 11:48
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 11:48
 * @modify by reason:{方法名}:{原因}
 */
public interface PipeContext {

    public void handleError(PipeException e);


}
