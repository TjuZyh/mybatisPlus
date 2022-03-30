package com.zyh.mybatisplus.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyh.mybatisplus.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id查询用户信息为map集合
     * @param
     * @return
     */
    /*Map<String , Object> selectMapById(long id);*/

    int insertSelective(User user);

    int deleteById(@Param("id") Long id);

    int updateAgeAndEmail(@Param("age") Integer age, @Param("email") String email);



}
