package com.gy.crm.workbench.service;

import com.gy.crm.settings.entity.User;
import com.gy.crm.vo.PageListVo;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<User> queryUser();

    boolean save(Activity activity);

    PageListVo<Activity> pageList(Map<String, Object> map);

    boolean deleteActivity(String[] actId);

    Map<String, Object> queryUserAndActivity(String actid);

    Map<String, Object> updateActivity(Activity activity);

    Activity detail(String actid);

    List<ActivityRemark> queryActivityRemarkInfo(String actid);

    boolean addRemarkInfo(String remarkInfo, String curActId,User user);

    boolean deleteActivityRemark(String deleteActId);

    ActivityRemark queryNoteContent(String updateActId);

    boolean update(String noteContent,String actRemarkId);
}
