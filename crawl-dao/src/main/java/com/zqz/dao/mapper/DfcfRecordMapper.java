package com.zqz.dao.mapper;


import com.zqz.dao.entity.DfcfRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DfcfRecordMapper {

    int insert(DfcfRecord record);

    int updateByPrimaryKeySelective(DfcfRecord record);

    List<DfcfRecord> getRecordsByParam(@Param("stockCode") String stockCode,
                                       @Param("processDate") String processDate,
                                       @Param("stockName") String stockName,
                                       @Param("stockMarket") String stockMarket);

    DfcfRecord selectByProDateAndCodeAndTVer(@Param("stockMarket") String stockMarket,
                                             @Param("processDate") String processDate,
                                             @Param("stockCode") String stockCode);
}