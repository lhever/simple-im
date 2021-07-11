package com.lhever.common.core.support.page;

/**
 * Created by lihong10 on 2017/11/28.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用法参见{@link UnRepeatablePage} 与  {@link Page}
 *
 * @param <T>
 * @author lihong10 2017年12月4日 上午10:42:30
 */
public class RepeatablePage<T> extends Page<T> {

    public RepeatablePage(List<T> data, int pageSize) {
        this(data, 1, pageSize);
    }

    public RepeatablePage(List<T> data, int currentPage, int pageSize) {
        super(data, currentPage, pageSize);
    }

    @Override
    public boolean hasNext() {
        if (totalPage <= 0) {
            return false;
        }
        boolean hasNext = cursor < totalPage;
        if (hasNext == false) {
            reset(); //重置的目的，是为了可以重复调用for循环
        }
        return hasNext;
    }

    @Override
    public List<T> next() {
        int pageNo = this.cursor + 1;
        List<T> page = getPage(pageNo);
        this.cursor++;
        return page;
    }

    public void check() {
    }

    public void reset() {
        this.cursor = 0;
    }

    public void reset(int cursor) {
        if (cursor < 0) {
            this.cursor = 0;
        } else if (cursor >= totalPage) {
            this.cursor = totalPage;
        } else {
            this.cursor = cursor;
        }
    }

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6});
        int pageSize = 3;//遍历分页时候，指定分页大小是3
        RepeatablePage<Integer> page = new RepeatablePage<Integer>(list, pageSize);

        System.out.println("------------直接获取某一页------------------------");
        System.out.println(page.getPage(1));
        System.out.println(page.getPage(2));
        System.out.println(page.getPage(3));


        System.out.println("第1次遍历结果是");
        for (List<Integer> li : page) { //for遍历
            System.out.println(li);
        }

        System.out.println("第2次遍历结果是");
        for (List<Integer> li : page) {
            System.out.println(li);
        }

        System.out.println("第3次遍历结果是");
        for (List<Integer> li : page) {
            System.out.println(li);
        }

        List<String> li = new ArrayList<>();
        RepeatablePage<String> pageEmpty = new RepeatablePage(li, pageSize);
        for (List<String> sub : pageEmpty) {
        }
        System.out.println("-----------------");


        List<String> li2 = new ArrayList<>();
        li2.add(null);
        li2.add(null);
        RepeatablePage<String> pageEmpty2 = new RepeatablePage(li2, pageSize);
        for (List<String> sub : pageEmpty2) {
            System.out.println(sub);
        }
        System.out.println("-----------------");

    }

}
