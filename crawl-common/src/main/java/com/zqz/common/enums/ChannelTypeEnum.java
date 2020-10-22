package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:48 2020/10/22
 */
@Getter
@AllArgsConstructor
public enum ChannelTypeEnum {

    DFCF("DFCF", "东方财富");


    private String type;
    private String desc;
}
