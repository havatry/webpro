package com.prop.bean;

import java.util.List;

/**
 * ��װ��ҳ����Ϣ
 */
public class Page {
    private int total; // �ܼ�¼��
    private List<Record> data; // SIZE����¼

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
