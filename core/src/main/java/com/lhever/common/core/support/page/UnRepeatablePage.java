package com.lhever.common.core.support.page;

/**
 * Created by lihong10 on 2017/11/28.
 */

import java.util.Arrays;
import java.util.List;

/**
 * 用法参见{@link RepeatablePage} 与  {@link Page}
 *
 * @param <T>
 * @author lihong10 2017年12月4日 上午10:42:30
 */
public class UnRepeatablePage<T> extends Page<T> {

    public UnRepeatablePage(List<T> data, int pageSize) {
        this(data, 1, pageSize);
    }


    public UnRepeatablePage(List<T> data, int pageNo, int pageSize) {
        super(data, pageNo, pageSize);
    }

    @Override
    public boolean hasNext() {
        return cursor < this.totalPage;
    }

    @Override
    public List<T> next() {
        cursor++;
        if (data == null) {
            return null;
        }

        List<T> subList = null;
        if (data.size() > pageSize) {
            subList = data.subList(0, pageSize);
            data = data.subList(pageSize, data.size());
        } else {
            subList = data.subList(0, data.size());
            data = null;
        }

        return subList;
    }

    public void check() {
        if (cursor > 0) {
            throw new IllegalStateException("the operation is not permitted if for cycle or next() method" +
                    " has benn called before ");
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("reset is not permitted !!!，" +
                "if you have to, use RepeatablePage.class");
    }

    @Override
    public void reset(int cursor) {
        throw new UnsupportedOperationException("reset is not permitted !!!，" +
                "if you have to, use RepeatablePage.class ");
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6});
        int pageSize = 6;//遍历分页时候，指定分页大小是3
        UnRepeatablePage<Integer> page = new UnRepeatablePage<Integer>(list, 3);

        System.out.println("------------遍历前允许直接获取某一页------------------------");
        System.out.println(page.getPage(2));


        System.out.println("第1次遍历结果是");
        for (List<Integer> li : page) { //for遍历
            System.out.println(li);
        }
        System.out.println("------------遍历过之后，不允许直接获取某一页------------------------");
        System.out.println(page.getPage(2));

        page.reset();//调用reset会抛出异常，应为该子类不允许这个操作
        for (List<Integer> li : page) { //for遍历
            System.out.println(li);
        }
    }

}




