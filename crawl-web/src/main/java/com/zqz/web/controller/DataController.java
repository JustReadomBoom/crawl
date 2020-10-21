package com.zqz.web.controller;

import com.zqz.common.model.GetDfcfDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.service.dfcf.GetDfcfDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 17:52 2020/10/20
 */
@Controller
@RequestMapping("/data")
@Slf4j
public class DataController {
    @Autowired
    private GetDfcfDataService getDfcfDataService;


    @GetMapping("/get/dfcf")
    @ResponseBody
    public WebResp<GetDfcfDataResp> getDfcfData(@RequestParam("page") Integer page,
                                                @RequestParam("limit") Integer limit,
                                                @RequestParam("stockCode") String stockCode,
                                                @RequestParam("processDate")String processDate,
                                                @RequestParam("stockName") String stockName,
                                                @RequestParam("stockMarket") String stockMarket){
        try{
            return getDfcfDataService.doGetDfcfData(page, limit, stockCode, processDate, stockName, stockMarket);
        }catch (Exception e){
            log.error("*****getDfcfData异常:[{}]", e.getMessage(), e);
            WebResp<GetDfcfDataResp> resp = new WebResp<>();
            resp.setCode(9999);
            resp.setMsg("FAIL");
            return resp;
        }
    }
}
