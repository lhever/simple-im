package com.lhever.common.core.utils;

/**
 * 该类是一个将int型整数转成32位二进制字符串形式的工具类
 * Created by lihong10 on 2017/4/28.
 */
public class Int2BinaryStringUtils {


    private final static char[] digits = {'0', '1'};


    /**
     * 将int型整数转成32位的2进制形式
     * @param num
     * @return String
     */
    public static String toFullBinaryString(int num, boolean indent)
    {
        char[] buf = new char[32];
        int pos = 32;
        int mask = 1;
        do
        {
            buf[--pos] = digits[num & mask];
            num >>>= 1;
        }
        while (pos > 0);
        if (indent) {
            return formatString(buf);
        }

        return new String(buf, pos, 32);
    }

    public static String toFullBinaryString(int num)
    {
        char[] buf = new char[32];
        int pos = 32;
        int mask = 1;
        do
        {
            buf[--pos] = digits[num & mask];
            num >>>= 1;
        }
        while (pos > 0);

        return new String(buf, pos, 32);
    }


    public static String toFullBinaryString(long num)
    {
        char[] buf = new char[64];
        int pos = 64;
        int mask = 1;
        do
        {
            buf[--pos] = digits[(int)(num & mask)];
            num >>>= 1;
        }
        while (pos > 0);

        return new String(buf, pos, 64);
    }

    public static String toFullBinaryString(long num, boolean indent)
    {
        char[] buf = new char[64];
        int pos = 64;
        int mask = 1;
        do
        {
            buf[--pos] = digits[(int)(num & mask)];
            num >>>= 1;
        }
        while (pos > 0);

        if (indent) {
            return  formatString(buf);
        }

        return new String(buf, pos, 64);
    }


    public static String formatString(char[] buf) {

        //从后往前，每相隔8个元素就插入一个空格，一共插入8个空格
        char[] formatBuf = new char[buf.length + (buf.length / 8)];
        int k = formatBuf.length;
        for(int i = buf.length; i > 0; i--) {
            formatBuf[--k] = buf[i - 1];
            if ((i - 1) % 8 == 0) {
                //插入空格
                formatBuf[--k] = (char) 0;
            }
        }
        return new String(formatBuf, 1, formatBuf.length - 1);

    }



    public static String toFullBinaryString(byte num)
    {
        num &= 0xFF;
        char[] buf = new char[8];
        int pos = 8;
        int mask = 1;
        do
        {
            buf[--pos] = digits[num & mask];
            num >>>= 1;
        }
        while (pos > 0);

        return new String(buf, pos, 8);
    }

    public static String toFullBinaryString(char num)
    {
        num &= 0xFFFF;
        char[] buf = new char[16];
        int pos = 16;
        int mask = 1;
        do
        {
            buf[--pos] = digits[num & mask];
            num >>>= 1;
        }
        while (pos > 0);

        return new String(buf, pos, 16);
    }


    /**
     * 将int型整数转成32位的2进制形式
     * @param num
     * @return String
     */
    public static String toFullBinaryString2(int num)
    {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++)
        {
            chs[Integer.SIZE - 1 - i] = (char) ((num >> i & 1) + '0');
        }
        return new String(chs);
    }

    public static void printComplementCode(int a)
    {
        for (int i = 0; i < 32; i++)
        {
            // 0x80000000 是一个首位为1，其余位数为0的整数
            int t = (a & 0x80000000 >>> i) >>> (31 - i);
            System.out.print(t);
        }
        System.out.println();
    }




    /**
     * 测试
     * @param args
     */
    public static void main(String[] args)
    {
       /* System.out.println("方法一：" + toFullBinaryString(6053));
        System.out.println("JDK自带： " + Integer.toBinaryString(6053));

        System.out.println("方法一：" + toFullBinaryString(-2));
        System.out.println("JDK自带： " + Integer.toBinaryString(-2));

        System.out.println("------------------------------------------");

        System.out.println("方法二：" + toFullBinaryString2(6053));
        System.out.println("JDK自带： " + Integer.toBinaryString(6053));

        System.out.println("方法二：" + toFullBinaryString2(-2));
        System.out.println("JDK自带： " + Integer.toBinaryString(-2));*/

        System.out.println("方法一：" + toFullBinaryString(255));
//        System.out.println("JDK自带： " + Integer.toBinaryString(6053));


        System.out.println(toFullBinaryString('\uffff'));
        System.out.println(toFullBinaryString(0xFFFF));
        System.out.println(toFullBinaryString(0x80000000 ));

        printComplementCode(-10); // 11111111 11111111 11111111 11110110
        printComplementCode(10); // 11111111 11111111 11111111 11110110


        System.out.println(toFullBinaryString(10));
        System.out.println(toFullBinaryString(Integer.reverseBytes(10)));


        System.out.println(toFullBinaryString(1));
        System.out.println(toFullBinaryString(Integer.reverse(1)));
        System.out.println(Integer.reverse(1));


    }










}
