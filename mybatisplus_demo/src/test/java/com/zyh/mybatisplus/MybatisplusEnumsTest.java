package com.zyh.mybatisplus;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.zyh.mybatisplus.entity.User;
import com.zyh.mybatisplus.enums.SexEnum;
import com.zyh.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MybatisplusEnumsTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        User user = new User();
        user.setName("fy11");
        user.setAge(20);
        user.setEmail("fy2389.com");
        user.setSex(SexEnum.FEMALE);
        //INSERT INTO user ( uid, sex, name, age, email ) VALUES ( ?, ?, ?, ?, ? )
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

}
