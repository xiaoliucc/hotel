package com.example.hotel.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    /*根据用户名查询*/
    @Select("select * from users where username=#{username}")
    User findByUsername(String username);

    /*注册插入*/
    @Insert("insert into users(username ,password,role) values (#{username},#{password},#{role})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    /*
        useGeneratedKeys = true表示开启 “使用数据库自动生成的主键” 功能。
        keyProperty = "id"表示将数据库生成的主键值，赋值给 Java 对象中的 id 属性。
     */
    void insert(User user);

    /*全部用户输出*/
    @Select("select * from users")
    List<User> getAllUser();
}
