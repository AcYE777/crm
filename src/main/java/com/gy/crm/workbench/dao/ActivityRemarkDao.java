package com.gy.crm.workbench.dao;

public interface ActivityRemarkDao {

    int queryByActivityId(String[] actId);

    int deleteByActivity(String[] actId);
}
