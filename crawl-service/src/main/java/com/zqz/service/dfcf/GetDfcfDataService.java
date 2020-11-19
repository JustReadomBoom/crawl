package com.zqz.service.dfcf;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.model.GetMarketDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.common.utils.CommonUtil;
import com.zqz.dao.entity.DfcfMarket;
import com.zqz.dao.service.DfcfMarketService;
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
    private DfcfMarketService dfcfMarketService;

    public WebResp<GetMarketDataResp> doGetDfcfMarketData(Integer page,
                                                          Integer limit,
                                                          String stockCode,
                                                          String processDate,
                                                          String stockName) {
        WebResp<GetMarketDataResp> resp = new WebResp<>();
        List<GetMarketDataResp> list = new ArrayList<>();
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());

        if (StringUtils.isBlank(processDate)) {
            resp.setCode(RespCodeEnum.SELECT_DATE.getCode());
            resp.setMsg(RespCodeEnum.SELECT_DATE.getMsg());
            return resp;
        }

        Page<Object> startPage = PageHelper.startPage(page, limit);

        List<DfcfMarket> markets = dfcfMarketService.getByParam(stockCode, processDate, stockName);
        if (markets.isEmpty()) {
            resp.setCode(RespCodeEnum.NO_DATA.getCode());
            resp.setMsg(RespCodeEnum.NO_DATA.getMsg());
            return resp;
        }
        int total = (int) startPage.getTotal();
        resp.setCount(total);

        markets.forEach(m -> {
            GetMarketDataResp dr = new GetMarketDataResp();
            dr.setProcessDate(m.getProcessDate());
            dr.setStockCode(m.getStockCode());
            dr.setStockName(m.getStockName());
            dr.setPriceNew(m.getPriceNew());
            dr.setStockChange(m.getStockChange());
            dr.setChangeAmt(m.getChangeAmt());
            dr.setTradCount(CommonUtil.formatNum(m.getTradCount()));
            dr.setTradAmt(CommonUtil.formatNum(m.getTradAmt()));
            dr.setAmplitude(m.getAmplitude() + "%");
            dr.setMaxPrice(m.getMaxPrice());
            dr.setMinPrice(m.getMinPrice());
            dr.setOpenPrice(m.getOpenPrice());
            dr.setCloseLast(m.getCloseLast());
            dr.setCrawlCount(m.getCrawlCount());
            dr.setUTime(m.getuTime());
            list.add(dr);
        });
        resp.setData(list);
        return resp;
    }
}
