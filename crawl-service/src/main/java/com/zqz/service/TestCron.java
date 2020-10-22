//package com.zqz.service;
//
//import com.zqz.common.enums.ChannelTypeEnum;
//import com.zqz.common.enums.OptStatusEnum;
//import com.zqz.common.enums.StockTypeEnum;
//import com.zqz.common.utils.DateUtil;
//import com.zqz.dao.entity.OptRecord;
//import com.zqz.dao.service.OptRecordService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//
///**
// * @Author: zqz
// * @Description: 测试功能入口
// * @Date: Created in 15:08 2020/10/19
// */
//@Component
//@Slf4j
//public class TestCron implements InitializingBean, DisposableBean {
//    @Autowired
//    private DfcfDataParseService dfcfDataParseService;
//    @Autowired
//    private OptRecordService optRecordService;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Date now = new Date();
//        String nowDate = DateUtil.getDateFormat3Str(now);
//        OptRecord optRecord;
//        optRecord = optRecordService.getRecordByChannelAndDate(ChannelTypeEnum.DFCF.getType(), nowDate);
//        if(null != optRecord){
//            if(OptStatusEnum.PROCESSING.getStatus().equals(optRecord.getStatus())){
//                log.info("----->[{}][{}]正在处理中......", nowDate, ChannelTypeEnum.DFCF.getType());
//                return;
//            }
//            optRecord.setCount(optRecord.getCount() + 1);
//            optRecord.setStatus(OptStatusEnum.PROCESSING.getStatus());
//            optRecordService.updateByPrimaryKeySelective(optRecord);
//        } else {
//            optRecord = new OptRecord();
//            optRecord.setChannelType(ChannelTypeEnum.DFCF.getType());
//            optRecord.setProcessDate(now);
//            optRecord.setStatus(OptStatusEnum.PROCESSING.getStatus());
//            optRecord.setCount(1);
//            optRecordService.insert(optRecord);
//        }
//        List<StockTypeEnum> stockTypeList = initStock();
//
//        stockTypeList.forEach(e -> {
//            dfcfDataParseService.crawlData(e.getType());
//        });
//
//        optRecordService.updateStatusByChannelAndDate(OptStatusEnum.SUCCESS.getStatus(), ChannelTypeEnum.DFCF.getType(), nowDate);
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        log.info("=============TestCron Destroy=============");
//    }
//
//    private List<StockTypeEnum> initStock() {
//        StockTypeEnum[] stockTypes = StockTypeEnum.values();
//        return Arrays.asList(stockTypes);
//    }
//}
