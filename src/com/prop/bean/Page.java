package com.prop.bean;

import java.util.List;

/**
 * 封装分页的信息
 */
public class Page {
    private int total; // 总记录数
    private List<Record> data; // SIZE条记录
    private boolean autoFlush; // 是否前端自动刷新

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

    public boolean isAutoFlush() {
        return autoFlush;
    }

    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }
}
