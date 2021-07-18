package com.lhever.common.core.converter.bytes;

//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.ByteBufUtil;
//import io.netty.buffer.PooledByteBufAllocator;

/**
 * <p>
 * 打印字节数组到日志的工具，便于排错使用，也可以当做hexdump工具使用
 * </p>
 *
 * @author lihong10 2019/6/12 19:26
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/6/12 19:26
 * @modify by reason:{方法名}:{原因}
 */
public class BytesPrinter {

    public static final String NEWLINE = System.getProperty("line.separator", "\n");
    private static final char[] BYTE2CHAR = new char[256];
    private static final String[] HEXDUMP_ROWPREFIXES = new String[65536 >>> 4];
    private static final String[] HEXPADDING = new String[16];
    private static final String[] BYTE2HEX = new String[256];
    private static final String[] BYTEPADDING = new String[16];
    private static final String[] BYTE2HEX_PAD = new String[256];
    private static final String[] BYTE2HEX_NOPAD = new String[256];


    static {
        int i;

        // Generate the lookup table for hex dump paddings
        for (i = 0; i < HEXPADDING.length; i ++) {
            int padding = HEXPADDING.length - i;
            StringBuilder buf = new StringBuilder(padding * 3);
            for (int j = 0; j < padding; j ++) {
                buf.append("   ");
            }
            HEXPADDING[i] = buf.toString();
        }

        // Generate the lookup table for the start-offset header in each row (up to 64KiB).
        for (i = 0; i < HEXDUMP_ROWPREFIXES.length; i ++) {
            StringBuilder buf = new StringBuilder(12);
            buf.append(NEWLINE);
            buf.append(Long.toHexString(i << 4 & 0xFFFFFFFFL | 0x100000000L));
            buf.setCharAt(buf.length() - 9, '|');
            buf.append('|');
            HEXDUMP_ROWPREFIXES[i] = buf.toString();
        }

        // Generate the lookup table that converts a byte into a 2-digit hexadecimal integer.
        for (i = 0; i < BYTE2HEX_PAD.length; i++) {
            String str = Integer.toHexString(i);
            BYTE2HEX_PAD[i] = i > 0xf ? str : ('0' + str);
            BYTE2HEX_NOPAD[i] = str;
        }

        // Generate the lookup table for byte-to-hex-dump conversion
        for (i = 0; i < BYTE2HEX.length; i ++) {
            BYTE2HEX[i] = ' ' + byteToHexStringPadded(i);
        }

        // Generate the lookup table for byte dump paddings
        for (i = 0; i < BYTEPADDING.length; i ++) {
            int padding = BYTEPADDING.length - i;
            StringBuilder buf = new StringBuilder(padding);
            for (int j = 0; j < padding; j ++) {
                buf.append(' ');
            }
            BYTEPADDING[i] = buf.toString();
        }

        // Generate the lookup table for byte-to-char conversion
        for (i = 0; i < BYTE2CHAR.length; i ++) {
            if (i <= 0x1f || i >= 0x7f) {
                BYTE2CHAR[i] = '.';
            } else {
                BYTE2CHAR[i] = (char) i;
            }
        }
    }


    /**
     * Determine if the requested {@code index} and {@code length} will fit within {@code capacity}.
     * @param index The starting index.
     * @param length The length which will be utilized (starting from {@code index}).
     * @param capacity The capacity that {@code index + length} is allowed to be within.
     * @return {@code true} if the requested {@code index} and {@code length} will fit within {@code capacity}.
     * {@code false} if this would result in an index out of bounds exception.
     */
    public static boolean isOutOfBounds(int index, int length, int capacity) {
        return (index | length | (index + length) | (capacity - (index + length))) < 0;
    }



