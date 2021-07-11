package com.lhever.common.core.test;


import com.lhever.common.core.support.timer.HashedWheelTimer;
import com.lhever.common.core.support.timer.Timeout;
import com.lhever.common.core.support.timer.Timer;
import com.lhever.common.core.support.timer.TimerTask;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lihong10 on 2017/6/19.
 */
public class HashedWheelTimerTest {


    public static void main(String[] argv) {

        final Timer timer = new HashedWheelTimer(Executors.defaultThreadFactory(), 5, TimeUnit.SECONDS, 2);

        TimerTask task1 = new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                System.out.println("task 1 will run per 5 seconds ");
                System.out.println(this == timeout.task());
                timer.newTimeout(this, 5, TimeUnit.SECONDS);
            }
        };
        timer.newTimeout(task1, 5, TimeUnit.SECONDS);


        TimerTask task2 = new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                System.out.println("task 2 will run per 10 seconds");
                timer.newTimeout(this, 10, TimeUnit.SECONDS);
            }
        };
        timer.newTimeout(task2, 10, TimeUnit.SECONDS);


        timer.newTimeout(new TimerTask() {
            public void run(Timeout timeout) throws Exception {
                System.out.println("task 3 run only once ! ");
            }
        }, 15, TimeUnit.SECONDS);

    }




    @Test
    public void test() {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);

        int maxp1 = Integer.MAX_VALUE + 1;
        System.out.println(maxp1);

        int maxp2 = Integer.MAX_VALUE + 2;
        System.out.println(maxp2);
    }


}
