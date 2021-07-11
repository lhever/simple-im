package com.lhever.common.core.support.serializer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.core.serializer.support.SerializationFailedException;
import org.springframework.util.Assert;

import java.io.ByteArrayOutputStream;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/11 17:10
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/11 17:10
 * @modify by reason:{方法名}:{原因}
 */
public class SerializingConverter implements Converter<Object, byte[]> {

    private final org.springframework.core.serializer.Serializer<Object> serializer;


    /**
     * Create a default {@code SerializingConverter} that uses standard Java serialization.
     */
    public SerializingConverter() {
        this.serializer = new DefaultSerializer();
    }

    /**
     * Create a {@code SerializingConverter} that delegates to the provided {@link org.springframework.core.serializer.Serializer}.
     */
    public SerializingConverter(Serializer<Object> serializer) {
        Assert.notNull(serializer, "Serializer must not be null");
        this.serializer = serializer;
    }


    /**
     * Serializes the source object and returns the byte array result.
     */
    @Override
    public byte[] convert(Object source) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
        try  {
            this.serializer.serialize(source, byteStream);
            return byteStream.toByteArray();
        }
        catch (Throwable ex) {
            throw new SerializationFailedException("Failed to serialize object using " +
                    this.serializer.getClass().getSimpleName(), ex);
        }
    }

}

