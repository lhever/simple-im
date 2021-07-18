package com.lhever.common.core.test;

import com.lhever.common.core.support.page.PageUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/12/12 9:51
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/12/12 9:51
 * @modify by reason:{方法名}:{原因}
 */
public class PageUtilsTest {

    @Test
    public void test() {
        print(0, 1, 5);
    }

    public void print(int total, int pageNo, int pageSize) {
        int[] offsetLimt = PageUtils.getOffsetLimt(total, pageNo, pageSize);

        System.out.println("offset, limit: " + Arrays.toString(offsetLimt));

        int[] fromTo = PageUtils.getFromTo(total, pageNo, pageSize);
        System.out.println("from,   to: " +Arrays.toString(fromTo));
    }

















}
