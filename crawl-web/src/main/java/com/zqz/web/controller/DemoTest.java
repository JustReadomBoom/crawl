package com.zqz.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 10:12 2020/10/20
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class DemoTest {


    @GetMapping("/demo")
    public Object demo(){
        return "Hello, this is crawl demo!!!";
    }
}
