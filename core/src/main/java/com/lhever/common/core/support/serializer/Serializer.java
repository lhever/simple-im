package com.lhever.common.core.support.serializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:02
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:02
 * @modify by reason:{方法名}:{原因}
 */
@FunctionalInterface
public interface Serializer<T> {

    /**
     * Write an object of type T to the given OutputStream.
     * <p>Note: Implementations should not close the given OutputStream
     * (or any decorators of that OutputStream) but rather leave this up
     * to the caller.
     * @param object the object to serialize
     * @param outputStream the output stream
     * @throws IOException in case of errors writing to the stream
     */
    void serialize(T object, OutputStream outputStream) throws IOException;

}