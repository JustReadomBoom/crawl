package com.zqz.dao.service;

import com.zqz.dao.entity.OptRecord;
import com.zqz.dao.mapper.OptRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:31 2020/10/22
 */
@Service
public class OptRecordService {
    @Resource
    private OptRecordMapper mapper;

    public int insert(OptRecord record){
        return mapper.insert(record);
    }

    public int updateByPrimaryKeySelective(OptRecord record){
        return mapper.updateByPrimaryKeySelective(record);
    }

    public OptRecord getRecordByChannelAndDate(String channelType, String processDate) {
        return mapper.getRecordByChannelAndDate(channelType, processDate);
    }

    public int updateStatusByChannelAndDate(String status, String channelType, String processDate) {
        return mapper.updateStatusByChannelAndDate(status, channelType, processDate);
    }
}