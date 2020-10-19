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
}
