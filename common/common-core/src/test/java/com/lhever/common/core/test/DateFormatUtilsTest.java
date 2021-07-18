package com.lhever.common.core.test;

import com.lhever.common.core.utils.DateFormatUtils;
import org.junit.Test;

import java.util.Date;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/11/27 13:43
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/11/27 13:43
 * @modify by reason:{方法名}:{原因}
 */
public class DateFormatUtilsTest {




    @Test
    public void test() {
        System.out.println(DateFormatUtils.toISO8601DateString(new Date()));
    }
}
