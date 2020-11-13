package com.zqz.dao.mapper;


import com.zqz.dao.entity.User;

public interface UserMapper {

    User getUserByAccount(String account);
}