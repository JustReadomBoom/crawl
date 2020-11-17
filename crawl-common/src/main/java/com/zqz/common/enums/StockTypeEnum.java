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

//    ALL("全部股票", "C._AB"),
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

    public static String getTypeByDesc(String desc) {
        if(desc == null) {
            return null;
        }
        for (StockTypeEnum enumInstance : values()) {
            if (enumInstance.desc.equals(desc)) {
                return enumInstance.type;
            }
        }
        return null;
    }

    //通过股票类型获取url参数fs
    public static String getUrlParamFSByType(String desc){
        if(desc == null) {
            return null;
        }
        if(SH_SZ_A.getDesc().equals(desc)){
            return "m:0+t:6+f:!2,m:0+t:13+f:!2,m:0+t:80+f:!2,m:1+t:2+f:!2,m:1+t:23+f:!2";
        }else if(SH_A.getDesc().equals(desc)){
            return "m:1+t:2+f:!2,m:1+t:23+f:!2";
        }else if(SZ_A.getDesc().equals(desc)){
            return "m:0+t:6+f:!2,m:0+t:13+f:!2,m:0+t:80+f:!2";
        }else if(CREATE.getDesc().equals(desc)){
            return "m:0+t:80+f:!2";
        }else if(MID_SMALL.getDesc().equals(desc)){
            return "m:0+t:13+f:!2";
        }else if(SH_B.getDesc().equals(desc)){
            return "m:1+t:3+f:!2";
        }else if(SZ_B.getDesc().equals(desc)){
            return "m:0+t:7+f:!2";
        }
        return null;
    }
}
