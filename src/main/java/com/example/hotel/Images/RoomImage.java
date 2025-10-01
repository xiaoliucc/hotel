package com.example.hotel.Images;

import java.time.LocalDateTime;

public class RoomImage {
    private Long id;
    private Long roomId;
    private String imageUrl;
    private Boolean isPrimary;
    private Integer sortOrder;
    private LocalDateTime createTime;

    // 无参构造
    public RoomImage() {
    }

    // 有参构造
    public RoomImage(Long roomId, String imageUrl, Boolean isPrimary) {
        this.roomId = roomId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.sortOrder = 0;
    }

    public RoomImage(Long id, Long roomId, String imageUrl, Boolean isPrimary, Integer sortOrder, LocalDateTime createTime) {
        this.id = id;
        this.roomId = roomId;
        this.imageUrl = imageUrl;
        this.isPrimary = isPrimary;
        this.sortOrder = sortOrder;
        this.createTime = createTime;
    }

    // Getter and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsPrimary() { return isPrimary; }
    public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}