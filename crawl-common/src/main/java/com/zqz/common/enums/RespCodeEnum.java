package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:48 2020/10/22
 */
@Getter
@AllArgsConstructor
public enum RespCodeEnum {

    SUCCESS(0, "成功"),
    ERROR(9999, "系统异常"),
    NO_DATA(1, "无数据"),
    PROCESSING(2, "数据爬取中，请稍后查看!"),
    SELECT_DATE(3, "请选择日期"),
    STOP_DO(4, "3小时内不允许再次操作"),
    NO_USER(5, "用户不存在"),
    ERROR_PWD(6, "密码错误"),
    WEEK_LAST(7, "周末未开盘");

    private Integer code;
    private String msg;
}
