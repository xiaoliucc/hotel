package com.example.hotel.Orders;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long userId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;//BOOKED,CANCELLED,FINISHED;
    private LocalDateTime createTime;

    public Order() {
    }

    public Order(Long id, Long userId, Long roomId, LocalDate checkIn, LocalDate checkOut, String status, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
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
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return roomId
     */
    public Long getRoomId() {
        return roomId;
    }

    /**
     * 设置
     * @param roomId
     */
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    /**
     * 获取
     * @return checkIn
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * 设置
     * @param checkIn
     */
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * 获取
     * @return checkOut
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * 设置
     * @param checkOut
     */
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * 获取
     * @return status
     */
    public String getstatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setstatus(String status) {
        this.status = status;
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
        return "Order{id = " + id + ", userId = " + userId + ", roomId = " + roomId + ", checkIn = " + checkIn + ", checkOut = " + checkOut + ", status = " + status + ", createTime = " + createTime + "}";
    }
}
