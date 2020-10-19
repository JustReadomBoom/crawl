package com.zqz.dao.service;

import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.mapper.DfcfRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
