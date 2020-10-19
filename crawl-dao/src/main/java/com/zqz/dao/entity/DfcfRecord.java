package com.zqz.dao.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DfcfRecord implements Serializable {
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

    private Date createTime;

    private Date updateTime;

    private String someinfo;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(String stockMarket) {
        this.stockMarket = stockMarket == null ? null : stockMarket.trim();
    }

    public String getStockRank() {
        return stockRank;
    }

    public void setStockRank(String stockRank) {
        this.stockRank = stockRank == null ? null : stockRank.trim();
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

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public BigDecimal getMainNetInflowAmount() {
        return mainNetInflowAmount;
    }

    public void setMainNetInflowAmount(BigDecimal mainNetInflowAmount) {
        this.mainNetInflowAmount = mainNetInflowAmount;
    }

    public BigDecimal getMainNetProportion() {
        return mainNetProportion;
    }

    public void setMainNetProportion(BigDecimal mainNetProportion) {
        this.mainNetProportion = mainNetProportion;
    }

    public BigDecimal getSuperBigPartNetInflowAmount() {
        return superBigPartNetInflowAmount;
    }

    public void setSuperBigPartNetInflowAmount(BigDecimal superBigPartNetInflowAmount) {
        this.superBigPartNetInflowAmount = superBigPartNetInflowAmount;
    }

    public BigDecimal getSuperBigPartNetProportion() {
        return superBigPartNetProportion;
    }

    public void setSuperBigPartNetProportion(BigDecimal superBigPartNetProportion) {
        this.superBigPartNetProportion = superBigPartNetProportion;
    }

    public BigDecimal getBigPartNetInflowAmount() {
        return bigPartNetInflowAmount;
    }

    public void setBigPartNetInflowAmount(BigDecimal bigPartNetInflowAmount) {
        this.bigPartNetInflowAmount = bigPartNetInflowAmount;
    }

    public BigDecimal getBigPartNetProportion() {
        return bigPartNetProportion;
    }

    public void setBigPartNetProportion(BigDecimal bigPartNetProportion) {
        this.bigPartNetProportion = bigPartNetProportion;
    }

    public BigDecimal getMiddlePartNetInflowAmount() {
        return middlePartNetInflowAmount;
    }

    public void setMiddlePartNetInflowAmount(BigDecimal middlePartNetInflowAmount) {
        this.middlePartNetInflowAmount = middlePartNetInflowAmount;
    }

    public BigDecimal getMiddlePartNetProportion() {
        return middlePartNetProportion;
    }

    public void setMiddlePartNetProportion(BigDecimal middlePartNetProportion) {
        this.middlePartNetProportion = middlePartNetProportion;
    }

    public BigDecimal getLitterPartNetInflowAmount() {
        return litterPartNetInflowAmount;
    }

    public void setLitterPartNetInflowAmount(BigDecimal litterPartNetInflowAmount) {
        this.litterPartNetInflowAmount = litterPartNetInflowAmount;
    }

    public BigDecimal getLitterPartNetProportion() {
        return litterPartNetProportion;
    }

    public void setLitterPartNetProportion(BigDecimal litterPartNetProportion) {
        this.litterPartNetProportion = litterPartNetProportion;
    }

    public Integer getStockPage() {
        return stockPage;
    }

    public void setStockPage(Integer stockPage) {
        this.stockPage = stockPage;
    }

    public Date getCountTime() {
        return countTime;
    }

    public void setCountTime(Date countTime) {
        this.countTime = countTime;
    }

    public String getTimeVersion() {
        return timeVersion;
    }

    public void setTimeVersion(String timeVersion) {
        this.timeVersion = timeVersion == null ? null : timeVersion.trim();
    }

    public String getCrawlerVersion() {
        return crawlerVersion;
    }

    public void setCrawlerVersion(String crawlerVersion) {
        this.crawlerVersion = crawlerVersion == null ? null : crawlerVersion.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSomeinfo() {
        return someinfo;
    }

    public void setSomeinfo(String someinfo) {
        this.someinfo = someinfo == null ? null : someinfo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", stockMarket=").append(stockMarket);
        sb.append(", stockRank=").append(stockRank);
        sb.append(", stockCode=").append(stockCode);
        sb.append(", stockName=").append(stockName);
        sb.append(", priceNew=").append(priceNew);
        sb.append(", stockChange=").append(stockChange);
        sb.append(", processDate=").append(processDate);
        sb.append(", mainNetInflowAmount=").append(mainNetInflowAmount);
        sb.append(", mainNetProportion=").append(mainNetProportion);
        sb.append(", superBigPartNetInflowAmount=").append(superBigPartNetInflowAmount);
        sb.append(", superBigPartNetProportion=").append(superBigPartNetProportion);
        sb.append(", bigPartNetInflowAmount=").append(bigPartNetInflowAmount);
        sb.append(", bigPartNetProportion=").append(bigPartNetProportion);
        sb.append(", middlePartNetInflowAmount=").append(middlePartNetInflowAmount);
        sb.append(", middlePartNetProportion=").append(middlePartNetProportion);
        sb.append(", litterPartNetInflowAmount=").append(litterPartNetInflowAmount);
        sb.append(", litterPartNetProportion=").append(litterPartNetProportion);
        sb.append(", stockPage=").append(stockPage);
        sb.append(", countTime=").append(countTime);
        sb.append(", timeVersion=").append(timeVersion);
        sb.append(", crawlerVersion=").append(crawlerVersion);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", someinfo=").append(someinfo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}