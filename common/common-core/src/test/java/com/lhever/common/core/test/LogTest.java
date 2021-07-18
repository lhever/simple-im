package com.lhever.common.core.test;

import com.lhever.common.core.utils.LogUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/12 11:37
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/12 11:37
 * @modify by reason:{方法名}:{原因}
 */
public class LogTest {

    private static final Logger internalLog = LoggerFactory.getLogger(LogTest.class);


    @Test
    public void testLog() {
        LogUtils.info("ssssssssssssssssssssssss");
        LogUtils.debug("sssssssssssss");
    }

    public static void main(String[] args) {
        String msg = "是否计算看风景速度快是过节费大V框架";

        LogUtils.info(msg);
        LogUtils.info(msg);
        internalLog.info(msg);
        internalLog.info(msg);

        int total = 1000000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            LogUtils.info(msg);
        }

        long case1 = System.currentTimeMillis() - start;


        start = System.currentTimeMillis();
        for (int i = 0; i < total; i++) {
            internalLog.info(msg);
        }
        long case2 = System.currentTimeMillis() - start;


        System.out.println("case1: " + case1);
        System.out.println("case2: " + case2);


        long l = Long.reverseBytes(10);


    }


}
