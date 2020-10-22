package com.zqz.dao.mapper;


import com.zqz.dao.entity.OptRecord;
import org.apache.ibatis.annotations.Param;

public interface OptRecordMapper {
    int insert(OptRecord record);

    int updateByPrimaryKeySelective(OptRecord record);

    OptRecord getRecordByChannelAndDate(@Param("channelType") String channelType,
                                        @Param("processDate") String processDate);

    int updateStatusByChannelAndDate(@Param("status") String status,
                                     @Param("channelType") String channelType,
                                     @Param("processDate") String processDate);
}