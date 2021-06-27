package com.gy.crm.vo;

import java.util.List;

public class PageListVo<T> {
    private Integer total;
    private List<T> list;

    @Override
    public String toString() {
        return "PageListVo{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
