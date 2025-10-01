package com.example.hotel.Page;

import java.util.List;

// 简单的分页类
public class Page<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;

    public Page() {
    }

    public Page(List<T> content, int totalPages, long totalElements, int number, int size) {
        this.content = content;
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
        this.totalPages=size== 0?1:(int)Math.ceil((double) totalElements/size);
    }

    /**
     * 获取
     * @return content
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * 获取
     * @return totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * 设置
     * @param totalPages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * 获取
     * @return totalElements
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * 设置
     * @param totalElements
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * 获取
     * @return number
     */
    public int getNumber() {
        return number;
    }

    /**
     * 设置
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * 获取
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * 设置
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    public String toString() {
        return "Page{content = " + content + ", totalPages = " + totalPages + ", totalElements = " + totalElements + ", number = " + number + ", size = " + size + "}";
    }

    // 构造函数、getter、setter
}