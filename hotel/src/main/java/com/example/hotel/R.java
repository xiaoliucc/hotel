package com.example.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //自动生成 getter、setter、toString、equals 等方法
@NoArgsConstructor // 生成无参构造方法
@AllArgsConstructor // 生成全参构造方法
public class R<T> {
    private Integer code; // 200成功，其他失败
    private String msg; //提示信息
    private T data; //具体数据

    public static <T> R<T> ok(){
        return new R<>(200,"success",null);
    }
    public static <T> R<T> ok(T data){
        return new R<>(200,"success",data);
    }
    public static <T> R<T> ok(String msg){
        return new R<>(200,msg,null);
    }
    public static <T> R<T> ok(String msg,T data){
        return new R<>(200,msg,data);
    }
    public static <T> R<T> fail(String msg){
        return new R<>(500,msg,null);
    }
    public static <T> R<T> fail(int code,String msg){
        return new R<>(code,msg,null);
    }
}
