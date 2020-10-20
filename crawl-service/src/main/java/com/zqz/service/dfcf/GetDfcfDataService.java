package com.zqz.service.dfcf;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zqz.common.model.WebResp;
import com.zqz.dao.entity.DfcfRecord;
import com.zqz.dao.service.DfcfRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public WebResp<DfcfRecord> doGetDfcfData(Integer page, Integer limit){
        WebResp<DfcfRecord> resp = new WebResp<>();
        resp.setCode(0);
        resp.setMsg("SUCCESS");

        Page<Object> startPage = PageHelper.startPage(page, limit);
        List<DfcfRecord> records = dfcfRecordService.getRecords();
        if(records.isEmpty()){
            resp.setCode(1);
            resp.setMsg("无数据");
            return resp;
        }
        int pages = startPage.getPages();
        resp.setData(records);
        resp.setCount(pages);
        return resp;
    }


}
