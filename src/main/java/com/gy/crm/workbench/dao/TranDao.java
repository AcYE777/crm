package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.Tran;

public interface TranDao {
    int insert(Tran tran);

    Tran getTranById(String tid);


    int changeStageById(Tran tran1);
}
