package com.lhever.common.core.converter.bytes;

import sun.misc.Unsafe;

import java.nio.ByteOrder;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/4/12 14:41
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/4/12 14:41
 * @modify by reason:{方法名}:{原因}
 */
public class Bytes {

    /**
     * Size of boolean in bytes
     */
    public static final int SIZEOF_BOOLEAN = Byte.SIZE / Byte.SIZE;

    /**
     * Size of byte in bytes
     */
    public static final int SIZEOF_BYTE = SIZEOF_BOOLEAN;

    /**
     * Size of char in bytes
     */
    public static final int SIZEOF_CHAR = Character.SIZE / Byte.SIZE;

    /**
     * Size of double in bytes
     */
    public static final int SIZEOF_DOUBLE = Double.SIZE / Byte.SIZE;

    /**
     * Size of float in bytes
     */
    public static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;

    /**
     * Size of int in bytes
     */
    public static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;

    /**
     * Size of long in bytes
     */
    public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

    /**
     * Size of short in bytes
     */
    public static final int SIZEOF_SHORT = Short.SIZE / Byte.SIZE;


    /**
     * Convert an int value to a byte array.  Big-endian.  Same as what DataOutputStream.writeInt
     * does.
     *
     * @param val value
     * @return the byte array
     */
    public static byte[] toBytes(int val) {
        byte[] b = new byte[4];
        for (int i = 3; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    /**
     * Convert a long value to a byte array using big-endian.
     *
     * @param val value to convert
     * @return the byte array
     */
    public static byte[] toBytes(long val) {
        byte[] b = new byte[8];
        for (int i = 7; i > 0; i--) {
            b[i] = (byte) val;
            val >>>= 8;
        }
        b[0] = (byte) val;
        return b;
    }

    /**
     * @param f float value
     * @return the float represented as byte []
     */
    public static byte[] toBytes(final float f) {
        // Encode it as int
        return toBytes(Float.floatToRawIntBits(f));
    }

    /**
     * Convert a boolean to a byte array. True becomes -1
     * and false becomes 0.
     *
     * @param b value
     * @return <code>b</code> encoded in a byte array.
     */
    public static byte[] toBytes(final boolean b) {
        return new byte[]{b ? (byte) -1 : (byte) 0};
    }


    /**
     * Convert a short value to a byte array of {@link #SIZEOF_SHORT} bytes long.
     *
     * @param val value
     * @return the byte array
     */
    public static byte[] toBytes(short val) {
        byte[] b = new byte[SIZEOF_SHORT];
        b[1] = (byte) val;
        val >>= 8;
        b[0] = (byte) val;
        return b;
    }

    /**
     * Serialize a double as the IEEE 754 double format output. The resultant
     * array will be 8 bytes long.
     *
     * @param d value
     * @return the double represented as byte []
     */
    public static byte[] toBytes(final double d) {
        // Encode it as a long
        return toBytes(Double.doubleToRawLongBits(d));
    }


    /**
     * Converts a byte array to an int value
     *
     * @param bytes byte array
     * @return the int value
     */
    public static int toInt(byte[] bytes) {
        return toInt(bytes, 0, SIZEOF_INT);
    }

    /**
     * Converts a byte array to an int value
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @param length length of int (has to be {@link #SIZEOF_INT})
     * @return the int value
     * @throws IllegalArgumentException if length is not {@link #SIZEOF_INT} or
     *                                  if there's not enough room in the array at the offset indicated.
     */
    public static int toInt(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_INT || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_INT);
        }
        if (UnsafeComparer.unaligned()) {
            return toIntUnsafe(bytes, offset);
        } else {
            int n = 0;
            for (int i = offset; i < (offset + length); i++) {
                n <<= 8;
                n ^= bytes[i] & 0xFF;
            }
            return n;
        }
    }

    private static IllegalArgumentException explainWrongLengthOrOffset(final byte[] bytes,
                                                                       final int offset,
                                                                       final int length,
                                                                       final int expectedLength) {
        String reason;
        if (length != expectedLength) {
            reason = "Wrong length: " + length + ", expected " + expectedLength;
        } else {
            reason = "offset (" + offset + ") + length (" + length + ") exceed the"
                    + " capacity of the array: " + bytes.length;
        }
        return new IllegalArgumentException(reason);
    }

    /**
     * Converts a byte array to an int value (Unsafe version)
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the int value
     */
    public static int toIntUnsafe(byte[] bytes, int offset) {
        if (UnsafeComparer.littleEndian) {
            return Integer.reverseBytes(UnsafeComparer.theUnsafe.getInt(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET));
        } else {
            return UnsafeComparer.theUnsafe.getInt(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET);
        }
    }

