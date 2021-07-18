package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 12:07
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 12:07
 * @modify by reason:{方法名}:{原因}
 */
public interface Pipeline<IN, OUT> extends Pipe<IN, OUT> {

    public void addPipe(Pipe<?, ?> pipe);
}
