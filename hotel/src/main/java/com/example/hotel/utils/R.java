package com.example.hotel.utils;

public class R<T> {
    private Integer code;
    private String msg;
    private T data;

    public R() {}

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // Getter and Setter 方法
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }

    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // 静态方法
    public static <T> R<T> ok() {
        return new R<T>(200, "success", null);
    }

    public static <T> R<T> ok(T data) {
        return new R<T>(200, "success", data);
    }

    public static <T> R<T> ok(String msg) {
        return new R<T>(200, msg, null);
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R<T>(200, msg, data);
    }

    public static <T> R<T> fail(String msg) {
        return new R<T>(500, msg, null);
    }

    public static <T> R<T> fail(int code, String msg) {
        return new R<T>(code, msg, null);
    }
}