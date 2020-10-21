package com.zqz.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 09:14 2020/10/21
 */
@Data
public class GetDfcfDataResp implements Serializable {
    private static final long serialVersionUID = -7933667243538440113L;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date processDate;

    private String stockRank;

    private String stockMarket;

    private String stockCode;

    private String stockName;

    private BigDecimal priceNew;

    private BigDecimal stockChange;

    private BigDecimal mainNetInflowAmount;

    private BigDecimal mainNetProportion;

    private BigDecimal superBigPartNetInflowAmount;

    private BigDecimal superBigPartNetProportion;

    private BigDecimal bigPartNetInflowAmount;

    private BigDecimal bigPartNetProportion;

    private BigDecimal middlePartNetInflowAmount;

    private BigDecimal middlePartNetProportion;

    private BigDecimal litterPartNetInflowAmount;

    private BigDecimal litterPartNetProportion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;

}
