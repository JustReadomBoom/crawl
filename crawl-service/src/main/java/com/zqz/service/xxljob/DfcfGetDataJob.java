package com.zqz.service.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.zqz.common.enums.StockTypeEnum;
import com.zqz.service.DfcfDataParseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:15 2020/10/20
 */
@Slf4j
@JobHandler(value = "dfcfGetDataJob")
@Component
public class DfcfGetDataJob extends IJobHandler {
    @Autowired
    private DfcfDataParseService dfcfDataParseService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("===============DfcfGetDataJob Start================");
        try {
            List<StockTypeEnum> stockTypeList = initStock();
            stockTypeList.forEach(e -> {
                dfcfDataParseService.crawlData(e.getType());
            });
            return SUCCESS;
        }catch (Exception e){
            log.error("***** DfcfGetDataJob执行异常:[{}]", e.getMessage(), e);
            return FAIL;
        }
    }


    private List<StockTypeEnum> initStock() {
        StockTypeEnum[] stockTypes = StockTypeEnum.values();
        return Arrays.asList(stockTypes);
    }
}
