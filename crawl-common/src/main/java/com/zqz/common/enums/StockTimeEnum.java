package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description: 时间排行类型枚举
 * @Date: Created in 14:42 2020/10/19
 */
@Getter
@AllArgsConstructor
public enum StockTimeEnum {

    TODAY("今日排行", "(BalFlowMain)");

    private String type;
    private String desc;

    public static String getDescByType(String type) {
        if(type == null) {
            return null;
        }
        for (StockTimeEnum enumInstance : values()) {
            if (enumInstance.type.equals(type)) {
                return enumInstance.desc;
            }
        }
        return null;
    }

}
