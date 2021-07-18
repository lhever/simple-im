package com.lhever.common.core.converter.bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.Unsafe;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/12 10:15
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/12 10:15
 * @modify by reason:{方法名}:{原因}
 */
public final class UnsafeAccess {

    private static final Log LOG = LogFactory.getLog(UnsafeAccess.class);

    public static final Unsafe theUnsafe;

    /**
     * The offset to the first element in a byte array.
     */
    public static final int BYTE_ARRAY_BASE_OFFSET;
    private static boolean unaligned = false;

    static {
        theUnsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    Field f = Unsafe.class.getDeclaredField("theUnsafe");
                    f.setAccessible(true);
                    return f.get(null);
                } catch (Throwable e) {
                    LOG.warn("sun.misc.Unsafe is not accessible", e);
                }
                return null;
            }
        });

        if (theUnsafe != null) {
            BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
            try {
                // Using java.nio.Bits#unaligned() to check for unaligned-com.hikvision.community.health.chep.modules.access capability
                Class<?> clazz = Class.forName("java.nio.Bits");
                Method m = clazz.getDeclaredMethod("unaligned");
                m.setAccessible(true);
                unaligned = (boolean) m.invoke(null);
            } catch (Exception e) {
                unaligned = false; // FindBugs: Causes REC_CATCH_EXCEPTION. Suppressed.
            }
        } else {
            BYTE_ARRAY_BASE_OFFSET = -1;
        }
    }

    private UnsafeAccess() {
    }

    public static boolean isAvailable() {
        return theUnsafe != null;
    }

    /**
     * @return true when running JVM is having sun's Unsafe package available in it and underlying
     * system having unaligned-com.hikvision.community.health.chep.modules.access capability.
     */
    public static boolean unaligned() {
        return unaligned;
    }

    public static final boolean littleEndian = ByteOrder.nativeOrder()
            .equals(ByteOrder.LITTLE_ENDIAN);
}

