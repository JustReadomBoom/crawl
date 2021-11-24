package com.zqz.web.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zqz.common.enums.RespCodeEnum;
import com.zqz.common.model.GetBrokenDataResp;
import com.zqz.common.model.GetMarketDataResp;
import com.zqz.common.model.WebResp;
import com.zqz.common.utils.CommonUtil;
import com.zqz.common.utils.RedisClient;
import com.zqz.dao.entity.DbMovies;
import com.zqz.dao.entity.User;
import com.zqz.dao.service.DbMoviesService;
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
import java.util.List;
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
    @Autowired
    private DbMoviesService dbMoviesService;
    private static final String HREF = "<a href=\"http://39.98.218.231:9518/zqz/login\">点我登录</a>";
//    private static final String HREF = "<a href=\"http://localhost:9518/zqz/login\">点我登录</a>";


    @GetMapping("/get/dfcf")
    @ResponseBody
    public WebResp<GetMarketDataResp> getDfcfMarketData(@RequestParam("page") Integer page,
                                                        @RequestParam("limit") Integer limit,
                                                        @RequestParam("stockCode") String stockCode,
                                                        @RequestParam("processDate") String processDate,
                                                        @RequestParam("stockName") String stockName,
                                                        HttpServletRequest request) {
        try {
            String authToken = request.getHeader("AuthToken");
            log.info("Header-Token={}", authToken);
            if(authToken.equals("null") || StringUtils.isBlank(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，" + HREF);
                return resp;
            }
            String redisToken = redisClient.get("AuthToken");
            log.info("Redis-Token:[{}]", redisToken);
            if(StringUtils.isBlank(redisToken) || !redisToken.equals(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，" + HREF);
                return resp;
            }
            return getDfcfDataService.doGetDfcfMarketData(page, limit, stockCode, processDate, stockName);
        } catch (Exception e) {
            log.error("*****getDfcfData异常:[{}]", e.getMessage(), e);
            WebResp<GetMarketDataResp> resp = new WebResp<>();
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
                resp.setMsg("未登录，" + HREF);
                return resp;
            }
            String redisToken = redisClient.get("AuthToken");
            log.info("Redis-Token:[{}]", redisToken);
            if(StringUtils.isBlank(redisToken) || !redisToken.equals(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，" + HREF);
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
                                                    HttpServletRequest request) {
        try {
            String authToken = request.getHeader("AuthToken");
            log.info("Header-Token={}", authToken);
            if(authToken.equals("null") || StringUtils.isBlank(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，" + HREF);
                return resp;
            }
            String redisToken = redisClient.get("AuthToken");
            log.info("Redis-Token:[{}]", redisToken);
            if(StringUtils.isBlank(redisToken) || !redisToken.equals(authToken)){
                WebResp resp = new WebResp<>();
                resp.setCode(1000);
                resp.setMsg("未登录，" + HREF);
                return resp;
            }
            return busDataService.doGetBrokenData(stockCode);
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
        redisClient.setAndExpire("AuthToken", token, 600, TimeUnit.SECONDS);
        resp.setAuthToken(token);
        return resp;
    }

    @GetMapping("/get/movies")
    @ResponseBody
    public WebResp<DbMovies> getMoviesData(@RequestParam("page") Integer page,
                                           @RequestParam("limit") Integer limit,
                                           @RequestParam("title") String title,
                                           @RequestParam("casts") String casts){

        WebResp<DbMovies> resp = new WebResp<>();
        resp.setCode(RespCodeEnum.SUCCESS.getCode());
        resp.setMsg(RespCodeEnum.SUCCESS.getMsg());
        Page<Object> startPage = PageHelper.startPage(page, limit);
        List<DbMovies> movies = dbMoviesService.selectByParam(title, casts);
        if(movies.isEmpty()){
            resp.setCode(RespCodeEnum.NO_DATA.getCode());
            resp.setMsg(RespCodeEnum.NO_DATA.getMsg());
            return resp;
        }
        int total = (int) startPage.getTotal();
        resp.setCount(total);

        resp.setData(movies);

        return resp;
    }
}
