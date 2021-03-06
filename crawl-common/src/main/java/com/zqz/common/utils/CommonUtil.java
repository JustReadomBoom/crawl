package com.zqz.common.utils;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:26 2020/10/19
 */
public class CommonUtil {

    private static final String UNIT_STRING_WAN = "万";
    private static final String UNIT_STRING_YI = "亿";
    private static final String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomJSCode() {
        String[] codes = str.split("");
        int num = 8;
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int a = random.nextInt(52);
            code.append(codes[a]);
        }
        return code.toString();
    }


    public static String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    public static String getRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }


    /**
     * 装换为万或亿为结尾
     *
     * @param amount 结果
     * @return
     */
    public static String formatNum(BigDecimal amount) {
        if (amount == null) {
            return "0";
        }
        if (amount.compareTo(new BigDecimal(10000)) < 0) {
            //如果小于1万
            return amount.stripTrailingZeros().toPlainString();
        }
        if (amount.compareTo(new BigDecimal(10000000)) < 0) {
            //如果大于1万小于1亿
            return amount.divide(new BigDecimal(10000), 2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + UNIT_STRING_WAN;
        }
        return amount.divide(new BigDecimal(100000000), 2, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + UNIT_STRING_YI;
    }
}
