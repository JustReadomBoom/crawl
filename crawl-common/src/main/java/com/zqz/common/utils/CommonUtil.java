package com.zqz.common.utils;

import java.util.Random;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:26 2020/10/19
 */
public class CommonUtil {

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
}
