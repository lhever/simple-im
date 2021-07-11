package com.lhever.common.core.support.serializer;

import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.NestedIOException;
import org.springframework.core.serializer.Deserializer;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:05
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:05
 * @modify by reason:{方法名}:{原因}
 */
public class DefaultDeserializer implements Deserializer<Object> {

    @Nullable
    private final ClassLoader classLoader;


    /**
     * Create a {@code DefaultDeserializer} with default {@link ObjectInputStream}
     * configuration, using the "latest user-defined ClassLoader".
     */
    public DefaultDeserializer() {
        this.classLoader = null;
    }

    /**
     * Create a {@code DefaultDeserializer} for using an {@link ObjectInputStream}
     * with the given {@code ClassLoader}.
     * @since 4.2.1
     * @see ConfigurableObjectInputStream#ConfigurableObjectInputStream(InputStream, ClassLoader)
     */
    public DefaultDeserializer(@Nullable ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    /**
     * Read from the supplied {@code InputStream} and deserialize the contents
     * into an object.
     * @see ObjectInputStream#readObject()
     */
    @Override
    @SuppressWarnings("resource")
    public Object deserialize(InputStream inputStream) throws IOException {
        ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
        try {
            return objectInputStream.readObject();
        }
        catch (ClassNotFoundException ex) {
            throw new NestedIOException("Failed to deserialize object type", ex);
        }
    }

}

