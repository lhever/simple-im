package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

import com.lhever.common.core.support.concurrent.mtpattern.tpt.TerminationToken;

import java.util.concurrent.*;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 13:48
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 13:48
 * @modify by reason:{方法名}:{原因}
 */
public class ThreadPoolPipeDecorator<IN, OUT> implements Pipe<IN, OUT> {

    private Pipe<IN, OUT> delegate;
    private ExecutorService executorService;

    // 线程池停止标志。
    private final PipieTerminationToken terminationToken;
    private final CountDownLatch stageProcessLatch = new CountDownLatch(1);

    public ThreadPoolPipeDecorator(Pipe<IN, OUT> delegate, ExecutorService executorSerivce) {
        this.delegate = delegate;
        this.executorService = executorSerivce;
        this.terminationToken = PipieTerminationToken.newInstance(executorSerivce);
    }


    @Override
    public void process(IN in) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int remaining = -1;
                try {
                    delegate.process(in);
                } finally {
                    remaining = terminationToken.reservations.decrementAndGet();
                }

                if (terminationToken.isToShutdown() && 0 == remaining) {
                    stageProcessLatch.countDown();
                }

            }
        };

        executorService.submit(task);
        terminationToken.reservations.incrementAndGet();


    }

    @Override
    public void init(PipeContext pipeCtx) {
        this.delegate.init(pipeCtx);
    }

    @Override
    public void setNextPipe(Pipe<?, ?> pipe) {

        delegate.setNextPipe(pipe);

    }

    @Override
    public void shutdown(long timeout, TimeUnit unit) {
        terminationToken.setIsToShutdown();
        if (terminationToken.reservations.get() > 0) {
            try {

                if (stageProcessLatch.getCount() > 0) {
                    stageProcessLatch.await(timeout, unit);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        delegate.shutdown(timeout, unit);
    }

    /**
     * 线程池停止标志。 每个ExecutorService实例对应唯一的一个TerminationToken实例。 这里使用了Two-phase
     * Termination模式（第5章）的思想来停止多个Pipe实例所共用的 线程池实例。
     *
     * @author Viscent Huang
     */
    private static class PipieTerminationToken extends TerminationToken {
        private final static ConcurrentMap<ExecutorService, PipieTerminationToken> INSTANCES_MAP =
                new ConcurrentHashMap<ExecutorService, PipieTerminationToken>();

        // 私有构造器
        private PipieTerminationToken() {

        }

        void setIsToShutdown() {
            this.toShutdown = true;
        }

        static PipieTerminationToken newInstance(ExecutorService executorSerivce) {
            PipieTerminationToken token = INSTANCES_MAP.get(executorSerivce);
            if (null == token) {
                token = new PipieTerminationToken();
                PipieTerminationToken existingToken = INSTANCES_MAP.putIfAbsent(executorSerivce, token);
                if (null != existingToken) {
                    token = existingToken;
                }
            }
            System.out.println(Thread.currentThread().getName() + token);

            return token;
        }
    }
}
