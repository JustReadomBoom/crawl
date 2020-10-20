package com.zqz.common.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 17:48 2020/10/20
 */
@Data
public class WebResp<T> {

    private Integer code;

    private String msg;

    private Integer count;

    private List<T> data;
}
