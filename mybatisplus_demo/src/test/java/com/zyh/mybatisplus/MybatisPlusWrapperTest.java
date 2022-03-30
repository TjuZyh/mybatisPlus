package com.zyh.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zyh.mybatisplus.entity.User;
import com.zyh.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class MybatisPlusWrapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test01(){
        //查询用户名包含a，年龄在20-30之间，邮箱信息不为null的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name" , "a")
                .between("age" , 20 , 30)
                .isNotNull("email");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test02(){
        //查询用户信息，按照年龄的降序排列；若年龄相同，则按照id升序排列
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                .orderByAsc("uid");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test04(){
        //将（年龄大于20并且用户名中含有a）或邮箱为null的用户信息修改
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("age" , 20)
                .like("name" , "a")
                .or()
                .isNull("email");
        User user = new User();
        user.setEmail("test04.com");
        user.setName("zyh");
        int update = userMapper.update(user, queryWrapper);
        System.out.println(update);
    }

    @Test
    public void test05(){
        //将用户名中包含a并且（年龄大于20 或 邮箱为null）的用户信息修改
        //lambda中的条件优先执行
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name" , "a")
                .and(w -> w.gt("age" , 20).isNull("email"));
        User user = new User();
        user.setAge(30);
        int update = userMapper.update(user, queryWrapper);
        System.out.println(update);
    }

    @Test
    public void test06(){
        //查询用户的用户名、年龄、邮箱信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name" , "age" , "email");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }

    @Test
    public void test07(){
        //查询id小于等于100的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("uid" , "select uid from user where uid <= 100");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test08(){
        //将用户名中包含a并且（年龄大于20 或 邮箱为null）的用户信息修改
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.like("name" , "a")
                .and(w -> w.gt("age" , 20).isNull("email"));
        updateWrapper.set("name" , "pdx").set("email","pdx0714.com");
        userMapper.update(null,updateWrapper);
    }

    //模拟真实开发条件，有的字段不会添加值
    @Test
    public void test09(){
        String username = "a";
        Integer ageBegin = null;
        Integer ageEnd = 30;
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //StringUtils用com.baomidou.mybatisplus包下的
        queryWrapper.like(StringUtils.isNotBlank(username),"name",username)
                .ge(ageBegin != null ,"age" , ageBegin)
                .le(ageEnd != null , "age" , ageEnd);
        //SELECT uid AS id,name,age,email FROM user WHERE (name LIKE ? AND age <= ?)
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test10(){
        String username = "a";
        Integer ageBegin = null;
        Integer ageEnd = 30;
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(username),User::getName , username)
                .ge(ageBegin != null ,User::getAge , ageBegin)
                .le(ageEnd != null , User::getAge , ageEnd);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void test11(){
        //将用户名中包含a并且（年龄大于20 或 邮箱为null）的用户信息修改
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.like(User::getName , "a")
                .and(w -> w.gt(User::getAge , 20).isNull(User::getEmail));
        updateWrapper.set(User::getName , "pdx").set(User::getEmail,"pdx0714.com");
        userMapper.update(null,updateWrapper);
    }
}
