package com.lhever.common.core.support.page;

/**
 * Created by lihong10 on 2017/11/28.
 */

import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * 该类用于对链表中的数据进行分页操作，采用迭代器设计模式实现。该类的其中一个子类 {@link UnRepeatablePage} 只允许分页一次。
 * 另一个子类  {@link RepeatablePage}则允许分页多次。
 *
 * <p>该类的子类适用于停车场项目是要到的另外一种分页方式，也即内存分页。正如本文术语所解释的，
 * 内存分页的数据总量往往是已经全部加载到内存中了的。因此可以想到，内存分页场景中的需要进行分页的数据总量并不大。 那么问题就来了？
 * 我们为什么还要进行分页，是不是画蛇添足多此一举了？笔者在写作之前，就已经总结出两种不得不使用内存分页机制的场景：
 * </p>
 * <ul>
 * <li>
 * 场景(1):  数据总量虽然不大，不足以对内存或性能造成威胁，但在使用数据进行计算的过程中产生的中间数据或最终结果总量巨大，
 * 计算产生的大量临时数据或最终结果才是可能导致性能问题的关键原因。这种情况下我们也只能将总数据划分成多个更小的批次进行处理，
 * 每次只处理一个批次，这样可以使每一个小批次产生的临时数据或计算结果不足以威胁到整个计算过程。当一个批次处理结束，
 * 临时数据被销毁之后，再进行下一批次的处理。
 * </li>
 * <li>
 * 场景（2）：一次性处理数据的耗时大于处理每一个小的批次数据的耗时：比如需要将这批数据作为参数调用某个接口，
 * 数据量大，接口提供方处理速度迟缓导致的超时无法获取数据。这种情况下，分批分多次调用接口成功的概率大于一个批次单次调用接口的概率。
 * </li>
 * <li>
 * 场景（3）：第三方库、框架本身的限制。比如sql语句的in 字句不能过长，否则会导致SQL异常。这种情况下，可能需要拆分为多次进行查询。
 * </li>
 * </ul>
 *
 * @author lihong10
 * @since 2.9.1
 */
public abstract class Page<T> implements Iterator<List<T>>, Iterable<List<T>> {
    /**
     * 原集合
     */
    protected List<T> data;

    /**
     * 当前页
     */
    protected int pageNo;

    /**
     * 该字段仅用作迭代每一分页的游标
     */
    protected int cursor;

    /**
     * 总页数
     */
    protected int totalPage;

    /**
     * 每页条数
     */
    protected int pageSize;

    /**
     * 总数据条数
     */
    protected int totalCount;

    /**
     * 双参数构造
     *
     * @param data
     * @param pageSize 分页大小
     * @author lihong10 2017年12月2日 上午10:42:30
     */
    public Page(List<T> data, int pageSize) {
        this(data, 1, pageSize);
    }

    /**
     * 三参数构造
     *
     * @param data     被分页的链表
     * @param pageNo   页码
     * @param pageSize 分页大小
     */
    public Page(List<T> data, int pageNo, int pageSize) {
        if (data == null) {
            throw new IllegalArgumentException("data can not be null!");
        }

        if (pageSize <= 0) {
            throw new IllegalArgumentException("pageSize must >= 1");
        }

        this.data = data;
        this.totalCount = data.size();
        this.pageSize = pageSize;
        this.totalPage = (totalCount + pageSize - 1) / pageSize;
        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > this.totalPage) {
            pageNo = totalPage;
        }
        this.pageNo = pageNo;
        this.cursor = 0;
    }

    /**
     * 返回迭代器对象
     *
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     */
    @Override
    public final Iterator<List<T>> iterator() {
        return this;
    }

    @Override
    public void remove() {
    }

    /**
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     * 得到pageNo表示的那一页数据
     */
    public final List<T> getPagedList() {
        check();
        int fromIndex = (pageNo - 1) * pageSize;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();//空数组
        }
        if (fromIndex < 0) {
            return Collections.emptyList();//空数组
        }
        int toIndex = pageNo * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }

    /**
     * @param pageNo
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     * 根据页数获取指定分页
     * @author lihong10 2017年11月30日 下午15:42:30
     */
    public final List<T> getPage(int pageNo) {
        check();
        if (pageNo <= 0) {
            pageNo = 1;
        } else if (pageNo > totalPage) {
            pageNo = totalPage;
        }

        int fromIndex;
        int toIndex;
        if (pageNo * pageSize < totalCount) {// 判断是否为最后一页
            toIndex = pageNo * pageSize;
            fromIndex = toIndex - pageSize;
        } else {
            toIndex = totalCount;
            fromIndex = pageSize * (totalPage - 1);
        }

        List<T> objects = null;
        if (data != null && data.size() > 0) {
            objects = data.subList(fromIndex, toIndex);
        }

        return objects;
    }

    /**
     * 重置游标，默认重置为0
     *
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     */
    public abstract void reset();

    /**
     * 重置游标操作
     *
     * @param cursor
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     */
    public abstract void reset(int cursor);

    /**
     * 判断某个方法是否允许被调用
     *
     * @return
     * @author lihong10 2017年12月3日 上午10:42:30
     */
    public abstract void check();

    public int getPageSize() {
        return pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
