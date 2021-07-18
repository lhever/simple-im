package com.lhever.common.core.support.concurrent.mtpattern.tpt;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {
    private static Log log = LogFactory.getLog(AbstractTerminatableThread.class);

    // 用以标记线程是否应该终止的标记对象
    public final TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }

    /**
     * @param terminationToken 线程间共享的线程终止标志实例
     */
    public AbstractTerminatableThread(TerminationToken terminationToken) {
        super();
        this.setDaemon(true);
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

    /**
     * 留给子类实现其线程处理逻辑。
     *
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    /**
     * 留给子类实现。用于实现线程停止后的一些清理动作。
     *
     * @param cause
     */
    protected void doCleanup(Exception cause) {
        // 什么也不做
    }

    /**
     * 留给子类实现。用于执行线程停止所需的操作。
     */
    protected void doTerminiate() {
        // 什么也不做
    }

    @Override
    public void run() {
        Exception ex = null;
        try {
            for (; ; ) {

                // 在执行线程的处理逻辑前先判断线程停止的标志。
                if (terminationToken.isToShutdown()
                        && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }

        } catch (Exception e) {
            // 使得线程能够响应interrupt调用而退出
            ex = e;
            log.info("线程[" + this.getName() + "]发生异常", e);
        } finally {
            try {
                doCleanup(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    /*
     * 请求停止线程。
     */
    @Override
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminiate();
        } finally {

            // 若无待处理的任务，则试图强制终止线程
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUtilThreadTerminated) {
        terminate();
        if (waitUtilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("线程" + this.getName() + "尚未处理完剩余任务便被终止", e);
            } catch (Exception ex) {
                log.info("停止线程" + this.getName() + "时发生异常", ex);
            }
        }
    }

}
