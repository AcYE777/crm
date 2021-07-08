package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {
    int insert(Tran tran);

    Tran getTranById(String tid);


    int changeStageById(Tran tran1);

    int getTotal();

    List<Map<Integer, String>> getStageByGroup();
}
