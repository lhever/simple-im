package com.lhever.common.core.support.tuple;

import com.lhever.common.core.utils.StringUtils;

import java.io.Serializable;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/11/28 11:15
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/11/28 11:15
 * @modify by reason:{方法名}:{原因}
 */
public class CustomNameValuePair<V> implements Cloneable, Serializable {

    public static final int HASH_SEED = 17;
    public static final int HASH_OFFSET = 37;

    private static final long serialVersionUID = -6437800749411518984L;

    private final String name;
    private final V value;

    /**
     * Default Constructor taking a name and a value. The value may be null.
     *
     * @param name The name.
     * @param value The value.
     */
    public CustomNameValuePair(final String name, final V value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        // don't call complex default formatting for a simple toString

        if (this.value == null) {
            return name;
        }
        final int len = this.name.length() + 1 + this.value.toString().length();
        final StringBuilder buffer = new StringBuilder(len);
        buffer.append(this.name);
        buffer.append("=");
        buffer.append(this.value.toString());
        return buffer.toString();
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof CustomNameValuePair) {
            final CustomNameValuePair that = (CustomNameValuePair) object;
            return StringUtils.equals(this.name, that.name)
                    && equals(this.value, that.value);
        }
        return false;
    }

    public static boolean equals(final Object obj1, final Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    public static int hashCode(final int seed, final int hashcode) {
        return seed * HASH_OFFSET + hashcode;
    }

    public static int hashCode(final int seed, final Object obj) {
        return hashCode(seed, obj != null ? obj.hashCode() : 0);
    }


    @Override
    public int hashCode() {
        int hash = HASH_SEED;
        hash = hashCode(hash, this.name);
        hash = hashCode(hash, this.value);
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}