package com.zqz.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class DfcfRecord implements Serializable {
    private static final long serialVersionUID = -6576069100908303933L;

    private Long id;

    private String stockMarket;

    private String stockRank;

    private String stockCode;

    private String stockName;

    private BigDecimal priceNew;

    private BigDecimal stockChange;

    private Date processDate;

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

    private Integer stockPage;

    private Date countTime;

    private String timeVersion;

    private String crawlerVersion;

    private Integer crawlCount;

    private Date cTime;

    private Date uTime;

    private String someinfo;
}