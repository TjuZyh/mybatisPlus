package com.zyh.mybatisplus;

import com.zyh.mybatisplus.entity.User;
import com.zyh.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void selectAll(){
        userMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    public void addUser(){
        User user = new User();
        user.setAge(22);
        user.setName("Herry");
        user.setEmail("tju_zhaoyihan@163.com");
        //返回值为添加的行数
        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println("id : " + user.getId());
    }

    @Test
    public void deleteTest(){
        //通过ID进行删除
        /*int result = userMapper.deleteById(1505189815698628610L);
        System.out.println("result: " + result);*/

        //根据map中所设置的条件进项删除用户信息
        Map<String , Object> map= new HashMap<>();
        map.put("name" , "Herry");
        map.put("age" , 22);
        userMapper.deleteByMap(map);

        //批量删除
        /*List<Long> list = Arrays.asList(1L , 2L , 3L);
        userMapper.deleteBatchIds(list);*/
    }

    @Test
    public void updateTest(){
        User user = new User();
        user.setId(1L);
        user.setAge(40);
        //通过ID进行更新
        userMapper.updateById(user);
    }

    @Test
    public void selectTest(){
        Map<String, Object> map = userMapper.selectMapById(1L);
        System.out.println(map);
    }
}
