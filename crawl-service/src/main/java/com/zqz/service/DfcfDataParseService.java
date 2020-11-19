package com.zqz.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zqz.common.constants.FieldConstant;
import com.zqz.common.enums.StockTimeEnum;
import com.zqz.common.enums.StockTypeEnum;
import com.zqz.common.utils.CommonUtil;
import com.zqz.common.utils.DateUtil;
import com.zqz.common.utils.HttpClientUtils;
import com.zqz.common.utils.HttpUtils;
import com.zqz.dao.entity.DfcfMarket;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.service.DfcfMarketService;
import com.zqz.dao.service.DfcfRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:15 2020/10/19
 */
@Service
@Slf4j
public class DfcfDataParseService {
    @Autowired
    private DfcfRecordService dfcfRecordService;
    @Autowired
    private DfcfMarketService dfcfMarketService;


    public void crawlMarketData() {
        log.info("-------> 开始爬取行情数据");
        HttpClientUtils clientUtils = new HttpClientUtils();
        try {
            HttpGet httpGet = createMarketHttpGet(1);
            String result = clientUtils.executeWithResult(httpGet, "utf-8");
            if (StringUtils.isBlank(result)) {
                log.warn("今日无行情数据");
                return;
            }
            String resultJsonStr = result.substring(result.indexOf("({") + 1, result.length() - 2);
            JSONObject jsonObj = JSON.parseObject(resultJsonStr);
            JSONObject dataObj = jsonObj.getJSONObject("data");
            Integer total = dataObj.getInteger("total");
            log.info("----> 行情数据总记录数:[{}]", total);
            //总页码
            int page = total / 50;
            log.info("----> 行情数据总页数:[{}]", page);

            if (page > 1) {
                int allPage = page + 1;
                for (int i = 1; i <= allPage; i++) {
                    try {
                        HttpGet httpGet2 = createMarketHttpGet(i);
                        Thread.sleep(1000L);
                        String innerResult = clientUtils.executeWithResult(httpGet2, "utf-8");
                        log.info("-----> 第[{}]页, 行情数据返回的结果[{}]", i, innerResult);
                        if (StringUtils.isNotBlank(innerResult)) {
                            //解析数据
                            String jsonStr = innerResult.substring(innerResult.indexOf("({") + 1, innerResult.length() - 2);
                            JSONObject innerJsonObj = JSON.parseObject(jsonStr);
                            JSONObject innerDataObj = innerJsonObj.getJSONObject("data");
                            JSONArray diffArray = innerDataObj.getJSONArray("diff");
                            for (int k = 0; k < diffArray.size(); k++) {
                                // 存储数据
                                JSONObject dataObject = diffArray.getJSONObject(k);
                                addMarketData(dataObject);
                            }
                        }
                    } catch (Exception e) {
                        log.error("***** 行情数据第[{}]页解析数据出现异常:{}", i, e.getMessage(), e);
                        throw new RuntimeException("***** 行情数据解析异常");
                    }
                }
            }

        } catch (Exception e) {
            log.error("抓取东方财富的行情数据出现异常:{}", e.getMessage(), e);
            throw new RuntimeException("*****抓取东方财富的行情数据出现异常");
        } finally {
            clientUtils.close();
        }
    }


    private HttpGet createMarketHttpGet(Integer pageIndex) {
        Map<String, Object> params = new HashMap<>();
        params.put("pn", pageIndex);  //页码
        params.put("pz", "50");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "bd1d9ddb04089700cf9c27f6f7426281");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid", "f3");
        params.put("fs", "m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23");
        params.put("fields", "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152");
        params.put("cb", "jQuery" + CommonUtil.getRandom(true, 21) + "_" + CommonUtil.getRandom(true, 13));    //jQuery + 随机数 + _ +随机数
        params.put("_", CommonUtil.getRandom(true, 13));  //随机数
        HttpGet httpGet = HttpUtils.get("http://66.push2.eastmoney.com/api/qt/clist/get", params);

