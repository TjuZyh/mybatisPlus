package com.zyh.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.mybatisplus.entity.User;
import com.zyh.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MybatisPlusPluginsTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        Page<User> page = new Page<>(2 , 3);
        userMapper.selectPage(page , null);
    }

    @Test
    public void test02(){
        Page<User> page = new Page<>(2 , 3);
        userMapper.selectPage(page , null);
        //获取当前页面的数据
        System.out.println(page.getRecords());
        //获取总页数
        System.out.println(page.getPages());
        //获取总记录数
        System.out.println(page.getTotal());
        //是否有下一页
        System.out.println(page.hasNext());
        //是否有前一页
        System.out.println(page.hasPrevious());
    }
}
