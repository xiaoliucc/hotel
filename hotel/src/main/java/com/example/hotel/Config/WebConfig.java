package com.example.hotel.Config;

import com.example.hotel.Interceptor.AdminInterceptor;
import com.example.hotel.Interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private JwtInterceptor jwtInterceptor;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/**");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                /*指定排除的 URL*/
                .excludePathPatterns("/api/user/login","/api/user/register");
    }
}
