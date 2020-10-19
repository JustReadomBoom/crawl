package com.zqz.service;

import com.zqz.common.enums.StockTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * @Author: zqz
 * @Description: 测试功能入口
 * @Date: Created in 15:08 2020/10/19
 */
@Component
@Slf4j
public class TestCron implements InitializingBean, DisposableBean {
    @Autowired
    private DfcfDataParseService dfcfDataParseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<StockTypeEnum> stockTypeList = initStock();
        stockTypeList.forEach(e -> {
            dfcfDataParseService.crawlData(e.getType());
        });
    }

    @Override
    public void destroy() throws Exception {
        log.info("=============TestCron Destroy=============");
    }

    private List<StockTypeEnum> initStock() {
        StockTypeEnum[] stockTypes = StockTypeEnum.values();
        return Arrays.asList(stockTypes);
    }
}
