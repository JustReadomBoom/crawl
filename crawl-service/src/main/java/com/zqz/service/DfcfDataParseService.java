package com.zqz.service;

import com.alibaba.fastjson.JSONArray;
import com.zqz.common.constants.FieldConstant;
import com.zqz.common.enums.StockTimeEnum;
import com.zqz.common.enums.StockTypeEnum;
import com.zqz.common.utils.CommonUtil;
import com.zqz.common.utils.DateUtil;
import com.zqz.common.utils.HttpClientUtils;
import com.zqz.common.utils.HttpUtils;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.service.DfcfRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    public void crawlData(String type) {
        Date now = new Date();
        String nowDate = DateUtil.getDateFormat3Str(now);
        log.info("----> [{}]开始数据抓取类型:[{}]", nowDate, type);

        HttpClientUtils clientUtils = new HttpClientUtils();
        try {
            String formatDay = new SimpleDateFormat(FieldConstant.TIME_FORMAT1).format(new Date());
            //股市类型
            String stockType = StockTypeEnum.getDescByType(type);
            //今日排行
            String dayKey = StockTimeEnum.getDescByType(FieldConstant.DAY_KEY);

            String jsName = FieldConstant.JS_NAME.replace(FieldConstant.JS_DATA, CommonUtil.randomJSCode());
            HttpGet httpGet = createDFCFHttpGet(jsName, dayKey, stockType, 1);
            String result = clientUtils.executeWithResult(httpGet, "utf-8");
            log.info("------> 抓取东方财富-股市[{}]-排行榜[{}]的个股资金流的数据, 返回的结果为[{}]", type, FieldConstant.DAY_KEY, result);
            String[] s1 = result.split("data:");
            String data1 = s1[0];
            int a1 = data1.indexOf("pages:");
            int a2 = data1.indexOf(",date");
            String page = data1.substring(a1 + 6, a2);
            log.info("------> 获取总页数:[{}]", page);
            int pageInt = Integer.valueOf(page);
            if (pageInt > 1) {
                int allPage = pageInt + 1;
                for (int i = 1; i <= allPage; ++i) {
                    HttpGet httpGet2 = createDFCFHttpGet(jsName, dayKey, stockType, i);
                    try {
                        Thread.sleep(2 * 1000L);
                        String result2 = clientUtils.executeWithResult(httpGet2, "utf-8");
                        log.info("-----> 第[{}]页, 返回的结果[{}]", i, result2);
                        String[] dateS2 = result2.split("data:");
                        String dataArr2 = dateS2[1].substring(0, dateS2[1].length() - 1);
                        log.info("-----> 第[{}]页,解析返回数据股票数值为[{}]", i, dataArr2);
                        JSONArray jsonArray = JSONArray.parseArray(dataArr2);
                        int size = jsonArray.size();
                        for (int k = 0; k < size; k++) {
                            // 存储数据
                            insertDfcfData(jsonArray, k, stockType, dayKey, formatDay, i);
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

    private HttpGet createDFCFHttpGet(String jsName, String stValue, String cmdValue, int i) {
        String time = String.valueOf(System.currentTimeMillis() / 30000);
        // 生成随机的js name code
        String code = CommonUtil.randomJSCode();
        String jsNameReal = jsName.replace("{{name.data}}", code);
        Map<String, Object> params = new HashMap<>();
        params.put("type", "ct");
        params.put("st", stValue);
        params.put("sr", "-1");
        params.put("p", String.valueOf(i));
        params.put("ps", "50");
        params.put("js", jsNameReal);
        params.put("token", "894050c76af8597a853f5b408b759f5d");
        params.put("cmd", cmdValue);
        params.put("sty", "DCFFITA");
        params.put("rt", time);
        HttpGet httpGet = HttpUtils.get(FieldConstant.DFCF_FUND_FLOW_URL, params);

        httpGet.addHeader("Accept", "*/*");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate");
        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Host", "nufm.dfcfw.com");
        httpGet.addHeader("Referer", "http://data.eastmoney.com/zjlx/detail.html");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        return httpGet;
    }

    private void insertDfcfData(JSONArray jsonArray, int k, String stockType, String dayKey, String formatDay, int stockPage) throws Exception {
        String infoStr = (String) jsonArray.get(k);
        log.info("-----> 需处理的数据,第[{}]个，数据为[{}]", k, infoStr);
        String[] infoStrings = infoStr.split(",");
        // 清洗数据将 "-" 转化为 "0"
        cleanInfoStringArr(infoStrings);
        String stockCode = infoStrings[1];
        String stockName = infoStrings[2];
        String stockTime = infoStrings[15];
        BigDecimal priceNew = new BigDecimal(infoStrings[3]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal change = new BigDecimal(infoStrings[4]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal mainNetInflowAmount = new BigDecimal(infoStrings[5]).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal mainNetProportion = new BigDecimal(infoStrings[6]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal superBigPartNetInFlowAmount = new BigDecimal(infoStrings[7]).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal superBigPartNetProportion = new BigDecimal(infoStrings[8]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal bigPartNetInFlowAmount = new BigDecimal(infoStrings[9]).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal bigPartNetProportion = new BigDecimal(infoStrings[10]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal middlePartNetInFlowAmount = new BigDecimal(infoStrings[11]).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal middlePartNetProportion = new BigDecimal(infoStrings[12]).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal litterPartNetInFlowAmount = new BigDecimal(infoStrings[13]).setScale(4, BigDecimal.ROUND_HALF_UP);
        BigDecimal litterPartNetProportion = new BigDecimal(infoStrings[14]).setScale(2, BigDecimal.ROUND_HALF_UP);
        Date time = DateUtil.format1.get().parse(infoStrings[15]);
        String timeVersion = stockType + "#" + dayKey + "#" + stockTime;
        String crawlerVersion = stockType + "#" + dayKey + "#" + formatDay;
        BigDecimal someInfo = new BigDecimal(0);
        if (infoStrings[16] != null) {
            someInfo = new BigDecimal(infoStrings[16]).setScale(4, BigDecimal.ROUND_HALF_UP);
        }
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
            record.setCountTime(time);
            record.setStockPage(stockPage);
            record.setSomeinfo(someInfo.toString());
            record.setTimeVersion(timeVersion);
            record.setCrawlerVersion(crawlerVersion);
            record.setCrawlCount(1);
            int a = dfcfRecordService.insert(record);
            log.info("-----> 插入数据[{}]", 1 == a ? "SUCCESS" : "FAIL");
        } else {
            log.info("-----> 该日期:[{}]-股票代码:[{}]已处理, 进行更新", DateUtil.getDateFormat3Str(now), stockCode);
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
            oldRecord.setCountTime(time);
            oldRecord.setStockPage(stockPage);
            oldRecord.setSomeinfo(someInfo.toString());
            oldRecord.setTimeVersion(timeVersion);
            oldRecord.setCrawlerVersion(crawlerVersion);
            oldRecord.setCrawlCount(oldRecord.getCrawlCount() + 1);
            int up = dfcfRecordService.updateByPrimaryKeySelective(oldRecord);
            log.info("-----> 更新结果:[{}]", up);
        }
    }

    private void cleanInfoStringArr(String[] infoStrings) {
        for (int i = 0; i < infoStrings.length; ++i) {
            infoStrings[i] = infoStrings[i].equals("-") ? "0" : infoStrings[i];
        }
    }


}
