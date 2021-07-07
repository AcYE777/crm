package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    List<ClueRemark> searchClueRemark(String clueId);

    void deleteByClueId(String clueId);
}
