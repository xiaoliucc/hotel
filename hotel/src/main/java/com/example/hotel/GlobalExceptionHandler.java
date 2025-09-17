package com.example.hotel;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /*拦截所有RuntimeException*/
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handle(RuntimeException e){
        return R.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void>  handleAll(Exception e){
        return R.fail("系统异常："+e.getMessage());
    }
}
