package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 11:45
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 11:45
 * @modify by reason:{方法名}:{原因}
 */
public interface Pipe<IN, OUT> {


    public void process(IN in);


    public void init(PipeContext pipeCtx);


    public void setNextPipe(Pipe<?, ?> pipe);


    void shutdown(long timeout, TimeUnit unit);


}
