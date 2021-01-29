package com.zqz.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 15:56 2020/10/21
 */
@Controller
@RequestMapping("/zqz")
@Slf4j
public class PageController {
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/snake")
    public String toSnake(){
        return "snake";
    }

}
