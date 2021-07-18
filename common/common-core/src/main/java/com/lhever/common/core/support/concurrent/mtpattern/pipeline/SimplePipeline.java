package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 12:21
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 12:21
 * @modify by reason:{方法名}:{原因}
 */
public class SimplePipeline<IN, OUT> extends AbstractPipe<IN, OUT> implements Pipeline<IN, OUT> {
    private Logger logger = LoggerFactory.getLogger(SimplePipeline.class);

    private final Queue<Pipe<?, ?>> pipes = new LinkedList<Pipe<?, ?>>();
    private final ExecutorService helperExecutor;

    public SimplePipeline(final ExecutorService helperExecutor) {
        super();
        this.helperExecutor = helperExecutor;
    }

    public SimplePipeline() {
        this(Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "SimplePipeline-Helper");
                t.setDaemon(false);
                return t;
            }

        }));
    }

    @Override
    public void init(PipeContext pipeCtx) {
        LinkedList<Pipe<?, ?>> pipesList = (LinkedList<Pipe<?, ?>>) pipes;
        Pipe<?, ?> prevPipe = this;
        for (Pipe<?, ?> pipe : pipesList)
        {
            prevPipe.setNextPipe(pipe);
            prevPipe = pipe;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                for (Pipe pipe : pipes) {
                    pipe.init(pipeCtx);
                }
            }
        };
        helperExecutor.submit(task);
    }




    @Override
    public void addPipe(Pipe<?, ?> pipe) {
        this.pipes.add(pipe);
    }

    public <IN, OUT> void addAsThreadPoolBasedPipe(Pipe<IN, OUT> delegate, ExecutorService executorSerivce)
    {
        addPipe(new ThreadPoolPipeDecorator<IN, OUT>(delegate, executorSerivce));
    }


    @Override
    public OUT doProcess(IN in) throws PipeException {
        return null;
    }


    @Override
    public void process(IN in) {
        Pipe<IN, ?> firstPipe = (Pipe<IN, ?>) pipes.peek();
        firstPipe.process(in);
    }


    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        Pipe<?, ?> pipe;

        while (null != (pipe = pipes.poll())) {
//            System.out.println("SimplePipeLine调用管道的关闭方法的线程是：  " + Thread.currentThread().getName());
            pipe.shutdown(timeout, unit);
        }

        helperExecutor.shutdown();
    }

    public PipeContext newDefaultPipelineContext() {
        return new PipeContext() {
            @Override
            public void handleError(PipeException e) {

                helperExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        logger.error("error occur at pipie with name: " + e.getSourcePipe() + ", input: " + e.getInput(), e);
                    }
                });

            }
        };
    }
}
