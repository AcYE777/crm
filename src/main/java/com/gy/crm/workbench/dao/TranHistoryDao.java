package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.TranHistory;

import java.util.List;

public interface TranHistoryDao {
    int insert(TranHistory tranHistory);

    List<TranHistory> getTranHistoryById(String tid);
}