        httpGet.addHeader("Accept", "*/*");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate");
        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Host", "66.push2.eastmoney.com");
        httpGet.addHeader("Referer", "http://quote.eastmoney.com");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return httpGet;
    }


    private void addMarketData(JSONObject dataObj) {
        Date now = new Date();
        boolean beAdd = false;
        String today = DateUtil.getDateFormat3Str(now);

        //股票代码
        String stockCode = dataObj.getString("f12");
        //股票名字
        String stockName = dataObj.getString("f14");

        //最新价格
        BigDecimal newPrice = parseValue(dataObj, "f15");
        //涨跌幅
        BigDecimal change = parseValue(dataObj, "f3");
        //涨跌额
        BigDecimal changeAmt = parseValue(dataObj, "f4");
        //成交量
        BigDecimal tradCount = parseValue(dataObj, "f5");
        //成交额
        BigDecimal tradAmt = parseValue(dataObj, "f6");
        //振幅
        BigDecimal amplitude = parseValue(dataObj, "f7");
        //最高价
        BigDecimal maxPrice = parseValue(dataObj, "f15");
        //最低价
        BigDecimal minPrice = parseValue(dataObj, "f16");
        //今开价
        BigDecimal openPrice = parseValue(dataObj, "f17");
        //昨收
        BigDecimal closeLast = parseValue(dataObj, "f18");

        DfcfMarket market = dfcfMarketService.getByProcessDateAndStockCode(today, stockCode);
        if (null == market) {
            beAdd = true;
            market = new DfcfMarket();
            market.setStockCode(stockCode);
            market.setProcessDate(now);
            market.setStockName(stockName);
            market.setCrawlCount(1);
        } else {
            market.setCrawlCount(market.getCrawlCount() + 1);
        }
        market.setPriceNew(newPrice);
        market.setStockChange(change);
        market.setChangeAmt(changeAmt);
        market.setTradCount(tradCount);
        market.setTradAmt(tradAmt);
        market.setAmplitude(amplitude);
        market.setMaxPrice(maxPrice);
        market.setMinPrice(minPrice);
        market.setOpenPrice(openPrice);
        market.setCloseLast(closeLast);
        if(beAdd){
            dfcfMarketService.insert(market);
        }else {
            dfcfMarketService.updateById(market);
        }
    }

    private BigDecimal parseValue(JSONObject dataObj, String fieldName){
        return "-".equals(dataObj.getString(fieldName)) ? BigDecimal.ZERO : dataObj.getBigDecimal(fieldName);
    }


    public void crawlData(String type) {
        Date now = new Date();
        String nowDate = DateUtil.getDateFormat3Str(now);
        log.info("----> [{}]开始数据抓取类型:[{}]", nowDate, type);

        HttpClientUtils clientUtils = new HttpClientUtils();
        try {
            //股市类型
            String stockType = StockTypeEnum.getDescByType(type);
            //今日排行
            String dayKey = StockTimeEnum.getDescByType(FieldConstant.DAY_KEY);
            //从第一页首页获取总记录数，计算总页码
            HttpGet httpGet = createDFCFHttpGet(stockType, 1);
            String result = clientUtils.executeWithResult(httpGet, "utf-8");
            log.info("------> 抓取东方财富-股市[{}]-排行榜[{}]的个股资金流的数据, 返回的结果为[{}]", type, FieldConstant.DAY_KEY, result);
            if (StringUtils.isBlank(result)) {
                log.warn("------> stockType[{}]无今日排行数据", type);
                return;
            }

            String resultJsonStr = result.substring(result.indexOf("({") + 1, result.length() - 2);

            JSONObject jsonObj = JSON.parseObject(resultJsonStr);
            JSONObject dataObj = jsonObj.getJSONObject("data");
            Integer total = dataObj.getInteger("total");
            log.info("----> 总记录数:[{}]", total);

            //总页码
            int page = total / 50;
            log.info("----> 总页数:[{}]", page);

            if (page > 1) {
                int allPage = page + 1;
                for (int i = 1; i <= allPage; i++) {
                    try {
                        HttpGet httpGet2 = createDFCFHttpGet(stockType, i);
                        Thread.sleep(1000L);
                        String innerResult = clientUtils.executeWithResult(httpGet2, "utf-8");
                        log.info("-----> 第[{}]页, 返回的结果[{}]", i, innerResult);
                        if (StringUtils.isNotBlank(innerResult)) {
                            //解析数据
                            String jsonStr = innerResult.substring(innerResult.indexOf("({") + 1, innerResult.length() - 2);
                            JSONObject innerJsonObj = JSON.parseObject(jsonStr);
                            JSONObject innerDataObj = innerJsonObj.getJSONObject("data");
                            JSONArray diffArray = innerDataObj.getJSONArray("diff");
                            for (int k = 0; k < diffArray.size(); k++) {
                                // 存储数据
                                JSONObject dataObject = diffArray.getJSONObject(k);
                                insertDfcfData(dataObject, stockType, dayKey, i);
                            }
                        }
                    } catch (Exception e) {
                        log.error("***** 股市[{}]-排行榜[{}]-第[{}]页解析数据出现异常:{}", stockType, FieldConstant.DAY_KEY, i, e.getMessage(), e);
                        throw new RuntimeException("*****解析数据异常");
                    }
                }
            }
        } catch (Exception e) {
            log.error("抓取东方财富的个股资金流的数据出现异常:{}", e.getMessage(), e);
            throw new RuntimeException("*****抓取东方财富的个股资金流的数据出现异常");
        } finally {
            clientUtils.close();
        }
    }

    private HttpGet createDFCFHttpGet(String stockType, Integer pageIndex) {
        Map<String, Object> params = new HashMap<>();
        params.put("pn", pageIndex);  //页码
        params.put("pz", "50");
        params.put("po", "1");
        params.put("np", "1");
        params.put("ut", "b2884a393a59ad64002292a3e90d46a5");
        params.put("fltt", "2");
        params.put("invt", "2");
        params.put("fid0", "f4001");
        params.put("fid", "f62");
        params.put("fs", StockTypeEnum.getUrlParamFSByType(stockType));   //每个板块不同
        params.put("stat", "1");
        params.put("fields", "f12,f14,f2,f3,f62,f184,f66,f69,f72,f75,f78,f81,f84,f87,f204,f205,f124");
        params.put("rt", CommonUtil.getRandom(true, 8));   //随机数
        params.put("cb", "jQuery" + CommonUtil.getRandom(true, 21) + "_" + CommonUtil.getRandom(true, 13));    //jQuery + 随机数 + _ +随机数
        params.put("_", CommonUtil.getRandom(true, 13));  //随机数
        HttpGet httpGet = HttpUtils.get("http://push2.eastmoney.com/api/qt/clist/get", params);

        httpGet.addHeader("Accept", "*/*");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate");
        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Host", "nufm.dfcfw.com");
        httpGet.addHeader("Referer", "http://data.eastmoney.com/zjlx/detail.html");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return httpGet;
    }

    private void insertDfcfData(JSONObject dataObj, String stockType, String dayKey, Integer stockPage) throws Exception {
        //股票代码
        String stockCode = dataObj.getString("f12");
        //股票名字
        String stockName = dataObj.getString("f14");

        //判空处理
        String priceNewStr = dataObj.getString("f2");
        if ("-".equals(priceNewStr)) {
            log.info("[{}][{}]无价格相关数据,过滤此记录", stockCode, stockName);
            return;
        }
        //股票最新价格
        BigDecimal priceNew = dataObj.getBigDecimal("f2");
        //涨跌幅
        BigDecimal change = dataObj.getBigDecimal("f3");
        //主力净额
        BigDecimal mainNetInflowAmount = dataObj.getBigDecimal("f62");
        //主力净流入--净占比
        BigDecimal mainNetProportion = dataObj.getBigDecimal("f184");
        //超大单净额
        BigDecimal superBigPartNetInFlowAmount = dataObj.getBigDecimal("f66");
        //超大单净流入--净占比
        BigDecimal superBigPartNetProportion = dataObj.getBigDecimal("f69");
        //大单净额
        BigDecimal bigPartNetInFlowAmount = dataObj.getBigDecimal("f72");
        //大单净流入--净占比
        BigDecimal bigPartNetProportion = dataObj.getBigDecimal("f75");
        //中单净额
        BigDecimal middlePartNetInFlowAmount = dataObj.getBigDecimal("f78");
        //中单净流入--净占比
        BigDecimal middlePartNetProportion = dataObj.getBigDecimal("f81");
        //小单净额
        BigDecimal litterPartNetInFlowAmount = dataObj.getBigDecimal("f84");
        //小单净流入--净占比
        BigDecimal litterPartNetProportion = dataObj.getBigDecimal("f87");

        Date now = new Date();

        DfcfRecord oldRecord = dfcfRecordService.selectByMarketProDateAndCodeAndTVer(StockTypeEnum.getTypeByDesc(stockType), DateUtil.getDateFormat3Str(now), stockCode);

        if (null == oldRecord) {
            DfcfRecord record = new DfcfRecord();
            record.setStockMarket(null == StockTypeEnum.getTypeByDesc(stockType) ? stockType : StockTypeEnum.getTypeByDesc(stockType));
            record.setStockRank(null == StockTimeEnum.getTypeByDesc(dayKey) ? dayKey : StockTimeEnum.getTypeByDesc(dayKey));
            record.setStockCode(stockCode);
            record.setStockName(stockName);
            record.setPriceNew(priceNew);
            record.setStockChange(change);
            record.setProcessDate(now);
            record.setMainNetInflowAmount(mainNetInflowAmount);
            record.setMainNetProportion(mainNetProportion);
            record.setSuperBigPartNetInflowAmount(superBigPartNetInFlowAmount);
            record.setSuperBigPartNetProportion(superBigPartNetProportion);
            record.setBigPartNetInflowAmount(bigPartNetInFlowAmount);
            record.setBigPartNetProportion(bigPartNetProportion);
            record.setMiddlePartNetInflowAmount(middlePartNetInFlowAmount);
            record.setMiddlePartNetProportion(middlePartNetProportion);
            record.setLitterPartNetInflowAmount(litterPartNetInFlowAmount);
            record.setLitterPartNetProportion(litterPartNetProportion);
            record.setCountTime(now);
            record.setStockPage(stockPage);
            record.setCrawlCount(1);
            int a = dfcfRecordService.insert(record);
            log.info("-----> 插入数据[{}]", 1 == a ? "SUCCESS" : "FAIL");
        } else {
            log.info("-----> 该日期:[{}]-股票类型:[{}]-股票代码:[{}]已处理, 进行更新", DateUtil.getDateFormat3Str(now), StockTypeEnum.getTypeByDesc(stockType), stockCode);
            oldRecord.setStockRank(null == StockTimeEnum.getTypeByDesc(dayKey) ? dayKey : StockTimeEnum.getTypeByDesc(dayKey));
            oldRecord.setStockName(stockName);
            oldRecord.setPriceNew(priceNew);
            oldRecord.setStockChange(change);
            oldRecord.setMainNetInflowAmount(mainNetInflowAmount);
            oldRecord.setMainNetProportion(mainNetProportion);
            oldRecord.setSuperBigPartNetInflowAmount(superBigPartNetInFlowAmount);
            oldRecord.setSuperBigPartNetProportion(superBigPartNetProportion);
            oldRecord.setBigPartNetInflowAmount(bigPartNetInFlowAmount);
            oldRecord.setBigPartNetProportion(bigPartNetProportion);
            oldRecord.setMiddlePartNetInflowAmount(middlePartNetInFlowAmount);
            oldRecord.setMiddlePartNetProportion(middlePartNetProportion);
            oldRecord.setLitterPartNetInflowAmount(litterPartNetInFlowAmount);
            oldRecord.setLitterPartNetProportion(litterPartNetProportion);
            oldRecord.setCountTime(now);
            oldRecord.setStockPage(stockPage);
            oldRecord.setCrawlCount(oldRecord.getCrawlCount() + 1);
            int up = dfcfRecordService.updateByPrimaryKeySelective(oldRecord);
            log.info("-----> 更新结果:[{}]", up);
        }
    }


}
