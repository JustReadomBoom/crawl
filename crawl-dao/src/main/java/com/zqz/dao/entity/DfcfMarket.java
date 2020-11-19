package com.zqz.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DfcfMarket implements Serializable {
    private Long id;

    private Date processDate;

    private String stockCode;

    private String stockName;

    private BigDecimal priceNew;

    private BigDecimal stockChange;

    private BigDecimal changeAmt;

    private BigDecimal tradCount;

    private BigDecimal tradAmt;

    private BigDecimal amplitude;

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    private BigDecimal openPrice;

    private BigDecimal closeLast;

    private Integer crawlCount;

    private Date cTime;

    private Date uTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode == null ? null : stockCode.trim();
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName == null ? null : stockName.trim();
    }

    public BigDecimal getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(BigDecimal priceNew) {
        this.priceNew = priceNew;
    }

    public BigDecimal getStockChange() {
        return stockChange;
    }

    public void setStockChange(BigDecimal stockChange) {
        this.stockChange = stockChange;
    }

    public BigDecimal getChangeAmt() {
        return changeAmt;
    }

    public void setChangeAmt(BigDecimal changeAmt) {
        this.changeAmt = changeAmt;
    }

    public BigDecimal getTradCount() {
        return tradCount;
    }

    public void setTradCount(BigDecimal tradCount) {
        this.tradCount = tradCount;
    }

    public BigDecimal getTradAmt() {
        return tradAmt;
    }

    public void setTradAmt(BigDecimal tradAmt) {
        this.tradAmt = tradAmt;
    }

    public BigDecimal getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(BigDecimal amplitude) {
        this.amplitude = amplitude;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getCloseLast() {
        return closeLast;
    }

    public void setCloseLast(BigDecimal closeLast) {
        this.closeLast = closeLast;
    }

    public Integer getCrawlCount() {
        return crawlCount;
    }

    public void setCrawlCount(Integer crawlCount) {
        this.crawlCount = crawlCount;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", processDate=").append(processDate);
        sb.append(", stockCode=").append(stockCode);
        sb.append(", stockName=").append(stockName);
        sb.append(", priceNew=").append(priceNew);
        sb.append(", stockChange=").append(stockChange);
        sb.append(", changeAmt=").append(changeAmt);
        sb.append(", tradCount=").append(tradCount);
        sb.append(", tradAmt=").append(tradAmt);
        sb.append(", amplitude=").append(amplitude);
        sb.append(", maxPrice=").append(maxPrice);
        sb.append(", minPrice=").append(minPrice);
        sb.append(", openPrice=").append(openPrice);
        sb.append(", closeLast=").append(closeLast);
        sb.append(", crawlCount=").append(crawlCount);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

}