    /**
     * 打印字节数组的关键方法
     * @param dump 打印的内容会被追加到dump中
     * @param buf 字节数组，被打印的对象
     * @param offset 偏移或起始位置
     * @param length 打印的字节长度
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/8/1 19:11
     * @modify by reason:{原因}
     */
    public static void print(StringBuilder dump, byte[] buf, int offset, int length) {
        if (isOutOfBounds(offset, length, buf.length)) {
            throw new IndexOutOfBoundsException(
                    "expected: " + "0 <= offset(" + offset + ") <= offset + length(" + length
                            + ") <= " + "buf.length(" + buf.length + ')');
        }
        if (length == 0) {
            return;
        }
        dump.append(
                "         +-------------------------------------------------+" +
                        NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" +
                        NEWLINE + "+--------+-------------------------------------------------+----------------+");

        final int startIndex = offset;
        final int fullRows = length >>> 4;
        final int remainder = length & 0xF;

        // Dump the rows which have 16 bytes.
        for (int row = 0; row < fullRows; row ++) {
            int rowStartIndex = (row << 4) + startIndex;

            // Per-row prefix.
            appendHexDumpRowPrefix(dump, row, rowStartIndex);

            // Hex dump
            int rowEndIndex = rowStartIndex + 16;
            for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                dump.append(BYTE2HEX[getUnsignedByte(buf[j])]);
            }
            dump.append(" |");

            // ASCII dump
            for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                dump.append(BYTE2CHAR[getUnsignedByte(buf[j])]);
            }
            dump.append('|');
        }

        // Dump the last row which has less than 16 bytes.
        if (remainder != 0) {
            int rowStartIndex = (fullRows << 4) + startIndex;
            appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);

            // Hex dump
            int rowEndIndex = rowStartIndex + remainder;
            for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                dump.append(BYTE2HEX[getUnsignedByte(buf[j])]);
            }
            dump.append(HEXPADDING[remainder]);
            dump.append(" |");

            // Ascii dump
            for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                dump.append(BYTE2CHAR[getUnsignedByte(buf[j])]);
            }
            dump.append(BYTEPADDING[remainder]);
            dump.append('|');
        }

        dump.append(NEWLINE +
                "+--------+-------------------------------------------------+----------------+");
    }

    private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
        if (row < HEXDUMP_ROWPREFIXES.length) {
            dump.append(HEXDUMP_ROWPREFIXES[row]);
        } else {
            dump.append(NEWLINE);
            dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFFL | 0x100000000L));
            dump.setCharAt(dump.length() - 9, '|');
            dump.append('|');
        }
    }

    private static String byteToHexStringPadded(int value) {
        return BYTE2HEX_PAD[value & 0xff];
    }


    /**
     * 字节转换成无符号数
     * @param b
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/8/1 19:13
     * @modify by reason:{原因}
     */
    private static short getUnsignedByte(byte b) {
        return (short)(b & 0xFF);
    }



    private static String testPrint(String a) {
        byte[] bytes = a.getBytes();
        StringBuilder builder = new StringBuilder();
        print(builder, bytes, 0, bytes.length);
        System.out.println(builder);
        return builder.toString();

    }

    /*private static String testPrint2(String b) {
        byte[] bytes = b.getBytes();
        PooledByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator(false);
        //这里：  byteBuf的实际类型是：PooledUnsafeHeapByteBuf
        ByteBuf byteBuf = pooledByteBufAllocator.heapBuffer(bytes.length);

        byteBuf.writeBytes(bytes);

        StringBuilder builder = new StringBuilder();
        ByteBufUtil.appendPrettyHexDump(builder, byteBuf);
        System.out.println(builder);
        return builder.toString();
    }*/




    public static void main(String... args) {

        System.out.println("\n\r");
        System.out.println(Long.toHexString(0 & 0xFFFFFFFFL | 0x100000000L));
        System.out.println(Long.toHexString(1 & 0xFFFFFFFFL | 0x100000000L));

        System.out.println("\n\r");
        System.out.println(1 << 4);
        System.out.println(Long.toHexString(16 & 0xFFFFFFFFL | 0x100000000L));

        testPrint("abcd123!@#$%^&");


       /* String str1 = "abcdefghijklmnopqrstuvwxyz1234567890";
        System.out.println("equals: "  + testPrint(str1).equals(testPrint2(str1)));

        String str2 = "我是中国人";
        System.out.println("equals: "  + testPrint(str2).equals(testPrint2(str2)));*/
    }






}


