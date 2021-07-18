package com.lhever.common.core.support.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监视线程实现
 *
 * @author lihong 2016年4月22日 下午2:52:55
 * @version v2.0
 */
public class SimpleThreadPoolMonitor implements ThreadPoolMonitor {

    private static Logger log = LoggerFactory.getLogger(SimpleThreadPoolMonitor.class);
    ThreadPoolExecutor executor;
    private long monitoringPeriod;

    public void run() {
        try {
            while (true) {
                log.info("监视线程开始监听......");
                monitorThreadPool();
                log.info("监视线程进入休眠状态......");
                Thread.sleep(monitoringPeriod * 1000);
            }
        } catch (Exception e) {
            log.error("线程是监视线程运行故障， 请通知相关人员进行必要的检查和修复， 具体错误信息是： ->" + e.getMessage());
        }
    }

    public void monitorThreadPool() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("线程池当前状态是：");
        strBuff.append(" - CurrentPoolSize : ").append(executor.getPoolSize());
        strBuff.append(" - CorePoolSize : ").append(executor.getCorePoolSize());
        strBuff.append(" - MaximumPoolSize : ").append(executor.getMaximumPoolSize());
        strBuff.append(" - ActiveTaskCount : ").append(executor.getActiveCount());
        strBuff.append(" - CompletedTaskCount : ").append(executor.getCompletedTaskCount());
        strBuff.append(" - TotalTaskCount : ").append(executor.getTaskCount());
        strBuff.append(" - isTerminated : ").append(executor.isTerminated());

        log.info(strBuff.toString());
    }


    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    /**
     * @return long
     * @author lihong 2016年4月22日 下午2:56:00
     * @since v2.0
     */
    public long getMonitoringPeriod() {
        return monitoringPeriod;
    }

    /**
     * @param monitoringPeriod void
     * @author lihong 2016年4月22日 下午2:56:08
     * @since v2.0
     */
    public void setMonitoringPeriod(long monitoringPeriod) {
        this.monitoringPeriod = monitoringPeriod;
    }
}