    /**
     * Converts a byte array to a long value. Reverses
     * {@link #toBytes(long)}
     *
     * @param bytes array
     * @return the long value
     */
    public static long toLong(byte[] bytes) {
        return toLong(bytes, 0, SIZEOF_LONG);
    }


    /**
     * Converts a byte array to a long value.
     *
     * @param bytes  array of bytes
     * @param offset offset into array
     * @param length length of data (must be {@link #SIZEOF_LONG})
     * @return the long value
     * @throws IllegalArgumentException if length is not {@link #SIZEOF_LONG} or
     *                                  if there's not enough room in the array at the offset indicated.
     */
    public static long toLong(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_LONG || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_LONG);
        }
        if (UnsafeComparer.unaligned()) {
            return toLongUnsafe(bytes, offset);
        } else {
            long l = 0;
            for (int i = offset; i < offset + length; i++) {
                l <<= 8;
                l ^= bytes[i] & 0xFF;
            }
            return l;
        }
    }

    /**
     * Converts a byte array to an long value (Unsafe version)
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the long value
     */
    public static long toLongUnsafe(byte[] bytes, int offset) {
        if (UnsafeComparer.littleEndian) {
            return Long.reverseBytes(UnsafeComparer.theUnsafe.getLong(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET));
        } else {
            return UnsafeComparer.theUnsafe.getLong(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET);
        }
    }


    /**
     * Presumes float encoded as IEEE 754 floating-point "single format"
     *
     * @param bytes byte array
     * @return Float made from passed byte array.
     */
    public static float toFloat(byte[] bytes) {
        return toFloat(bytes, 0);
    }

    /**
     * Presumes float encoded as IEEE 754 floating-point "single format"
     *
     * @param bytes  array to convert
     * @param offset offset into array
     * @return Float made from passed byte array.
     */
    public static float toFloat(byte[] bytes, int offset) {
        return Float.intBitsToFloat(toInt(bytes, offset, SIZEOF_INT));
    }


    /**
     * Reverses {@link #toBytes(boolean)}
     *
     * @param b array
     * @return True or false.
     */
    public static boolean toBoolean(final byte[] b) {
        if (b.length != 1) {
            throw new IllegalArgumentException("Array has wrong size: " + b.length);
        }
        return b[0] != (byte) 0;
    }


    /**
     * @param bytes byte array
     * @return Return double made from passed bytes.
     */
    public static double toDouble(final byte[] bytes) {
        return toDouble(bytes, 0);
    }

    /**
     * @param bytes  byte array
     * @param offset offset where double is
     * @return Return double made from passed bytes.
     */
    public static double toDouble(final byte[] bytes, final int offset) {
        return Double.longBitsToDouble(toLong(bytes, offset, SIZEOF_LONG));
    }

    /**
     * Converts a byte array to a short value
     *
     * @param bytes byte array
     * @return the short value
     */
    public static short toShort(byte[] bytes) {
        return toShort(bytes, 0, SIZEOF_SHORT);
    }

    /**
     * Converts a byte array to a short value
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @param length length, has to be {@link #SIZEOF_SHORT}
     * @return the short value
     * @throws IllegalArgumentException if length is not {@link #SIZEOF_SHORT}
     *                                  or if there's not enough room in the array at the offset indicated.
     */
    public static short toShort(byte[] bytes, int offset, final int length) {
        if (length != SIZEOF_SHORT || offset + length > bytes.length) {
            throw explainWrongLengthOrOffset(bytes, offset, length, SIZEOF_SHORT);
        }
        if (UnsafeComparer.unaligned()) {
            return toShortUnsafe(bytes, offset);
        } else {
            short n = 0;
            n ^= bytes[offset] & 0xFF;
            n <<= 8;
            n ^= bytes[offset + 1] & 0xFF;
            return n;
        }
    }

    /**
     * Converts a byte array to an short value (Unsafe version)
     *
     * @param bytes  byte array
     * @param offset offset into array
     * @return the short value
     */
    public static short toShortUnsafe(byte[] bytes, int offset) {
        if (UnsafeComparer.littleEndian) {
            return Short.reverseBytes(UnsafeComparer.theUnsafe.getShort(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET));
        } else {
            return UnsafeComparer.theUnsafe.getShort(bytes,
                    (long) offset + UnsafeComparer.BYTE_ARRAY_BASE_OFFSET);
        }
    }


    interface Comparer<T> {
        int compareTo(
                T buffer1, int offset1, int length1, T buffer2, int offset2, int length2
        );
    }


    enum UnsafeComparer implements Comparer<byte[]> {
        INSTANCE;

        static final Unsafe theUnsafe;
        private static boolean unaligned = false;

        /**
         * The offset to the first element in a byte array.
         */
        static final int BYTE_ARRAY_BASE_OFFSET;

        static {
            if (UnsafeAccess.unaligned()) {
                theUnsafe = UnsafeAccess.theUnsafe;
            } else {
                // It doesn't matter what we throw;
                // it's swallowed in getBestComparer().
                throw new Error();
            }

            BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);

            // sanity check - this should never fail
            if (theUnsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
            unaligned = UnsafeAccess.unaligned();
        }

        static final boolean littleEndian =
                ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN);

        /**
         * Returns true if x1 is less than x2, when both values are treated as
         * unsigned long.
         * Both values are passed as is read by Unsafe. When platform is Little Endian, have to
         * convert to corresponding Big Endian value and then do compare. We do all writes in
         * Big Endian format.
         */
        static boolean lessThanUnsignedLong(long x1, long x2) {
            if (littleEndian) {
                x1 = Long.reverseBytes(x1);
                x2 = Long.reverseBytes(x2);
            }
            return (x1 + Long.MIN_VALUE) < (x2 + Long.MIN_VALUE);
        }

        /**
         * Returns true if x1 is less than x2, when both values are treated as
         * unsigned int.
         * Both values are passed as is read by Unsafe. When platform is Little Endian, have to
         * convert to corresponding Big Endian value and then do compare. We do all writes in
         * Big Endian format.
         */
        static boolean lessThanUnsignedInt(int x1, int x2) {
            if (littleEndian) {
                x1 = Integer.reverseBytes(x1);
                x2 = Integer.reverseBytes(x2);
            }
            return (x1 & 0xffffffffL) < (x2 & 0xffffffffL);
        }

        /**
         * Returns true if x1 is less than x2, when both values are treated as
         * unsigned short.
         * Both values are passed as is read by Unsafe. When platform is Little Endian, have to
         * convert to corresponding Big Endian value and then do compare. We do all writes in
         * Big Endian format.
         */
        static boolean lessThanUnsignedShort(short x1, short x2) {
            if (littleEndian) {
                x1 = Short.reverseBytes(x1);
                x2 = Short.reverseBytes(x2);
            }
            return (x1 & 0xffff) < (x2 & 0xffff);
        }

        /**
         * Checks if Unsafe is available
         *
         * @return true, if available, false - otherwise
         */
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

        /**
         * Lexicographically compare two arrays.
         *
         * @param buffer1 left operand
         * @param buffer2 right operand
         * @param offset1 Where to start comparing in the left buffer
         * @param offset2 Where to start comparing in the right buffer
         * @param length1 How much to compare from the left buffer
         * @param length2 How much to compare from the right buffer
         * @return 0 if equal, < 0 if left is less than right, etc.
         */
        @Override
        public int compareTo(byte[] buffer1, int offset1, int length1,
                             byte[] buffer2, int offset2, int length2) {

            // Short circuit equal case
            if (buffer1 == buffer2 &&
                    offset1 == offset2 &&
                    length1 == length2) {
                return 0;
            }
            final int minLength = Math.min(length1, length2);
            final int minWords = minLength / SIZEOF_LONG;
            final long offset1Adj = offset1 + BYTE_ARRAY_BASE_OFFSET;
            final long offset2Adj = offset2 + BYTE_ARRAY_BASE_OFFSET;

            /*
             * Compare 8 bytes at a time. Benchmarking shows comparing 8 bytes at a
             * time is no slower than comparing 4 bytes at a time even on 32-bit.
             * On the other hand, it is substantially faster on 64-bit.
             */
            // This is the end offset of long parts.
            int j = minWords << 3; // Same as minWords * SIZEOF_LONG
            for (int i = 0; i < j; i += SIZEOF_LONG) {
                long lw = theUnsafe.getLong(buffer1, offset1Adj + (long) i);
                long rw = theUnsafe.getLong(buffer2, offset2Adj + (long) i);
                long diff = lw ^ rw;
                if (diff != 0) {
                    return lessThanUnsignedLong(lw, rw) ? -1 : 1;
                }
            }
            int offset = j;

            if (minLength - offset >= SIZEOF_INT) {
                int il = theUnsafe.getInt(buffer1, offset1Adj + offset);
                int ir = theUnsafe.getInt(buffer2, offset2Adj + offset);
                if (il != ir) {
                    return lessThanUnsignedInt(il, ir) ? -1 : 1;
                }
                offset += SIZEOF_INT;
            }
            if (minLength - offset >= SIZEOF_SHORT) {
                short sl = theUnsafe.getShort(buffer1, offset1Adj + offset);
                short sr = theUnsafe.getShort(buffer2, offset2Adj + offset);
                if (sl != sr) {
                    return lessThanUnsignedShort(sl, sr) ? -1 : 1;
                }
                offset += SIZEOF_SHORT;
            }
            if (minLength - offset == 1) {
                int a = (buffer1[(int) (offset1 + offset)] & 0xff);
                int b = (buffer2[(int) (offset2 + offset)] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return length1 - length2;
        }
    }


}
