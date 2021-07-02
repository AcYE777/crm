package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDao {

    int queryByActivityId(String[] actId);

    int deleteByActivity(String[] actId);

    List<ActivityRemark> queryActivityRemarkInfo(String actid);

    int addRemarkInfo(ActivityRemark ar);

    int deleteActivityRemark(String deleteActId);

    ActivityRemark queryNoteContent(String updateActId);

    int update(@Param("noteContent") String noteContent, @Param("actRemarkId") String actRemarkId);
}
