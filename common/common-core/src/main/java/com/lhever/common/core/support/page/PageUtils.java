package com.lhever.common.core.support.page;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/11/27 10:52
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/11/27 10:52
 * @modify by reason:{方法名}:{原因}
 */
public class PageUtils {


    public static int[] getFromTo(int total, int pageNo, int pageSize) {
        total = (total < 0) ? 0 : total;
        if (total == 0) {
            return new int[]{0, 0};
        }
        int totalPage = (total + pageSize - 1) / pageSize;

        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > totalPage) {
            pageNo = totalPage;
        }
        int fromIndex;
        int toIndex;

        if (pageNo * pageSize < total) {// 判断是否为最后一页
            toIndex = pageNo * pageSize;
            fromIndex = toIndex - pageSize;
        } else {
            toIndex = total;
            fromIndex = pageSize * (totalPage - 1);
        }
        return new int[]{fromIndex, toIndex};
    }


    public static int[] getOffsetLimt(int total, int pageNo, int pageSize) {
        total = (total < 0) ? 0 : total;
        if (total == 0) {
            return new int[]{0, pageSize};
        }
        int totalPage = (total + pageSize - 1) / pageSize;

        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > totalPage) {
            pageNo = totalPage;
        }
        int fromIndex;
        int toIndex;

        if (pageNo * pageSize < total) {// 判断是否为最后一页
            toIndex = pageNo * pageSize;
            fromIndex = toIndex - pageSize;
        } else {
            toIndex = total;
            fromIndex = pageSize * (totalPage - 1);
        }
        return new int[]{fromIndex, pageSize};
    }



    public static long[] getFromTo(long total, long pageNo, long pageSize) {
        total = (total < 0) ? 0 : total;
        if (total == 0) {
            return new long[]{0, 0};
        }
        long totalPage = (total + pageSize - 1) / pageSize;

        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > totalPage) {
            pageNo = totalPage;
        }
        long fromIndex;
        long toIndex;

        if (pageNo * pageSize < total) {// 判断是否为最后一页
            toIndex = pageNo * pageSize;
            fromIndex = toIndex - pageSize;
        } else {
            toIndex = total;
            fromIndex = pageSize * (totalPage - 1);
        }
        return new long[]{fromIndex, toIndex};
    }


    public static long[] getOffsetLimt(long total, long pageNo, long pageSize) {
        total = (total < 0) ? 0 : total;
        if (total == 0) {
            return new long[]{0, pageSize};
        }
        long totalPage = (total + pageSize - 1) / pageSize;

        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > totalPage) {
            pageNo = totalPage;
        }
        long fromIndex;
        long toIndex;

        if (pageNo * pageSize < total) {// 判断是否为最后一页
            toIndex = pageNo * pageSize;
            fromIndex = toIndex - pageSize;
        } else {
            toIndex = total;
            fromIndex = pageSize * (totalPage - 1);
        }
        return new long[]{fromIndex, pageSize};
    }




}
