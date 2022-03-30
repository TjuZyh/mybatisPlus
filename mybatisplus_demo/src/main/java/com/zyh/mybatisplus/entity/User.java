package com.zyh.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zyh.mybatisplus.enums.SexEnum;
import lombok.Data;

@Data
//设置对应数据库中的表名
@TableName("user")
public class User {

    //将属性所对应的字段指定为主键
    //@TableID注解的value属性用于指定主键的字段
    //@TableID注解的type属性用于指定主键的生成策略，默认为雪花算法（ASSIGN_ID）
    //  可以设置为自动递增：AUTO
    @TableId(value = "uid" , type = IdType.ASSIGN_ID)
    private Long id;

    private SexEnum sex;

    //指定属性所对应的字段名
    @TableField("name")
    private String name;

    private Integer age;

    private String email;

    /*//逻辑删除字段
    //添加后通用Mapper中的删除方法变为逻辑删除，即若删除将is_delete字段置为1
    @TableLogic
    private Integer isDelete;*/
}
