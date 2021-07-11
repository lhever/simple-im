package com.lhever.common.core.support.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lihong 2016年4月22日 下午8:14:41
 * @version v2.0
 */
public class TestTask2 implements Runnable {

    private static Logger log = LoggerFactory.getLogger(TestTask2.class);
    String taskName;

    /**
     * Instantiates a new test task.
     */
    public TestTask2() {
    }

    /**
     * @param taskName
     * @author lihong 2016年4月22日 下午8:16:12
     * @since v2.0
     */
    public TestTask2(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Run.
     *
     * @see Runnable#run()
     * @since v2.0
     */
    public void run() {
        try {
            log.info(this.taskName + " : is started.");
            Thread.sleep(10000);
            throw new IllegalStateException("测试线程池任务抛出异常");
        } catch (InterruptedException e) {
            log.error(this.taskName + " : is not completed!");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return (getTaskName());
    }

    /**
     * @return String
     * @author lihong 2016年4月22日 下午8:15:58
     * @since v2.0
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName void
     * @author lihong 2016年4月22日 下午8:16:03
     * @since v2.0
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
