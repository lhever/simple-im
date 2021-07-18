package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {
    private final static Logger LOG = LoggerFactory.getLogger(ThreadUtils.class);

    public static void joinThread(Thread thread) {
        if (thread == null) {
            return;
        }

        try {
            thread.join();
        } catch (InterruptedException e) {
            LOG.error("join thread fail", e);
        } catch (Exception e) {
            LOG.error("join thread fail", e);
        }
    }

    public static String getName() {
        return Thread.currentThread().getName();
    }

    public static void joinThread(Thread thread, long millis) {
        if (thread == null) {
            return;
        }

        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            LOG.error("join thread fail", e);
        } catch (Exception e) {
            LOG.error("join thread fail", e);
        }
    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOG.error("thread sleep fail", e);
        }
    }


}
