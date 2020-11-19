package com.zqz.dao.service;

import com.zqz.dao.entity.DfcfMarket;
import com.zqz.dao.mapper.DfcfMarketMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 14:26 2020/11/18
 */
@Service
public class DfcfMarketService {
    @Resource
    private DfcfMarketMapper mapper;

    public int insert(DfcfMarket dfcfMarket){
        return mapper.insert(dfcfMarket);
    }

    public DfcfMarket getByProcessDateAndStockCode(String today, String stockCode) {
        return mapper.getByProcessDateAndStockCode(today, stockCode);
    }

    public int updateById(DfcfMarket market) {
        return mapper.updateById(market);
    }

    public List<DfcfMarket> getByParam(String stockCode, String processDate, String stockName) {
        return mapper.getByParam(stockCode, processDate, stockName);
    }

    public List<DfcfMarket> getLastDaysData(String stockCode) {
        return mapper.getLastDaysData(stockCode);
    }
}
