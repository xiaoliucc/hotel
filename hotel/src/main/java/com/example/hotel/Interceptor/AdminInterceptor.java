package com.example.hotel.Interceptor;

import com.example.hotel.AdminOnly;
import com.example.hotel.R;
import com.example.hotel.User.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson.JSON;
@Component
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*只拦截带@AdminOnly的方法*/
        if(!(handler instanceof HandlerMethod))return true;
        HandlerMethod hm=(HandlerMethod) handler;
        if (!hm.hasMethodAnnotation(AdminOnly.class))return true;
        User user=(User) request.getSession().getAttribute("loginUser");
        if(user==null || !"ADMIN".equals(user.getRole())){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(R.fail(403,"需要管理员权限")));
            return false;
        }
        return true;
    }
}
