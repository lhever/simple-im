package com.lhever.common.core.support.serializer;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:04
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:04
 * @modify by reason:{方法名}:{原因}
 */
@FunctionalInterface
public interface Deserializer<T> {

    /**
     * Read (assemble) an object of type T from the given InputStream.
     * <p>Note: Implementations should not close the given InputStream
     * (or any decorators of that InputStream) but rather leave this up
     * to the caller.
     * @param inputStream the input stream
     * @return the deserialized object
     * @throws IOException in case of errors reading from the stream
     */
    T deserialize(InputStream inputStream) throws IOException;

}
