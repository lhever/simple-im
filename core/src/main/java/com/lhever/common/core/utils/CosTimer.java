package com.lhever.common.core.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <p>处理状态变更的定时器</p>
 *
 * @author wangzhankai 2019年11月15日 14:29
 * @version V1.0
 */
public class CosTimer extends Thread {

    private static final Logger log = LoggerFactory.getLogger(CosTimer.class);

    /*自身实例*/
    private static volatile CosTimer instance;
    /*执行间隔 ms*/
    private static long interval = 10000L;
    /*最先执行的任务*/
    private volatile TimerTask firstTask;
    /*任务列表*/
    private PriorityQueue<TimerTask> tasks = new PriorityQueue<>(new TimerComparator());

    private CosTimer() {
    }

    /**
     * 获取实例（单例模式）
     *
     * @return CosTimer
     * @author wangzhankai 2019/11/15 14:35
     */
    public static CosTimer getInstance() {
        if (instance == null) {
            synchronized (CosTimer.class) {
                if (instance == null) {
                    instance = new CosTimer();
                    instance.setDaemon(true);
                    instance.setName("custom-timed-task-executor");
                    instance.start();//自启
                }
            }
        }
        return instance;
    }

    /**
     * 判断任务时间，并执行任务
     *
     * @return void
     * @author wangzhankai 2019/11/15 15:07
     */
    private void execTask() {
        while (firstTask != null && firstTask.getTaskTime() <= now()) {//到任务时间，则执行任务
            synchronized (this) {//执行任务时，获取锁
                //先判断是否配置了doTask
                if (firstTask.getTask() == null) {
                    //设置自执行
                    firstTask.setExecOnSet(true); //firstTask到了运行时间了，但是关联任务为空，
                } else {
                    //执行任务
                    firstTask.doTask();
                }
                //顺序判断下一个任务是否可以执行
                firstTask = tasks.poll();
            }
        }
    }

    /**
     * 根据id判断任务是否已存在, 必须加锁，否则会出现判断失误
     * @param taskId
     * @return boolean
     * @author wangzhankai 2019/11/22 15:19
     */
    public synchronized boolean exist(String taskId) {
        if (taskId == null || firstTask == null) {
            return false;
        }
        try {
            //首先判断firstTask是不是查询任务
            if (firstTask.getTaskId().equals(taskId)) {
                return true;
            }
            //从列表中查询
            TimerTask[] tasksCopy =  tasks.toArray(new TimerTask[tasks.size()]);
            for (TimerTask timerTask : tasksCopy) {
                if (taskId.equals(timerTask.getTaskId())) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("CosTimer judge exist异常:{}", e.getMessage(), e);
        }
        return false;
    }

    /**
     * 添加任务（支持多个任务相同id）
     *
     * @param taskName
     * @param taskTime
     * @return TimerTask
     * @author wangzhankai 2019/11/15 15:03
     */
    public synchronized TimerTask add(String taskId, String taskName, long taskTime) {
        TimerTask timerTask = new TimerTask(taskId, taskName, taskTime);
        return add(timerTask);
    }

    public synchronized TimerTask add(TimerTask timerTask) {
        if (firstTask != null) {
            //先与当前firstTask比较,如果新任务比firstTask后执行，则直接添加到任务列表中
            if (firstTask.getTaskTime() < timerTask.getTaskTime()) {
                doAddTask(timerTask);
            } else {//否则将新任务设置成firstTask
                doAddTask(firstTask);
                firstTask = timerTask;
            }
        } else {
            firstTask = timerTask;
        }
        return timerTask;
    }

    /**
     * 添加多个任务
     *
     * @param taskList
     * @return List<TimerTask>
     * @author wangzhankai 2019/12/3 15:39
     */
    public synchronized <T> List<TimerTask> addAll(List<TimerTask> taskList) {
        //入参校验
        if (CollectionUtils.isEmpty(taskList)) {
            return taskList;
        }
        tasks.addAll(taskList);
        //设置firstTask
        if (firstTask == null) {
            firstTask = tasks.poll();
        } else if (tasks.size() > 0 && tasks.peek().getTaskTime() < firstTask.getTaskTime()) {
            doAddTask(firstTask);
            firstTask = tasks.poll();
        }
        return taskList;
    }

    /**
     * 删除指定任务
     *
     * @param taskId
     * @return CosTimer
     * @author wangzhankai 2019/11/15 16:14
     */
    public synchronized CosTimer delete(String taskId) {
        if (taskId == null) {
            return instance;
        }
        if (firstTask != null && taskId.equals(firstTask.getTaskId())) {//首先判断是不是firstTask
            firstTask = tasks.poll();
        } else if (CollectionUtils.isNotEmpty(tasks)) {//删除任务列表中的任务
            List<TimerTask> discards = new ArrayList<>(1);
            for (TimerTask timerTask : tasks) {
                if (taskId.equals(timerTask.getTaskId())) {
                    discards.add(timerTask);
                }
            }
            for (TimerTask discard : discards) {
                tasks.remove(discard);
            }
        }

        return instance;
    }

    /**
     * 更新指定任务
     *
     * @param taskId
     * @param taskName
     * @param taskTime
     * @return TimerTask
     * @author wangzhankai 2019/11/15 16:17
     */
    public synchronized TimerTask update(String taskId, String taskName, long taskTime) {
        return instance.delete(taskId).add(taskId, taskName, taskTime);
    }

    /**
     * 为任务列表添加任务
     *
     * @param timerTask
     * @return void
     * @author wangzhankai 2019/11/15 15:53
     */
    private synchronized void doAddTask(TimerTask timerTask) {
        tasks.add(timerTask);
    }

    @Override
    public void run() {
        //无限循环
        for (; ; ) {
            //执行任务
            if (firstTask != null) {
                execTask();
            }
            try {
                //间隔执行
                sleep(interval);
            } catch (InterruptedException e) {
                //中断不打印日志
            }
        }
    }

    /**
     * 获取当前时间
     *
     * @return long
     * @author wangzhankai 2019/11/15 14:38
     */
    public static long now() {
        return System.currentTimeMillis();
    }


    /*任务实体类*/
    @Getter
    @Setter
    public static class TimerTask {
        /*任务id*/
        private String taskId;
        /*任务名称*/
        private String taskName;
        /*任务时间*/
        private long taskTime;
        /*执行任务*/
        private volatile Runnable task;

        /*是否在task字段被赋值后直接运行程序*/
        private volatile boolean execOnSet = false;

        public TimerTask(String taskId, String taskName, long taskTime) {
            this.taskId = taskId;
            this.taskName = taskName;
            this.taskTime = taskTime;
        }

        public void setTask(Runnable task) {
            this.task = task;
            if (execOnSet) {//设置后直接执行
                doTask();
            }
        }

        public void doTask() {
            if (task != null) {
                try {
                    task.run();
                } catch (Throwable e) {
                    String tip = "CosTimer 任务执行失败, taskId:{}, taskName:{}, taskTime:{}, 失败原因:{}";
                    log.error(tip, taskId, taskName, taskTime, e.getMessage(), e);
                }
            }
        }
    }

    public static class TimerComparator implements Comparator<TimerTask> {
        @Override
        public int compare(TimerTask o1, TimerTask o2) {
            if (o1.getTaskTime() > o2.getTaskTime()) {
                return 1;
            } else if (o1.getTaskTime() == o2.getTaskTime()) {
                return 0;
            }
            return -1;
        }
    }
}
