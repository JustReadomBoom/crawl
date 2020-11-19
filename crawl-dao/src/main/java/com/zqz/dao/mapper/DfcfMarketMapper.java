package com.zqz.dao.mapper;

import com.zqz.dao.entity.DfcfMarket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DfcfMarketMapper {

    int insert(DfcfMarket record);

    DfcfMarket getByProcessDateAndStockCode(@Param("today") String today, @Param("stockCode") String stockCode);

    int updateById(DfcfMarket market);

    List<DfcfMarket> getByParam(@Param("stockCode") String stockCode,
                                @Param("processDate") String processDate,
                                @Param("stockName") String stockName);

    List<DfcfMarket> getLastDaysData(@Param("stockCode") String stockCode);
}