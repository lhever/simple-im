package com.lhever.common.core.test;

import com.lhever.common.core.consts.RegExp;
import com.lhever.common.core.utils.Int2BinaryStringUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.BitSet;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/5/16 10:19
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/5/16 10:19
 * @modify by reason:{方法名}:{原因}
 */
public class RexExpTest {

    /**
     * 密码校验正则表达式, 改正则要求密码至少包含小写字母、大写字母、数字、特殊字符中的至少2种组合，并且长度在 8 ~16
     * 该正则表达式使用排除法，排除了全大写、全小写、全数字、全字母的特殊情况
     */
    public final String PASSWORD = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{8,16}$";

    public final String PASSWORD_1 = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)\\W{8,16}$";


    /**
     * 1、排除大写字母、小写字母、数字、特殊符号中1种组合、2种组合、3种组合，那么就只剩下4种都包含的组合了
     * <p>
     * 　　2、表达式为：^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$
     * <p>
     * 　　3、拆分解释：其中（2）-（6）运用了零宽断言、环视等正则功能
     * <p>
     * 　　　　（1）^匹配开头
     * <p>
     * 　　　　（2）(?![A-Za-z0-9]+$)匹配后面不全是（大写字母或小写字母或数字）的位置，排除了（大写字母、小写字母、数字）的1种2种3种组合
     * <p>
     * 　　　　（3）(?![a-z0-9\\W]+$)同理，排除了（小写字母、数字、特殊符号）的1种2种3种组合
     * <p>
     * 　　　　（4）(?![A-Za-z\\W]+$)同理，排除了（大写字母、小写字母、特殊符号）的1种2种3种组合
     * <p>
     * 　　　　（5）(?![A-Z0-9\\W]+$)同理，排除了（大写字母、数组、特殊符号）的1种2种3种组合
     * <p>
     * 　　　　（6）[a-zA-Z0-9\\W]匹配（小写字母或大写字母或数字或特殊符号）因为排除了上面的组合，所以就只剩下了4种都包含的组合了
     * <p>
     * 　　　　（7）{8,}8位以上
     * <p>
     * 　　　　（8）$匹配字符串结尾
     */
    public static final String PW_PATTERN = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$";


    public static final String PW_PATTERN_ =

            "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![\\W_]+$)[^\\u4e00-\\u9fa5]{8,16}$";


    //字母数字下划线 键盘上的特殊符号
    public static final String aa = "^[a-zA-Z0-9_~`!@#$%^&*()\\-+<>=\\[\\]\\{\\}:;\'\",\\./\\?\\|\\\\]{8,16}$";


    public static final String bb = "^(?![A-Z]+$)(?![a-z]+$)(?!\\d+$)(?![_~`!@#$%^&*()\\-+<>=\\[\\]\\{\\}:;\'\",\\./\\?\\|\\\\]+$)[a-zA-Z0-9_~`!@#$%^&*()\\-+<>=\\[\\]\\{\\}:;\'\",\\./\\?\\|\\\\]{8,16}$";


    @Test
    public void alpha_num_special_Underline() {
        System.out.println("~~~~~~~~".matches(aa));
        System.out.println("`````````".matches(aa));
        System.out.println("!!!!!!!!!".matches(aa));
        System.out.println("@@@@@@@@".matches(aa));
        System.out.println("#########".matches(aa));
        System.out.println("$$$$$$$$$".matches(aa));
        System.out.println("%%%%%%%%%%%".matches(aa));
        System.out.println("^^^^^^^^^".matches(aa));
        System.out.println("&&&&&&&&&".matches(aa));
        System.out.println("*********".matches(aa));
        System.out.println("(((((((((".matches(aa));
        System.out.println("))))))))))".matches(aa));

        System.out.println("--------------------------------");
        System.out.println("-----------".matches(aa));

        System.out.println("____________".matches(aa));
        System.out.println("+++++++++++_".matches(aa));
        System.out.println("=========())".matches(aa));
        System.out.println("[[[[[[[[[{{{{[".matches(aa));
        System.out.println("]]]]]]]]]]]]]]".matches(aa));
        System.out.println("{{{{{{{{{{{{{".matches(aa));
        System.out.println("}}}}}}}}}}}}}".matches(aa));
        System.out.println(":::::::::::::".matches(aa));
        System.out.println(";;;;;;;;;;;;;;".matches(aa));
        System.out.println("''''''''''''''".matches(aa));
        System.out.println("\"\"\"\"\"\"\"\"".matches(aa));
        System.out.println("||||||||||||".matches(aa));
        System.out.println("//////////////".matches(aa));
        System.out.println(".............".matches(aa));
        System.out.println(",,,,,,,,,,,,,".matches(aa));
        System.out.println("<<<<<<<<<<<<".matches(aa));
        System.out.println(">>>>>>>>".matches(aa));
        System.out.println("?????????????".matches(aa));
        System.out.println("aaaaaaaaaaaaa".matches(aa));
        System.out.println("AAAAAAAAAAAAA".matches(aa));
        System.out.println("11111111111".matches(aa));
        System.out.println("\\\\\\\\\\\\\\\\".matches(aa));
        System.out.println("\\\\\\\\\\\\\\\\".length());
        System.out.println("\\\\\\\\".matches(aa));
        System.out.println("\\\\\\\\".length());
    }


    @Test
    public void test3() {
        System.out.println("lihong10".matches(bb));
        System.out.println("123qaz!@#Q".matches(bb));
        System.out.println("123qaz!@#Q_>".matches(bb));
        System.out.println("12345678B".matches(bb));
        System.out.println("aaaabbbbb1".matches(bb));
        System.out.println("AAAABBBB2".matches(bb));
        System.out.println("<>?:\"'[}3".matches(bb));
    }

    @Test
    public void test4() {
        System.out.println("lihong10".matches(RegExp.USER_PASSWORD));
        System.out.println("123qaz!@#Q".matches(RegExp.USER_PASSWORD));
        System.out.println("123qaz!@#Q_>".matches(RegExp.USER_PASSWORD));
        System.out.println("12345678B".matches(RegExp.USER_PASSWORD));
        System.out.println("aaaabbbbb1".matches(RegExp.USER_PASSWORD));
        System.out.println("AAAABBBB2".matches(RegExp.USER_PASSWORD));
        System.out.println("<>?:\"'[}3".matches(RegExp.USER_PASSWORD));
        System.out.println("<>?:\"'[}我".matches(RegExp.USER_PASSWORD));
        System.out.println("<>?:\"'[ _".matches(RegExp.USER_PASSWORD));
        System.out.println("1 2 3 4 5    ".matches(RegExp.USER_PASSWORD));
        System.out.println(RegExp.USER_PASSWORD);
    }


    @Test
    public void test() {

        String a = "^(?!_)[a-zA-Z0-9_\u4e00-\u9fa5]+$";
        System.out.println("AAA".matches(a));
        System.out.println("李红10absc12A".matches(a));
        System.out.println("_".matches(a));
        System.out.println("_AAA".matches(a));
        System.out.println("_aAAA".matches(a));
        System.out.println("_李红".matches(a));
        System.out.println("李&红".matches(a));
    }


    @Test
    public void test11() {

        System.out.println("lihong11".matches(RegExp.USER_PASSWORD));
    }


    @Test
    public void test12() {
        BitSet set = new BitSet();
        set.set(0);
        set.set(1);
        set.set(63);
        set.set(64);
        set.set(65);
        set.set(66);


        System.out.println(Arrays.toString(set.toLongArray()));
        System.out.println(set.toString());
        BitSet bitSet = BitSet.valueOf(set.toByteArray());
        System.out.println(bitSet.get(0));
        System.out.println(bitSet.get(2));
        System.out.println(bitSet.get(64));


        BitSet bitSet1 = set.get(63, 66);
        System.out.println(bitSet1.toString());

        for (int i = 0; i < 128; i++) {
            System.out.println(Int2BinaryStringUtils.toFullBinaryString(3 << i)   + "   " + i + "<<");
        }
    }

    @Test
    public void test13() {


        for (int i = 0; i < 128; i++) {
            System.out.println(Int2BinaryStringUtils.toFullBinaryString(i)   + "   " + i );
            System.out.println(Int2BinaryStringUtils.toFullBinaryString(-i)   + "   " + -i );
        }
    }


}
