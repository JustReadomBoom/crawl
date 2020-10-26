package com.zqz.service;

import com.zqz.common.enums.ChannelTypeEnum;
import com.zqz.common.enums.OptStatusEnum;
import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.enums.StockTypeEnum;
import com.zqz.common.model.GetBrokenDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.common.utils.DateUtil;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.entity.OptRecord;
import com.zqz.dao.service.DfcfRecordService;
import com.zqz.dao.service.OptRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 13:47 2020/10/22
 */
@Service
@Slf4j
public class BusDataService {
    @Autowired
    private OptRecordService optRecordService;
    @Autowired
    private DfcfDataParseService dfcfDataParseService;
    @Autowired
    private DfcfRecordService dfcfRecordService;
    private ExecutorService executor = Executors.newFixedThreadPool(3);

    /**
     * @Author: zqz
     * @Description: 主动触发
     * @Param: []
     * @Return: com.zqz.common.model.WebResp
     * @Date: 14:05 2020/10/22
     */
    public WebResp doActiveCrawl() {
        WebResp resp = new WebResp<>();
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());
        Date now = new Date();
        String nowDate = DateUtil.getDateFormat3Str(now);
        log.info("-----> 开始手动触发日期:[{}]的数据爬取", nowDate);
        OptRecord optRecord;
        optRecord = optRecordService.getRecordByChannelAndDate(ChannelTypeEnum.DFCF.getType(), nowDate);
        if (null != optRecord) {
            if (OptStatusEnum.PROCESSING.getStatus().equals(optRecord.getStatus())) {
                log.info("----->[{}][{}]正在处理中......", nowDate, ChannelTypeEnum.DFCF.getType());
                resp.setCode(RespCodeEnum.PROCESSING.getCode());
                resp.setMsg(RespCodeEnum.PROCESSING.getMsg());
                return resp;
            }
            //最近一次更新时间和当前时间比对
            Integer compareHour = DateUtil.compareHour(optRecord.getuTime(), now);
            log.info("---->当前时间和上次更新时间相差:[{}]小时", compareHour);
            if (OptStatusEnum.SUCCESS.getStatus().equals(optRecord.getStatus()) && 3 > compareHour) {
                resp.setCode(RespCodeEnum.STOP_DO.getCode());
                resp.setMsg(RespCodeEnum.STOP_DO.getMsg());
                return resp;
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
        //异步触发任务
        executor.submit(new Runnable() {
            @Override
            public void run() {
                doTask(nowDate);
            }
        });
        return resp;
    }

    /**
     * @Author: zqz
     * @Description: 触发爬取任务
     * @Param: [nowDate]
     * @Return: void
     * @Date: 14:04 2020/10/22
     */
    private void doTask(String nowDate) {
        List<StockTypeEnum> stockTypes = initStock();
        try {
            stockTypes.forEach(e -> {
                dfcfDataParseService.crawlData(e.getType());
            });
            optRecordService.updateStatusByChannelAndDate(OptStatusEnum.SUCCESS.getStatus(), ChannelTypeEnum.DFCF.getType(), nowDate);
        } catch (Exception e) {
            log.error("*****爬取数据异常:[{}]", e.getMessage(), e);
            optRecordService.updateStatusByChannelAndDate(OptStatusEnum.ERROR.getStatus(), ChannelTypeEnum.DFCF.getType(), nowDate);
        }
    }


    private List<StockTypeEnum> initStock() {
        StockTypeEnum[] stockTypes = StockTypeEnum.values();
        return Arrays.asList(stockTypes);
    }


    public WebResp<GetBrokenDataResp> doGetBrokenData(String stockCode, String stockMarket) {
        WebResp<GetBrokenDataResp> resp = new WebResp<>();
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());

        List<GetBrokenDataResp> list = new ArrayList<>();

        List<DfcfRecord> records = dfcfRecordService.getLastDaysData(stockCode, stockMarket);
        if (records.isEmpty()) {
            resp.setCode(RespCodeEnum.NO_DATA.getCode());
            resp.setMsg(RespCodeEnum.NO_DATA.getMsg());
            return resp;
        }
        records.forEach(r -> {
            GetBrokenDataResp dataResp = new GetBrokenDataResp();
            dataResp.setProcessDate(r.getProcessDate());
            dataResp.setCurrentPrice(r.getPriceNew());
            list.add(dataResp);
        });

        resp.setData(list);
        return resp;
    }
}
