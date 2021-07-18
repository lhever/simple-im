package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 12:13
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 12:13
 * @modify by reason:{方法名}:{原因}
 */
public abstract class AbstractPipe<IN, OUT>  implements Pipe<IN, OUT> {

    protected volatile Pipe<?, ?> nextPipe = null;

    protected volatile PipeContext pipeCtx;
    protected String name;

    public AbstractPipe() {
    }

    public AbstractPipe(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract OUT doProcess(IN in) throws PipeException;


    @Override
    public void process(IN in) {
        try {
            OUT out = doProcess(in);
            if (null != nextPipe) {
                if (null != out) {
                    ((Pipe<OUT, ?>) nextPipe).process(out);
                }
            }
        } catch (PipeException e) {
            pipeCtx.handleError(e);
        }
    }


    @Override
    public void init(PipeContext pipeCtx) {
        this.pipeCtx = pipeCtx;
    }

    @Override
    public void setNextPipe(Pipe<?, ?> pipe) {
        this.nextPipe = pipe;
    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {

    }

    @Override
    public String toString() {
        return name;
    }
}
