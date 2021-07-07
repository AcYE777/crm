package com.gy.crm.workbench.service;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Tran;
import com.gy.crm.workbench.entity.TranHistory;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<User> getOwnerList();

    boolean saveTran(Tran tran, String customerName);

    Tran getTranById(String tid);

    List<TranHistory> getTranHistoryById(String tid);

    Map<String, Object> changeStage(Tran tran);
}
