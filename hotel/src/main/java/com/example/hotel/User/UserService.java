package com.example.hotel.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    /* 注册：密码 MD5 加密，默认角色 USER */
    public void register(User user){
        if(userMapper.findByUsername(user.getUsername())!=null){
            throw new RuntimeException("用户存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        if(user.getRole()==null || user.getRole().isBlank()){
            user.setRole("USER");
        }
        userMapper.insert(user);
    }
    /* 登录，简单校验，返回用户对象 */
    public User login(String username,String password){
        User user=userMapper.findByUsername(username);
        if(user==null){
            throw new RuntimeException("用户不存在");
        }
        String md5=DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if(!md5.equals(user.getPassword())){
            throw new RuntimeException("密码错误");
        }
        return user;
    }
    public List<User> getAllUsers(){
        return userMapper.getAllUser();
    }

    public String getUserRole(String username){
        return userMapper.findByUsername(username).getRole();
    }

}
