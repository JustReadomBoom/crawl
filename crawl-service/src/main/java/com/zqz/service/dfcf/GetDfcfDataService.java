package com.zqz.service.dfcf;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zqz.common.model.GetDfcfDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.service.DfcfRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 17:55 2020/10/20
 */
@Service
@Slf4j
public class GetDfcfDataService {

    @Autowired
    private DfcfRecordService dfcfRecordService;

    public WebResp<GetDfcfDataResp> doGetDfcfData(Integer page, Integer limit){
        WebResp<GetDfcfDataResp> resp = new WebResp<>();
        List<GetDfcfDataResp> list = new ArrayList<>();
        resp.setCode(0);
        resp.setMsg("SUCCESS");

        Page<Object> startPage = PageHelper.startPage(page, limit);
        List<DfcfRecord> records = dfcfRecordService.getRecords();
        if(records.isEmpty()){
            resp.setCode(1);
            resp.setMsg("无数据");
            return resp;
        }
        int total = (int) startPage.getTotal();
        resp.setCount(total);

        records.forEach(e ->{
            GetDfcfDataResp dr = new GetDfcfDataResp();
            dr.setId(e.getId());
            dr.setProcessDate(e.getProcessDate());
            dr.setStockRank(e.getStockRank());
            dr.setStockMarket(e.getStockMarket());
            dr.setStockCode(e.getStockCode());
            dr.setStockName(e.getStockName());
            dr.setPriceNew(e.getPriceNew());
            dr.setStockChange(e.getStockChange());
            dr.setMainNetInflowAmount(e.getMainNetInflowAmount());
            dr.setMainNetProportion(e.getMainNetProportion());
            dr.setSuperBigPartNetInflowAmount(e.getSuperBigPartNetInflowAmount());
            dr.setSuperBigPartNetProportion(e.getSuperBigPartNetProportion());
            dr.setBigPartNetInflowAmount(e.getBigPartNetInflowAmount());
            dr.setBigPartNetProportion(e.getBigPartNetProportion());
            dr.setMiddlePartNetInflowAmount(e.getMiddlePartNetInflowAmount());
            dr.setMiddlePartNetProportion(e.getMiddlePartNetProportion());
            dr.setLitterPartNetInflowAmount(e.getLitterPartNetInflowAmount());
            dr.setLitterPartNetProportion(e.getLitterPartNetProportion());
            dr.setCreateTime(e.getCTime());
            list.add(dr);
        });
        resp.setData(list);
        return resp;
    }
}
