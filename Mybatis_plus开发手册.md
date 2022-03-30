### Mybatis_plus开发手册

------

#### 一 Mybatis_plus集成与测试

##### 1. 创建测试数据

```mysql
-- 数据库：mybatis_plus
CREATE TABLE `user` (
	`id` bigint(20) NOT NULL COMMENT '主键ID', 
  `name` varchar(30) DEFAULT NULL COMMENT '姓名', 
  `age` int(11) DEFAULT NULL COMMENT '年龄', 
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱', PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');
```

##### 2. 创建Springboot工程

**利用IDEA的spring Initializr创建项目，勾选相关依赖模块**

**添加依赖：**

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.4</version>
        <relativePath/> <!-- lookup parent from repository -->
</parent>
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--mybatisPlus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
        <!--druid连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.17</version>
        </dependency>

        <!--官方starter形式-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.17</version>
        </dependency>
    </dependencies>
```

##### 3. 配置Mybatis_plus

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mybatis_plus
    username: root
    password: 12345678
    #注意比较新版本的mysql驱动变成了com.mysql.cj.jdbc.Driver
    driver-class-name: com.mysql.cj.jdbc.Driver
```

##### 4. 启动类上添加注解

**需要在启动类上添加包扫描注解**

```java
@SpringBootApplication
@MapperScan("com.zyh.mybatisplus.mapper")
public class MybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisplusApplication.class, args);
    }

}
```

##### 5. 创建实体以及Mapper

```java
@Data
//设置对应数据库中的表名
@TableName("user")
public class User {
  	//将属性所对应的字段指定为主键
    //@TableID注解的value属性用于指定主键的字段
    //@TableID注解的type属性用于指定主键的生成策略，默认为雪花算法（ASSIGN_ID）
    // 可以设置为自动递增：AUTO
    @TableId(value = "uid" , type = IdType.ASSIGN_ID)
    private Long id;
  
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

@Repository 
public interface UserMapper extends BaseMapper<User> {

}
```

##### 6. 编写测试用例

```java
@SpringBootTest
class MybatisplusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectAll(){
        userMapper.selectList(null).forEach(System.out::println);
    }
}
```

**关于：自动注入时报错，因为找不到注入的对象，因为类是动态创建的，但是程序可以正确 的执行。**

**解决：可以在mapper接口上添加 @Repository 注解**

##### 7. 添加日志功能

```yaml
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

#### 二 通用Mapper

##### 1. 删除

**通过ID进行删除**

```java
@Test
public void testDeleteById(){
//通过id删除用户信息
//DELETE FROM user WHERE id=?
    int result = userMapper.deleteById(1475754982694199298L); 
    System.out.println("受影响行数:"+result);
}
```

**通过ID进行批量删除**

```java
@Test
public void testDeleteBatchIds(){ 
//通过多个id批量删除
//DELETE FROM user WHERE id IN ( ? , ? , ? ) 
    List<Long> idList = Arrays.asList(1L, 2L, 3L); 
    int result = userMapper.deleteBatchIds(idList); 
    System.out.println("受影响行数:"+result);
}
```

**通过Map条件删除**

```java
@Test
public void testDeleteByMap(){ 
//根据map集合中所设置的条件删除记录
//DELETE FROM user WHERE name = ? AND age = ? 
    Map<String, Object> map = new HashMap<>(); 
    map.put("age", 23);
    map.put("name", "张三");
    int result = userMapper.deleteByMap(map); 
    System.out.println("受影响行数:"+result);
}
```

##### 2. 插入

```java
@Test
public void testInsert(){
		User user = new User(null, "张三", 23, "zhangsan@atguigu.com"); 
		//INSERT INTO user ( id, name, age, email ) VALUES ( ?, ?, ?, ? ) 
		int result = userMapper.insert(user); System.out.println("受影响行数:"+result);
		//1475754982694199298 
		System.out.println("id自动获取:"+user.getId());
}
```

##### 3. 修改

```java
@Test
public void testUpdateById(){
		User user = new User(4L, "admin", 22, null); 
		//UPDATE user SET name=?, age=? WHERE id=? 
		int result = userMapper.updateById(user); 
		System.out.println("受影响行数:"+result);
}
```

##### 4. 查询

**根据ID查询用户信息**

```java
@Test
public void testSelectById(){
    //根据id查询用户信息
    //SELECT id,name,age,email FROM user WHERE id=? 
    User user = userMapper.selectById(4L); 
    System.out.println(user);
}
```

**根据ID查询多个用户信息**

```java
@Test
public void testSelectBatchIds(){
		//根据多个id查询多个用户信息
		//SELECT id,name,age,email FROM user WHERE id IN ( ? , ? ) 
		List<Long> idList = Arrays.asList(4L, 5L);
		List<User> list = userMapper.selectBatchIds(idList); 
		list.forEach(System.out::println);
}
```

**通过map条件查询用户信息**

```java
@Test
public void testSelectByMap(){
		//通过map条件查询用户信息
		//SELECT id,name,age,email FROM user WHERE name = ? AND age = ? 
		Map<String, Object> map = new HashMap<>();
		map.put("age", 22);
		map.put("name", "admin");
		List<User> list = userMapper.selectByMap(map); 
		list.forEach(System.out::println);
}
```

**查询所有数据**

```java
@Test
public void testSelectList(){ //查询所有用户信息
    //SELECT id,name,age,email FROM user
    List<User> list = userMapper.selectList(null);
    list.forEach(System.out::println);
}
```

**通过观察BaseMapper中的方法，大多方法中都有Wrapper类型的形参，此为条件构造器，可针对于SQL语句设置不同的条件，若没有条件，则可以为该形参赋值null，即查询(删除/修改)所 有数据**

##### 5. 自定义SQL

**由于plus中给出的SQL语句只是针对于单表操作，所以可以创建自定义的SQL，和mybatis一样**

- 创建mapper.xml文件，路径为resources/mapper/userMapper.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="com.zyh.mybatisplus.mapper.UserMapper">
  
      <!--Map<String , Object> selectMapById(long id);-->
      <select id="selectMapById" resultType="map">
          select id , name , age ,email from user where id = #{id};
      </select>
  </mapper>
  ```

