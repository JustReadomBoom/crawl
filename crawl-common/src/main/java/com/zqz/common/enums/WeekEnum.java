package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 16:21 2021/3/31
 */
@Getter
@AllArgsConstructor
public enum  WeekEnum {

    MONDAY("星期一"),
    TUESDAY("星期二"),
    WEDNESDAY("星期三"),
    THURSDAY("星期四"),
    FRIDAY("星期五"),
    SATURDAY("星期六"),
    SUNDAY("星期日");

    private String week;

}
