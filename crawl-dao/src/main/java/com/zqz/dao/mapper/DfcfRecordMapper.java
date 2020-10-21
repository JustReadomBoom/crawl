package com.zqz.dao.mapper;


import com.zqz.dao.entity.DfcfRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DfcfRecordMapper {

    int insert(DfcfRecord record);

    List<DfcfRecord> selectByTimeVersion(@Param("stockCode") String stockCode,
                                         @Param("timeVersion") String timeVersion);

    int updateByPrimaryKeySelective(DfcfRecord record);

    List<DfcfRecord> getRecordByProcessDate(@Param("date") Date date);

    List<DfcfRecord> getRecordsByParam(@Param("stockCode") String stockCode,
                                       @Param("processDate") String processDate,
                                       @Param("stockName") String stockName);

}