- 自定义方法

  ```java
  		/**
       * 根据id查询用户信息为map集合
       * @param id
       * @return
       */
      Map<String , Object> selectMapById(long id);
  ```

- 测试

  ```java
  		@Test
      public void selectTest(){
          Map<String, Object> map = userMapper.selectMapById(1L);
          System.out.println(map);
      }
  ```

#### 三 通用Service

##### 1. 创建Service接口

**需要继承IService，其中的泛型为操作的实体类**

```java
public interface UserService extends IService<User> {

}
```

##### 2. 创建ServiceImpl实现类

**需要继承于ServiceImpl，其中第一泛型为对象Mapper；第二个为实体类，并实现Service**

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService{

}
```

**3. 测试**

```java
@SpringBootTest
public class MybatisPlusServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCount(){
        int count = userService.count();
        System.out.println(count);
    }
}
```

#### 四 Mybatis-plus常用注解

- **@TableName：**设置对应数据库中的表名
- **@TableId：**将属性所对应的字段指定为主键
  - value属性用于指定主键的字段
  - type属性用于指定主键的生成策略，默认为雪花算法（ASSIGN_ID）；可以设置为自动递增：AUTO
- **@TableField：**指定属性所对应的字段名
- **@TableLogic：**指定为逻辑删除

#### 五 条件构造器

##### 1. QueryWrapper

**组装查询条件**

```java
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
```

**组装排序条件**

```java
		@Test
    public void test02(){
        //查询用户信息，按照年龄的降序排列；若年龄相同，则按照id升序排列
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                .orderByAsc("uid");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
```

**组装删除条件**

```java
		@Test
    public void test03(){
        //删除邮箱地址为null的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isnull("email");
        int result = userMapper.delete(queryWrapper);
    }
```

**组装更新条件**

```java
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
```

**关于条件的优先级**

```java
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
```

**查询目标字段**

```java
		@Test
    public void test06(){
        //查询用户的用户名、年龄、邮箱信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name" , "age" , "email");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }
```

**组装子查询**

```java
    @Test
    public void test07(){
        //查询id小于等于100的用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("uid" , "select uid from user where uid <= 100");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }
```

##### 2. UpdateWrapper

```java
		@Test
    public void test08(){
        //将用户名中包含a并且（年龄大于20 或 邮箱为null）的用户信息修改
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.like("name" , "a")
                .and(w -> w.gt("age" , 20).isNull("email"));
        updateWrapper.set("name" , "pdx").set("email","pdx0714.com");
        userMapper.update(null,updateWrapper);
    }
```

##### 3. condition组装条件

**模拟开发中搜索框中不添加值的情况**

```java
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
```

##### 4.LambdaQueryWrapper

**利用lambdaQueryWrapper可以避免字段名写错的问题**

```java
@Test
public void test10(){
    String username = "a";
    Integer ageBegin = null;
    Integer ageEnd = 30;
    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(StringUtils.isNotBlank(username),User::getName , username)
            .ge(ageBegin != null , User::getAge , ageBegin)
            .le(ageEnd != null , User::getAge , ageEnd);
    List<User> userList = userMapper.selectList(queryWrapper);
    userList.forEach(System.out::println);
}
```

##### 5. LambdaUpdateWrapper

```java
@Test
public void test11(){
    //将用户名中包含a并且（年龄大于20 或 邮箱为null）的用户信息修改
    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.like(User::getName , "a")
            .and(w -> w.gt(User::getAge , 20).isNull(User::getEmail));
    updateWrapper.set(User::getName , "pdx").set(User::getEmail,"pdx0714.com");
    userMapper.update(null,updateWrapper);
}
```

#### 六 分页插件

**分页插件的配置**

```java
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
```

**测试用例**

```java
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
```

#### 七 通用枚举

**配置枚举类扫描**

```yaml
mybatis-plus:
  type-enums-package: com.zyh.mybatisplus.enums
```

**创建枚举类**

```java
@Getter
public enum SexEnum {
    MALE(1 , "male"),
    FEMALE(2 , "female");

    @EnumValue
    private Integer sex;
    private String sexName;

    SexEnum(Integer sex, String sexName) {
        this.sex = sex;
        this.sexName = sexName;
    }
}
```

```java
//引用字段
private SexEnum sex;
```

**测试用例**

```java
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
```

#### 八 代码生成器

##### 1. 添加依赖

```xml
<!--代码生成器-->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.5.1</version>
</dependency>
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.31</version>
</dependency>
```

##### 2. 快速生成类

```java
public class FastAutoGeneratorTest {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/mybatis_plus? characterEncoding=utf-8&userSSL=false", "root", "12345678")
                .globalConfig(builder -> {
                    builder.author("zyh Generator") // 设置作者
                            // .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/zyh/desktop/generatorClass"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zyh") // 设置父包名
                            .moduleName("mybatisplus") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "/Users/zyh/desktop/generatorClass")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker 引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
```

