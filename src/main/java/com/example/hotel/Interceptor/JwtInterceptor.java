package com.example.hotel.Interceptor;

import com.alibaba.fastjson.JSON;
import com.example.hotel.utils.JWtUtil;
import com.example.hotel.utils.R;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JWtUtil jWtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("Authorization");
        if(token==null || !token.startsWith("Bearer")){
            witeJson(response, R.fail(401,"缺少令牌"));
            return false;
        }
        token=token.substring(7);
        try {
            Claims claims=jWtUtil.parseToken(token);
            request.setAttribute("userId",Long.valueOf(claims.getSubject()      ));
            request.setAttribute("role",claims.get("role"));
            return true;
        }catch (Exception e){
            witeJson(response,R.fail(401,"令牌无效"));
            return false;
        }
    }

    public void witeJson(HttpServletResponse resp,R<?> r)throws IOException{
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(JSON.toJSONString(r));
    }
}
