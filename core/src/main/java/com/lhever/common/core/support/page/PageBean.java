package com.lhever.common.core.support.page;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageBean<T> implements Serializable {
    //常量------------------------------------------------------------------
    private static final long serialVersionUID = 1L;
    public final static long DEFAULT_PAGE_SIZE = 24;

    private long total;//总记录数
    private long totalPage = 0;// 总页数
    private long page = 0;// 当前页
    private long pageSize = DEFAULT_PAGE_SIZE;// 每页记录数
    private List<T> rows;//已分页好的结果集

    private boolean isFirstPage;// 是否为第一页
    private boolean isLastPage;// 是否为最后一页
    private boolean hasPreviousPage;// 是否有前一页
    private boolean hasNextPage;// 是否有下一页

    /**
     * 构造方法
     * @param pageInfo
     */
    @Deprecated
    public PageBean(PageInfo<T> pageInfo){
        this.page = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.total = Long.valueOf(pageInfo.getTotal()).intValue();
        this.totalPage = pageInfo.getPages();
        this.rows = pageInfo.getList();
        init();
    }


    //构造方法--------------------------------------------------------------
    public PageBean() {
        this(0, 0, DEFAULT_PAGE_SIZE, new ArrayList<T>());
    }

    public PageBean(long total, long page, long pageSize, List<T> rows) {
        this.total = total;
        this.pageSize = pageSize;
        this.page = page;
        this.totalPage = total % pageSize == 0L ? (total / pageSize) : (total / pageSize + 1);

        if (this.page > this.totalPage) {
            this.page = this.totalPage;
        }
        this.rows = rows;
        init();
    }

    //公共方法--------------------------------------------------------------

    /**
     * 初始化分页信息
     */
    public void init() {
        this.isFirstPage = isFirstPage();
        this.isLastPage = isLastPage();
        this.hasPreviousPage = isHasPreviousPage();
        this.hasNextPage = isHasNextPage();
    }

    /**
     * 计算总页数,静态方法,供外部直接通过类名调用
     *
     * @param pageSize 每页记录数
     * @param allRow   总记录数
     * @return 总页数
     */
    public static long countTotalPage(final long pageSize, final long allRow) {
        long totalPage = (allRow % pageSize == 0) ? (allRow / pageSize) : (allRow / pageSize + 1);
        return totalPage;
    }

    /**
     * 计算当前页开始记录
     *
     * @param pageSize    每页记录数
     * @param currentPage 当前第几页
     * @return 当前页开始记录号
     */
    public static long countOffset(final long pageSize, final long currentPage) {
        long page = currentPage;
        if (currentPage == 0) {
            page = 1;
        }
        final long offset = pageSize * (page - 1);
        return offset;
    }

    /**
     * 计算当前页开始记录,考虑最后一页情况
     *
     * @param pageSize    每页记录数
     * @param currentPage 当前第几页
     * @return 当前页开始记录号
     */
    public static long countOffset(final long pageSize, final long currentPage, final long totalCount) {
        long startIndex = countOffset(pageSize, currentPage);
        if (startIndex >= totalCount) {
            long pageNo = (totalCount + pageSize - 1) / pageSize;
            startIndex = (pageNo - 1) * pageSize;
        }
        return startIndex;
    }

    /**
     * 计算当前页,若为0或者请求的URL中没有"?page=",则用1代替
     *
     * @param page 传入的参数(可能为空,即0,则返回1)
     * @return 当前页
     */
    public static long countCurrentPage(long page) {
        final long curPage = (page == 0 ? 1 : page);
        return curPage;
    }

    //受保护方法------------------------------------------------------------

    //私有方法--------------------------------------------------------------

    //重载Object方法--------------------------------------------------------

    //get/set方法-----------------------------------------------------------
    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public long getPage() {
        if (this.totalPage < this.page) {
            this.page = this.totalPage;
        }
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }


    /**
     * 以下判断页的信息,只需getter方法(is方法)即可
     *
     * @return
     */

    public boolean isFirstPage() {
        return page == 1; // 如是当前页是第1页
    }

    public boolean isLastPage() {
        return page == totalPage; // 如果当前页是最后一页
    }

    public boolean isHasPreviousPage() {
        return page != 1; // 只要当前页不是第1页
    }

    public boolean isHasNextPage() {
        return page != totalPage; // 只要当前页不是最后1页
    }


    @Override
    public String toString() {
        return "PageBean [pageSize=" + pageSize + ", rows=" + rows + ", total=" + total + ", totalPage=" + totalPage
                + ", page=" + page + ", isFirstPage=" + isFirstPage + ", isLastPage=" + isLastPage
                + ", hasPreviousPage=" + hasPreviousPage + ", hasNextPage=" + hasNextPage + "]";
    }
}
