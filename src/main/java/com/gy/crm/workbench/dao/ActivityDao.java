package com.gy.crm.workbench.dao;

import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> queryByCondition(Map<String, Object> map);

    Integer queryTotalThisCondition(Map<String, Object> map);

    int deleteById(String[] actId);
}
