package com.zqz.service.dfcf;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.model.GetDfcfDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.service.DfcfRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    public WebResp<GetDfcfDataResp> doGetDfcfData(Integer page,
                                                  Integer limit,
                                                  String stockCode,
                                                  String processDate,
                                                  String stockName,
                                                  String stockMarket) {
        WebResp<GetDfcfDataResp> resp = new WebResp<>();
        List<GetDfcfDataResp> list = new ArrayList<>();
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());

        if(StringUtils.isBlank(processDate)){
            resp.setCode(RespCodeEnum.SELECT_DATE.getCode());
            resp.setMsg(RespCodeEnum.SELECT_DATE.getMsg());
            return resp;
        }

        Page<Object> startPage = PageHelper.startPage(page, limit);
        List<DfcfRecord> records = dfcfRecordService.getRecordsByParam(stockCode, processDate, stockName, stockMarket);
        if (records.isEmpty()) {
            resp.setCode(RespCodeEnum.NO_DATA.getCode());
            resp.setMsg(RespCodeEnum.NO_DATA.getMsg());
            return resp;
        }
        int total = (int) startPage.getTotal();
        resp.setCount(total);

        records.forEach(e -> {
            GetDfcfDataResp dr = new GetDfcfDataResp();
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
            dr.setCreateTime(e.getUTime());
            list.add(dr);
        });
        resp.setData(list);
        return resp;
    }
}
