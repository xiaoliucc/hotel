package com.example.hotel.Rooms;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Room {
    private Long id;
    private String roomNumber;
    private String type;
    private BigDecimal price;
    private String status;//AVAILABLE OR BOOKED
    private LocalDateTime createTime;

    public Room() {
    }

    public Room(Long id, String roomNumber, String type, BigDecimal price, String status, LocalDateTime createTime) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
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
     * @return roomNumber
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * 设置
     * @param roomNumber
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    /**
     * 获取
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * 设置
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(String status) {
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
        return "Room{id = " + id + ", roomNumber = " + roomNumber + ", type = " + type + ", price = " + price + ", status = " + status + ", createTime = " + createTime + "}";
    }
}
