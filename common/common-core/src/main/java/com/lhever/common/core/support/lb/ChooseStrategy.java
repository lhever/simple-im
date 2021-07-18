package com.lhever.common.core.support.lb;

import java.util.List;
import java.util.function.Supplier;

public interface ChooseStrategy<T> {

    public T choose(Supplier<List<T>> supplier, Object key);

    public T choose(List<T> supplier, Object key);

}
