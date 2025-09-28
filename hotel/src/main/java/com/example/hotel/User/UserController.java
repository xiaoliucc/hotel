package com.example.hotel.User;

import com.example.hotel.AdminOnly;
import com.example.hotel.JWtUtil;
import com.example.hotel.Orders.Order;
import com.example.hotel.R;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWtUtil jwtUtil;
    /*注册*/
    @PostMapping("/register")
    public R<Void> register(@RequestParam String username,
                            @RequestParam String password){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.register(user);
        return R.ok("注册成功");
    }

    @PostMapping("/login")
    public R<Map<String, String>> login(@RequestParam String username,
                                        @RequestParam String password
                                        /*HttpSession session*/){
        User user=userService.login(username,password);
//        session.setAttribute("loginUser",user);
        String token= jwtUtil.generateToken(user.getId(), user.getRole());
        return R.ok("登录成功，角色"+user.getRole(), Map.of("token",token,"role", user.getRole()));
    }
    @GetMapping("/all")
    @AdminOnly
    public R<List<User>> getAll(){
        List<User> list=userService.getAllUsers();
        return R.ok(list);
    }
}
