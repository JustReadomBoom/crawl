package com.zqz.service.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.zqz.common.enums.ChannelTypeEnum;
import com.zqz.common.enums.OptStatusEnum;
import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.enums.WeekEnum;
import com.zqz.common.utils.DateUtil;
import com.zqz.dao.entity.OptRecord;
import com.zqz.dao.service.OptRecordService;
import com.zqz.service.DfcfDataParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:15 2020/10/20
 */
@Slf4j
@Component
@JobHandler(value = "dfcfGetDataJob")
public class DfcfGetDataJob extends IJobHandler {
    @Autowired
    private DfcfDataParseService dfcfDataParseService;
    @Autowired
    private OptRecordService optRecordService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("===============DfcfGetDataJob Start================");
        Date now = new Date();
        String week = DateUtil.dateToWeek(now);
        if(WeekEnum.SATURDAY.getWeek().equals(week) || WeekEnum.SUNDAY.getWeek().equals(week)){
            log.info("------> 周末未开盘");
            return SUCCESS;
        }
        String nowDate = DateUtil.getDateFormat3Str(now);
        OptRecord optRecord;
        optRecord = optRecordService.getRecordByChannelAndDate(ChannelTypeEnum.DFCF.getType(), nowDate);
        if (null != optRecord) {
            if (OptStatusEnum.PROCESSING.getStatus().equals(optRecord.getStatus())) {
                log.info("----->[{}][{}]正在处理中......", nowDate, ChannelTypeEnum.DFCF.getType());
                return SUCCESS;
            }
            optRecord.setCount(optRecord.getCount() + 1);
            optRecord.setStatus(OptStatusEnum.PROCESSING.getStatus());
            optRecordService.updateByPrimaryKeySelective(optRecord);
        } else {
            optRecord = new OptRecord();
            optRecord.setChannelType(ChannelTypeEnum.DFCF.getType());
            optRecord.setProcessDate(now);
            optRecord.setStatus(OptStatusEnum.PROCESSING.getStatus());
            optRecord.setCount(1);
            optRecordService.insert(optRecord);
        }
        try {
            dfcfDataParseService.crawlMarketData();
            optRecordService.updateStatusByChannelAndDate(OptStatusEnum.SUCCESS.getStatus(), ChannelTypeEnum.DFCF.getType(), nowDate);
            return SUCCESS;
        } catch (Exception e) {
            log.error("***** DfcfGetDataJob执行异常:[{}]", e.getMessage(), e);
            optRecordService.updateStatusByChannelAndDate(OptStatusEnum.ERROR.getStatus(), ChannelTypeEnum.DFCF.getType(), nowDate);
            return FAIL;
        }
    }
}
