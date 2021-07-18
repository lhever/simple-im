package com.lhever.common.core.utils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * * author wangwei
 * * CREATE ON 2017/11/20 14:46
 * * DECRIPTION
 * * WWW.JOINTEM.COM
 **/
public class RandomUtil {

    private static AtomicInteger number = new AtomicInteger(0);
    private static String lastTime = "";

    private static synchronized int getIncrementOneNumber(boolean isSleep){
        if (isSleep){
            try {
                //防止一秒中出现重复数据
                Thread.sleep(112);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int n = number.incrementAndGet();
        if (n>9){
            number.set(0);
            n=0;
        }
        return n;
    }

    //获取随机10位数字(时间戳+递增数字),每一秒只可以拿到10个,并发则排队等待
    public static synchronized String getTenRandomNum(){
        String result;
        String time = String.valueOf(new Date().getTime()/1000);
        if (lastTime.equals(time)){
            result = time.substring(1)+getIncrementOneNumber(true);
        }else{
            result = time.substring(1)+getIncrementOneNumber(false);
        }
        lastTime = time;
        return result;
    }

    public static String getRandomNum(Integer num){
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<num;i++){
            stringBuffer.append(random.nextInt(10));
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(getTenRandomNum());
        List<String> aaa = new ArrayList<>();
        Set<String> sss = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String temp = getTenRandomNum();
            System.out.println(temp);
            aaa.add(temp);
            sss.add(temp);
        }
        System.out.println(aaa.size());
        System.out.println(sss.size());
    }
}
