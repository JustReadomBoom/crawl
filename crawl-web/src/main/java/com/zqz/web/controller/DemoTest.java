package com.zqz.web.controller;

import com.alibaba.fastjson.JSON;
import com.zqz.common.model.TestInfoReq;
import com.zqz.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:12 2020/10/20
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class DemoTest {

    private static final String tJson = "{\"contractParam\":{\"debtorMobile02\":\"123456789\",\"signMonth\":\"8\",\"debtorName02\":\"张三\",\"signDay\":\"20\",\"signYear\":\"2020\",\"debtorIdNo02\":\"12345678923456\",\"debtorMail02\":\"KKKK@163.com\",\"debtorAddress02\":\"北京市北京城区东城区1008号\"},\"isSelf\":true,\"modelNo\":1,\"organCode\":\"6566567567577YUGR\",\"organName\":\"这是测试的数据哦\",\"organType\":\"MERGE\",\"posPage\":\"5\",\"posX\":180,\"posY\":405,\"productCode\":\"UJ\",\"requestId\":\"OL20200828766671\",\"signProcessEnum\":\"END\",\"signType\":2,\"sysCode\":\"BBOL\",\"uploadType\":1}";


    @PostMapping("/commit")
    public String testReqCommit(@RequestBody TestInfoReq req){
        log.info("Req:[{}]", JSON.toJSONString(req));
        return CommonUtil.getRandom(true, 10)+ tJson;
    }

}
