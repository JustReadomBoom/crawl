package com.zqz.web.controller;

import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.model.GetBrokenDataResp;
import com.zqz.common.model.GetDfcfDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.common.utils.CommonUtil;
import com.zqz.common.utils.RedisClient;
import com.zqz.dao.entity.User;
import com.zqz.dao.service.UserService;
import com.zqz.service.BusDataService;
import com.zqz.service.dfcf.GetDfcfDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private BusDataService busDataService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisClient redisClient;


    @GetMapping("/get/dfcf")
    @ResponseBody
    public WebResp<GetDfcfDataResp> getDfcfData(@RequestParam("page") Integer page,
                                                @RequestParam("limit") Integer limit,
                                                @RequestParam("stockCode") String stockCode,
                                                @RequestParam("processDate") String processDate,
                                                @RequestParam("stockName") String stockName,
                                                @RequestParam("stockMarket") String stockMarket,
                                                HttpServletRequest request) {
        try {
            String authToken = request.getHeader("AuthToken");
            log.info("Header-Token={}", authToken);
            if(authToken.equals("null") || StringUtils.isBlank(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，<a href=\"https://www.zhouqz.top/zqz/login\">点我登录</a>");
                return resp;
            }
            String redisToken = redisClient.get("AuthToken");
            log.info("Redis-Token:[{}]", redisToken);
            if(StringUtils.isBlank(redisToken) || !redisToken.equals(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，<a href=\"https://www.zhouqz.top/zqz/login\">点我登录</a>");
                return resp;
            }
            return getDfcfDataService.doGetDfcfData(page, limit, stockCode, processDate, stockName, stockMarket);
        } catch (Exception e) {
            log.error("*****getDfcfData异常:[{}]", e.getMessage(), e);
            WebResp<GetDfcfDataResp> resp = new WebResp<>();
            resp.setCode(RespCodeEnum.ERROR.getCode());
            resp.setMsg(RespCodeEnum.ERROR.getMsg());
            return resp;
        }
    }

    @GetMapping("/active/crawl")
    @ResponseBody
    public WebResp activeCrawl(HttpServletRequest request) {
        try {
            String authToken = request.getHeader("AuthToken");
            log.info("Header-Token={}", authToken);
            if(authToken.equals("null") || StringUtils.isBlank(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，<a href=\"https://www.zhouqz.top/zqz/login\">点我登录</a>");
                return resp;
            }
            String redisToken = redisClient.get("AuthToken");
            log.info("Redis-Token:[{}]", redisToken);
            if(StringUtils.isBlank(redisToken) || !redisToken.equals(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，<a href=\"https://www.zhouqz.top/zqz/login\">点我登录</a>");
                return resp;
            }
            return busDataService.doActiveCrawl();
        } catch (Exception e) {
            log.error("*****activeCrawl异常:[{}]", e.getMessage(), e);
            WebResp resp = new WebResp<>();
            resp.setCode(RespCodeEnum.ERROR.getCode());
            resp.setMsg(RespCodeEnum.ERROR.getMsg());
            return resp;
        }
    }

    @GetMapping("/get/broken/data")
    @ResponseBody
    public WebResp<GetBrokenDataResp> getBrokenData(@RequestParam("stockCode") String stockCode,
                                                    @RequestParam("stockMarket") String stockMarket) {
        try {
            return busDataService.doGetBrokenData(stockCode, stockMarket);
        } catch (Exception e) {
            log.error("*****getBrokenData异常:[{}]", e.getMessage(), e);
            WebResp resp = new WebResp<>();
            resp.setCode(RespCodeEnum.ERROR.getCode());
            resp.setMsg(RespCodeEnum.ERROR.getMsg());
            return resp;
        }
    }


    @GetMapping("/login")
    @ResponseBody
    public WebResp toLogin(@RequestParam("account") String account,
                                      @RequestParam("pwd") String pwd) {
        log.info("登录， account={}, pwd={}", account, pwd);
        WebResp resp = new WebResp<>();
        User user = userService.getUserByAccount(account);
        if(null == user){
            resp.setCode(RespCodeEnum.NO_USER.getCode());
            resp.setMsg(RespCodeEnum.NO_USER.getMsg());
            return resp;
        }
        String userPwd = user.getPwd();
        String md5Pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        log.info("MD5加密后:[{}]", md5Pwd);
        if(!md5Pwd.equals(userPwd)){
            resp.setCode(RespCodeEnum.ERROR_PWD.getCode());
            resp.setMsg(RespCodeEnum.ERROR_PWD.getMsg());
            return resp;
        }
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());
        //生成token
        String token = CommonUtil.getRandomString(32);
        //存入redis
        redisClient.setAndExpire("AuthToken", token, 300, TimeUnit.SECONDS);
        resp.setAuthToken(token);
        return resp;
    }
}
