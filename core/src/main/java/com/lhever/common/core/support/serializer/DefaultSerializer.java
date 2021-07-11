package com.lhever.common.core.support.serializer;

import org.springframework.core.serializer.Serializer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:03
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:03
 * @modify by reason:{方法名}:{原因}
 */
public class DefaultSerializer implements Serializer<Object> {

    /**
     * Writes the source object to an output stream using Java serialization.
     * The source object must implement {@link Serializable}.
     * @see ObjectOutputStream#writeObject(Object)
     */
    @Override
    public void serialize(Object object, OutputStream outputStream) throws IOException {
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
                    "but received an object of type [" + object.getClass().getName() + "]");
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

}
