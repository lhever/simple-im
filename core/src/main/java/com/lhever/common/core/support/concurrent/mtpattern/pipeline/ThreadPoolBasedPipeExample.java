package com.lhever.common.core.support.concurrent.mtpattern.pipeline;

import java.util.Random;
import java.util.concurrent.*;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/8/6 14:26
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/8/6 14:26
 * @modify by reason:{方法名}:{原因}
 */
public class ThreadPoolBasedPipeExample {

    public static void main(String... args) {

        /*
         * 创建线程池
         */
        final ThreadPoolExecutor executorSerivce = new ThreadPoolExecutor(1,
                Runtime.getRuntime().availableProcessors() * 2,
                60, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(5), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }

        }, new ThreadPoolExecutor.CallerRunsPolicy());

        SimplePipeline pipeline = new SimplePipeline();

        /*
         * 创建第一条管道
         */
        Pipe<String, String> pipe = new AbstractPipe<String, String>() {

            @Override
            public String doProcess(String input) throws PipeException {
                String result = input + "->[pipe1," + Thread.currentThread().getName() + "]";
                System.out.println(result);
                return result;
            }
        };
        ((AbstractPipe<String, String>) pipe).setName("pipe1");

        pipeline.addAsThreadPoolBasedPipe(pipe, executorSerivce);

        /*
         * 创建第二条管道
         */
        pipe = new AbstractPipe<String, String>() {

            @Override
            public String doProcess(String input) throws PipeException {
                String result = input + "->[pipe2," + Thread.currentThread().getName() + "]";
                System.out.println(result);
                try {
                    Thread.sleep(new Random().nextInt(100));
                } catch (InterruptedException e) {
                    ;
                }
                return result;
            }
        };
        ((AbstractPipe<String, String>) pipe).setName("pipe2");

        /*
         * 将第二条管道加入管道线
         */
        pipeline.addAsThreadPoolBasedPipe(pipe, executorSerivce);

        /*
         * 创建第三条管道
         */
        pipe = new AbstractPipe<String, String>() {

            @Override
            public String doProcess(String input) throws PipeException {
                String result = input + "->[pipe3," + Thread.currentThread().getName() + "]";
                System.out.println(result);
                try {
                    Thread.sleep(new Random().nextInt(200));
                } catch (InterruptedException e) {
                    ;
                }
                return result;
            }

        };
        ((AbstractPipe<String, String>) pipe).setName("pipe3");

        /*
         * 将第三条管道加入管道线
         */
        pipeline.addAsThreadPoolBasedPipe(pipe, executorSerivce);

        /*
         * 第四条
         */
        pipe = new AbstractPipe<String, String>() {

            @Override
            public String doProcess(String input) throws PipeException {
                String result = input + "->[pipe4," + Thread.currentThread().getName() + "]";
                ;
                System.out.println(result);
                try {
                    Thread.sleep(new Random().nextInt(200));
                } catch (InterruptedException e) {
                    ;
                }
                return result;
            }

        };
        ((AbstractPipe<String, String>) pipe).setName("pipe4");

        /*
         * 将第四条管道加入管道线
         */
        pipeline.addAsThreadPoolBasedPipe(pipe, executorSerivce);

        /*
         * 创建第五条
         */
        pipe = new AbstractPipe<String, String>() {

            @Override
            public String doProcess(String input) throws PipeException {
                String result = input + "->[pipe5," + Thread.currentThread().getName() + "]";

                if (result.startsWith("Task-8")) {
                    throw new PipeException("Task-8 error", this, input);
                }
                ;
                System.out.println(result);
                try {
                    Thread.sleep(new Random().nextInt(200));
                } catch (InterruptedException e) {
                    ;
                }
                return result;
            }

            @Override
            public void shutdown(long timeout, TimeUnit unit) {
                // 在最后一个Pipe中关闭线程池
                //System.out.println("最后一个管道关闭时候队列的大小" + executorSerivce.getQueue().size());
                executorSerivce.shutdown();
                try {
                    executorSerivce.awaitTermination(timeout, unit);
                } catch (InterruptedException e) {
                    ;
                }
            }
        };
        ((AbstractPipe<String, String>) pipe).setName("pipe5");

        /*
         * 将第五条管道加入管道线
         */
        pipeline.addAsThreadPoolBasedPipe(pipe, executorSerivce);

        /*
         * 管道线初始化
         */
        pipeline.init(pipeline.newDefaultPipelineContext());

        int N = 10;
        try {
            for (int i = 0; i < N; i++) {
                pipeline.process("Task-" + i);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        pipeline.shutdown(1, TimeUnit.SECONDS);

    }

}
