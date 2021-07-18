
package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期时间工具类
 */
public class DateFormatUtils {

    protected static final Logger LOG = LoggerFactory.getLogger(DateFormatUtils.class);

    public static final String DATA_FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    public static final String DATA_FORMAT_yyyy_MM_dd_T_HH_mm_ss_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String DATA_FORMAT_yyyy_MM_dd_T_HH_mm_ss_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String DATA_FORMAT_yyyyMM = "yyyyMM";

    public static final String DATA_FORMAT_yyyy = "yyyy";

    public static final String DATA_FORMAT_yyyyMMdd = "yyyyMMdd";

    public static final String DATA_FORMAT_yyyySlashMMSlashdd = "yyyy/MM/dd";

    public static final String DATA_FORMAT_yyyy_MM_dd = "yyyy-MM-dd";

    public static final String DATA_FORMAT_yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";

    public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final String HH_MM_SS = "HH:mm:ss";

    public static final String HH_MM = "HH:mm";

    public static final String DATA_FORMAT_yyyy_MM_dd_HH_mm = "yyyy/MM/dd HH:mm";

    public static final String DATA_FORMAT_yyyySlashMMSlashdd_HH_mm_ss = "yyyy/MM/dd HH:mm:ss";


    private static final String[][] patterns = new String[][] {
       new String[]{"^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}[Z]{1})$", "yyyy-MM-dd'T'HH:mm:ssXXX"},
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[Z]{1})$)", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"},
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]{1}\\d{2}:\\d{2})$)", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"},
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[+-]{1}\\d{2,4})$)", "yyyy-MM-dd'T'HH:mm:ss.SSSZ"}, //2020-10-16T19:08:00.000+0000
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$)", "yyyy-MM-dd HH:mm:ss"},
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})$)", "yyyy-MM-dd HH:mm:ss.SSS"},
       new String[]{"(^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}\\.\\d{3})$)", "yyyy-MM-dd'T'HH:mm:ss.SSS"},
    };



    public static boolean matchRegex(String s, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }
    /**
     * date 转 localDate
     *
     * @param date date
     * @return localDate
     */
    public static LocalDate dateLocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    /**
     * 字符串转日期Date对象，该方法能根据字符串的格式智能匹配日期格式
     *
     * @param dateStr
     * @return
     * @author lihong10 2018/12/27 16:16
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:16
     * @modify by reason:{原因}
     */
    public static Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        Date date = null;
        for (String[] pattern : patterns) {
            if (!matchRegex(dateStr, pattern[0])) {
                continue;
            }
            LOG.debug("date[{}] is match pattern[{}]", dateStr, pattern[1]);
            date = stringToDate(dateStr, pattern[1], true);
            return date;
        }

        String specialPattern = "(^(\\d{4}-\\d{2}-\\d{2}[T]{1}\\d{2}:\\d{2}:\\d{2}\\.\\d{3}[\\s]{1}\\d{2}:\\d{2})$)";
        //如果匹配特殊格式，修饰后再转换
        if (matchRegex(dateStr, specialPattern)) {
            char[] chars = dateStr.toCharArray();
            chars[chars.length - 6] = '+'; //空白符转+号
            date = stringToDate(new String(chars), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", true);
            return date;
        }

        try {
            date = new Date(Long.parseLong(dateStr));
        } catch (NumberFormatException e) {
            LOG.debug("parse dateStr:{} to long error", dateStr, e);
            throw new IllegalArgumentException("日期" + dateStr + "不是long型", e);
        }
        return date;
    }

    @Deprecated
    //该方法不具有通用性，建议移除或重构
    public static boolean isToday(Date inputJudgeDate) {
        boolean flag = false;
        if(null == inputJudgeDate){
            return flag;
        }
        //获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        String beginTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);
        } catch (ParseException e) {

        }
        if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    @Deprecated
    //该方法不具有通用性，建议移除或重构
    public static boolean isTodayOrYesterday(Date inputJudgeDate) {
        boolean flag = false;
        if(null == inputJudgeDate){
            return flag;
        }
        //获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        SimpleDateFormat dateFormat1 =new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,-24);
        String yesterdayDate = dateFormat1.format(calendar.getTime());
        String beginTime = yesterdayDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);
        } catch (ParseException e) {

        }
        if(inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    public static Date addDay(Date inputDate,Integer num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(inputDate);
        ca.add(Calendar.DATE, num);// num为增加的天数
        return ca.getTime();
    }

    @Deprecated
    //该方法不具有通用性，建议移除或重构
    public static int caculateTotalTime(Date startTime,Date endTime) {
        if(null == endTime){
            return 0;
        }
        Long l = 0L;
        long ts = startTime.getTime();
        long te = endTime.getTime();
        if(ts > te){
            return 0;
        }
        l = (te - ts) / (1000 * 60 * 60 * 24);
        return l.intValue();
    }

    public static void main(String[] args) {
        String date = "2020-02-17T10:42:00Z";
        System.out.println(isToday(null));
        System.out.println(caculateTotalTime(toDate(date),addDay(toDate(date),2)));
        System.out.println(isToday(toDate(date)));
        System.out.println(isTodayOrYesterday(toDate(date)));

        System.out.println(toDate("2020-10-16T19:08:00.000+0800"));
        System.out.println(toDate("2020-10-16T19:08:00.000+0800"));
    }

    /**
     * 将长整型的日期（单位是毫秒）转换成iso8601格式
     *
     * @param timestamp
     * @return
     * @author lihong10 2018/12/27 16:17
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:17
     * @modify by reason:{原因}
     */
    public static String unixToIso(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Date date = new Date(timestamp);
        String dateStr = toISO8601DateString(date);
        LOG.debug("timestamp:{}  => ISODATE:{}", timestamp, dateStr);
        return dateStr;
    }


    public static String toISO8601DateString(Date date) {
        return toISO8601DateString(date, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }


    /**
     * 获取gmt时间
     * @return
     */
    public static String getGmtTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE,d MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String str = sdf.format(date);
        return str;
    }

    public static String getGmtTime() {
        return getGmtTime(new Date());
    }

    /**
     * 将日期对象转换成iso8601格式字符串
     *
     * @param date
     * @return
     * @author lihong10 2018/12/27 16:17
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:17
     * @modify by reason:{原因}
     */
   public static String toISO8601DateString(Date date, String format) {
//		TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat(format);
//		df.setTimeZone(tz);
        String nowAsISO = null;
        try {
            nowAsISO = df.format(date);
        } catch (Exception e) {
            LOG.info("to format " + format + " error", e);
        }
        return nowAsISO;
    }




    /**
     * 获取当天0点时间
     * @author lihong10 2019/11/20 11:52
     * @param time
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/11/20 11:52
     * @modify by reason:{原因}
     */
    public static Date getToday(Long time) {
        return getToday(new Date(time));
    }

    /**
     * 获取当天0点时间
     * @author lihong10 2019/11/20 11:52
     * @param date
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/11/20 11:52
     * @modify by reason:{原因}
     */
    public static Date getToday(Date date) {
        String str = dateToString(date, DATA_FORMAT_yyyy_MM_dd);
        str = str + " 00:00:00";
        Date today = stringToDate(str, DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        return today;
    }
    public static Date getTodayEnd(Date date) {
        String str = dateToString(date, DATA_FORMAT_yyyy_MM_dd);
        str = str + " 23:59:59";
        Date today = stringToDate(str, DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        return today;
    }


    /**
     * 将日期转换成字符串，模版自定义
     *
     * @param date
     * @param pattern
     * @return
     * @author lihong10 2018/12/27 16:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:19
     * @modify by reason:{原因}
     */
    public static String dateToString(Date date, String pattern) {

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            LOG.error("convert to format " + pattern + " error", e);

        }
        return null;
    }

    /**
     * 将字符串转换成日期，模版自定义
     *
     * @param dateStr
     * @param pattern
     * @return
     * @author lihong10 2018/12/27 16:19
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:19
     * @modify by reason:{原因}
     */
    public static Date stringToDate(String dateStr, String pattern) {
        return stringToDate(dateStr, pattern, false);
    }

    public static Date stringToDate(String dateStr, String pattern, boolean error) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (Exception e) {
            LOG.error("parse date: " + dateStr + " by pattern: " + pattern + " error", e);
            if (error) {
                throw new IllegalArgumentException("日期" + dateStr + "格式转换错误", e);
            }
        }
        return date;
    }

    /**
     * 获取当前日期
     *
     * @return
     * @author dingzhongcheng 2019/11/15 14:40
     */
    public static Date getCurrentDateTime() {
        return new Date();
    }

    /**
     * 获取当前时间的Timestamp
     *
     * @return
     * @author dingzhongcheng 2019/11/15 14:41
     */
    @Deprecated
    public static Timestamp getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
        String time = sdf.format(new Date());
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }


    /**
     * Date转化为Timestamp, 方法不通用
     * @param date
     * @return
     * @author dingzhongcheng 2019/11/15 14:44
     */
    @Deprecated
    public static Timestamp dateToTimestamp(Date date) {
        return dateToTimestamp(date, DATA_FORMAT_yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * Date转化为Timestamp
     *
     * @param date
     * @param format
     * @return
     * @author dingzhongcheng 2019/11/15 14:45
     */
    public static Timestamp dateToTimestamp(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(date);
        Timestamp ts = Timestamp.valueOf(time);
        return ts;
    }


    /**
     * 该方法不具有通用性，小区管理平台用到，无奈暂且保留，后续可能会移除
     * @description iso时间 获取本地时间，无T
     * @author panyue
     * @date 2018/10/30 16:10
     */
    @Deprecated
    public static String getIsoTimeDate(String isoTime){
        if(StringUtils.isBlank(isoTime)){
            return null;
        }
        return isoTime.substring(0,isoTime.length()-6).replace("T"," ");
    }

    @Deprecated
    public static String getIsoTimeDateT(String isoTime){
        if(StringUtils.isBlank(isoTime)){
            return null;
        }
        return isoTime.replace("T"," ");
    }

    public static Date addHour(Date inputDate, Integer num) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(inputDate);
        ca.add(Calendar.HOUR_OF_DAY, num);// num为增加的小时
        return ca.getTime();
    }

    /** 两个日期相减得到的天数 */
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime()) / (1000 * 60 * 60 * 24);

        return new Long(diff).intValue();
    }

}
