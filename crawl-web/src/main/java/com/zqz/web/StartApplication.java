package com.zqz.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 13:35 2020/10/19
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.zqz.dao.mapper"})
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}
