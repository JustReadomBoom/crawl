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
import com.zqz.dao.entity.DfcfRecord;
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
        if("-".equals(priceNewStr)){
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
