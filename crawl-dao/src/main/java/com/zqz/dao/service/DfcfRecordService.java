package com.zqz.dao.service;

import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.mapper.DfcfRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 13:47 2020/10/19
 */
@Service
public class DfcfRecordService {
    @Resource
    private DfcfRecordMapper mapper;

    public int insert(DfcfRecord record){
        return mapper.insert(record);
    }

    public int updateByPrimaryKeySelective(DfcfRecord record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    public List<DfcfRecord> getRecordsByParam(String stockCode, String processDate, String stockName, String stockMarket){
        return mapper.getRecordsByParam(stockCode, processDate, stockName, stockMarket);
    }

    public DfcfRecord selectByMarketProDateAndCodeAndTVer(String stockMarket, String processDate, String stockCode) {
        return mapper.selectByProDateAndCodeAndTVer(stockMarket, processDate, stockCode);
    }

    public List<DfcfRecord> getLastDaysData(String stockCode, String stockMarket) {
        return mapper.getLastDaysData(stockCode, stockMarket);
    }
}
