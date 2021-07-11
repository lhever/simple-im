package com.lhever.common.core.support.serializer;

import org.springframework.lang.Nullable;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:08
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:08
 * @modify by reason:{方法名}:{原因}
 */
@FunctionalInterface
public interface Converter<S, T> {

    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Nullable
    T convert(S source);

}
