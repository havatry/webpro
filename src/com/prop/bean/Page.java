package com.prop.bean;

import java.util.List;

/**
 * 封装分页的信息
 */
public class Page {
    private int total; // 总记录数
    private List<Record> data; // SIZE条记录

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Record> getData() {
        return data;
    }

    public void setData(List<Record> data) {
        this.data = data;
    }
}
