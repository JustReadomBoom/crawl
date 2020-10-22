package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:50 2020/10/22
 */
@Getter
@AllArgsConstructor
public enum OptStatusEnum {

    PROCESSING("P", "处理中"),
    SUCCESS("S", "完成"),
    ERROR("E", "异常");

    private String status;
    private String desc;

}
