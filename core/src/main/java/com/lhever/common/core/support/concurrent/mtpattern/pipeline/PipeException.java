package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 11:49
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 11:49
 * @modify by reason:{方法名}:{原因}
 */
public class PipeException extends Exception {

    public final Pipe<?, ?> sourcePipe;

    public final Object input;

    public PipeException(String message, Throwable cause, Pipe<?, ?> sourcePipe, Object input) {
        super(message, cause);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }


    public PipeException(String message, Pipe<?, ?> sourcePipe, Object input) {
        super(message);
        this.sourcePipe = sourcePipe;
        this.input = input;
    }

    public Pipe<?, ?> getSourcePipe() {
        return sourcePipe;
    }

    public Object getInput() {
        return input;
    }




}
