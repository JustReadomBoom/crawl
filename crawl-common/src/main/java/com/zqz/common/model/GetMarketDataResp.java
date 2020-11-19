package com.zqz.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:59 2020/11/19
 */
@Data
public class GetMarketDataResp implements Serializable {
    private static final long serialVersionUID = 890603253708794238L;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date processDate;

    private String stockCode;

    private String stockName;

    private BigDecimal priceNew;

    private BigDecimal stockChange;

    private BigDecimal changeAmt;

    private String tradCount;

    private String tradAmt;

    private String amplitude;

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    private BigDecimal openPrice;

    private BigDecimal closeLast;

    private Integer crawlCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date uTime;


}
