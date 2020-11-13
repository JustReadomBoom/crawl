package com.zqz.dao.service;

import com.zqz.dao.entity.User;
import com.zqz.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: zqz
 * @Description:
 * @Date: Created in 16:46 2020/11/13
 */
@Service
public class UserService {
    @Resource
    private UserMapper mapper;

    public User getUserByAccount(String account){
        return mapper.getUserByAccount(account);
    }
}
