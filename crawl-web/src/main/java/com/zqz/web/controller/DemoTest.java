package com.zqz.web.controller;

import com.zqz.common.model.User;
import com.zqz.common.model.WebResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:12 2020/10/20
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class DemoTest {

    @GetMapping("/demo")
    @ResponseBody
    public Object demo() {
        return "Hello, this is crawl demo!!!";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/get/user")
    @ResponseBody
    public WebResp<User> getData(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit){
        WebResp<User> resp = new WebResp<>();
        resp.setCode(0);
        resp.setMsg("SUCCESS");
        List<User> list = new ArrayList<>();
        User user1 = new User();
        User user2 = new User();

        user1.setId(1);
        user1.setUsername("Join");
        user1.setSex("男");

        user2.setId(2);
        user2.setUsername("Harry");
        user2.setSex("女");

        list.add(user1);
        list.add(user2);

        resp.setData(list);
        return resp;
    }
}
