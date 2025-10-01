package com.example.hotel.User;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String password;
    private String role;//'ADMIN OR USER'
    private LocalDateTime createTime;

    public User() {
    }

    public User(Long id, String username, String password, String role, LocalDateTime createTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * 设置
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * 获取
     * @return createTime
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "User{id = " + id + ", username = " + username + ", password = " + password + ", role = " + role + ", createTime = " + createTime + "}";
    }
}

