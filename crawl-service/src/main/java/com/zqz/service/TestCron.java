package com.zqz.service;

import com.zqz.common.enums.StockTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 15:08 2020/10/19
 */
@Component
@Slf4j
public class TestCron implements InitializingBean, DisposableBean {
    @Autowired
    private DfcfDataParseService dfcfDataParseService;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    @Override
    public void afterPropertiesSet() throws Exception {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                dfcfDataParseService.crawlData(StockTypeEnum.SH_SZ_A.getType());
            }
        }, 1, 10, TimeUnit.SECONDS);
    }

    @Override
    public void destroy() throws Exception {
        executor.shutdown();
    }
}
