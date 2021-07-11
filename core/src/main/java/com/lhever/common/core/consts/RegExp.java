package com.lhever.common.core.consts;

/**
 * 常用的正则表达式
 */
public class RegExp {
    /**
     * url
     */
    public static final String URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
    /**
     * 环信app_key的格式
     */
    public static final String APP_KEY = "^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+";

    /**
     * 手机号
     */
//	public final String PHONE = "^1\\d{10}$";
    public static final String PHONE = "^[1][0-9]{10}$";
    /**
     * 字符串内部的uuid部分
     */
    public static final String UUID_PART = "[a-zA-Z0-9]{32}";
    /**
     * UUID
     */
    public static final String UUID = "^" + UUID_PART + "$";
    /**
     * email
     * public final String EMAIL = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
     */
    public static final String EMAIL = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";

    /**
     * 账号： 1到32位， 只能包含字母数字和下划线
     */
    public static final String ACCOUNT = "^[0-9a-zA-Z_]{1,32}$";

    /**
     * 纯数字
     */
    public static final String NUMBER = "^\\d+$";

    /**
     * 日期(形式上是日期的字符串)
     */
    public static final String Date = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

    /**
     * yyyyMMdd格式的日期字符串
     */
    public static final String DateStr = "^[0-9]{8}$";

    /**
     * hh:mm:ss格式的时间字符串
     */
    public static final String TimeStr = "^\\d{2}[:]\\d{2}[:]\\d{2}$";

    /**
     * (UUID + 图片文件后缀) 形式的图片文件名
     */
    public static final String IMGFILENAME = "" + "[a-zA-Z0-9]{32}" + "\\.(JPEG|jpeg|JPG|jpg|GIF|gif|BMP|bmp|PNG|png)";

    /**
     * 首尾带有 # 号的字符串
     */
    public static final String WRAP_BY_OCTOTHORPE = "#(.*?)#";

    /**
     * 密码校验正则表达式, 改正则要求密码至少包含小写字母、大写字母、数字、下划线、中文等其他特殊符号中的至少2种组合，并且长度在 8 ~16
     * 该正则表达式使用排除法，排除了全大写、全小写、全数字、全字母的特殊情况
     */
    public static final String PASSWORD = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{8,16}$";


    /*
     *列举出了键盘可输入的特殊字符, 该变量仅仅是单纯的字符串，不是正则式，使用private修饰
     */
    private static final String KEYBOFD_SPECIAL_CHAR = "_~`!@#$%^&*()\\-+<>=\\[\\]\\{\\}:;\'\",\\./\\?\\|\\\\";

    /**
     * 开放平台开发者、管理员注册密码正则表达式
     */
    public static final String USER_PASSWORD =
            "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![" + KEYBOFD_SPECIAL_CHAR +
                    "]+$)[a-zA-Z0-9" + KEYBOFD_SPECIAL_CHAR + "]{8,16}$";


    /**
     * 字符串中的空格，制表位，换行符
     */
    public static final String WHITE_CHAR = "\\s*|\t|\r|\n";


    /**
     * 字母、数字、下划线、中文，下划线不允许开头
     */
    public static final String NUM_ALPHA_UNDERLINE_NOT_FIRST = "^(?!_)[a-zA-Z0-9_\u4e00-\u9fa5]+$";

    /**
     * 字母、数字、下划线、中文字符
     */
    public static final String NUM_ALPHA_UNDERLINE = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";


}
