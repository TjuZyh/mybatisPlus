package com.zyh.mybatisplus.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zyh.mybatisplus.entity.User;
import com.zyh.mybatisplus.mapper.UserMapper;
import com.zyh.mybatisplus.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService{

}
