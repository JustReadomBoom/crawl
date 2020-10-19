package com.zqz.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zqz
 * @Description: 股票类型枚举
 * @Date: Created in 14:34 2020/10/19
 */
@Getter
@AllArgsConstructor
public enum StockTypeEnum {

    ALL("全部股票", "C._AB"),
    SH_SZ_A("沪深A股", "C._A"),
    SH_A("沪市A股", "C.2"),
    SZ_A("深市A股", "C._SZAME"),
    CREATE("创业板", "C.80"),
    MID_SMALL("中小版", "C.13"),
    SH_B("沪市B股", "C.3"),
    SZ_B("深市B股", "C.7");

    private String type;
    private String desc;

    public static String getDescByType(String type) {
        if(type == null) {
            return null;
        }
        for (StockTypeEnum enumInstance : values()) {
            if (enumInstance.type.equals(type)) {
                return enumInstance.desc;
            }
        }
        return null;
    }


}
