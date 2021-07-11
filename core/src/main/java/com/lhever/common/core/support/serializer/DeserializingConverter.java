package com.lhever.common.core.support.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:10
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:10
 * @modify by reason:{方法名}:{原因}
 */

public class DeserializingConverter implements Converter<byte[], Object> {

    private final org.springframework.core.serializer.Deserializer<Object> deserializer;


    /**
     * Create a {@code DeserializingConverter} with default {@link java.io.ObjectInputStream}
     * configuration, using the "latest user-defined ClassLoader".
     * @see org.springframework.core.serializer.DefaultDeserializer#DefaultDeserializer()
     */
    public DeserializingConverter() {
        this.deserializer = new org.springframework.core.serializer.DefaultDeserializer();
    }

    /**
     * Create a {@code DeserializingConverter} for using an {@link java.io.ObjectInputStream}
     * with the given {@code ClassLoader}.
     * @since 4.2.1
     * @see org.springframework.core.serializer.DefaultDeserializer#DefaultDeserializer(ClassLoader)
     */
    public DeserializingConverter(ClassLoader classLoader) {
        this.deserializer = new DefaultDeserializer(classLoader);
    }

    /**
     * Create a {@code DeserializingConverter} that delegates to the provided {@link org.springframework.core.serializer.Deserializer}.
     */
    public DeserializingConverter(Deserializer<Object> deserializer) {
        Assert.notNull(deserializer, "Deserializer must not be null");
        this.deserializer = deserializer;
    }


    @Override
    public Object convert(byte[] source) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
        try {
            return this.deserializer.deserialize(byteStream);
        }
        catch (Throwable ex) {
            throw new SerializationFailedException("Failed to deserialize payload. " +
                    "Is the byte array a result of corresponding serialization for " +
                    this.deserializer.getClass().getSimpleName() + "?", ex);
        }
    }

}
