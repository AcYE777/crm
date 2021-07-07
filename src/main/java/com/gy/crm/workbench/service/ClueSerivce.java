package com.gy.crm.workbench.service;

import com.gy.crm.settings.entity.DicValue;
import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.Clue;
import com.gy.crm.workbench.entity.Tran;

import java.util.List;
import java.util.Map;

public interface ClueSerivce {

    List<User> getOwner();

    boolean addClue(Clue clue);

    List<Clue> queryClue();

    Clue detail(String clueId);

    List<Activity> showActivity(String clueId);

    boolean disAssociate(String tcarId);

    List<Activity> queryActivity(Map<String, String> map);

    boolean associateAct(Map<String, Object> map1);

    List<Activity> searchActivityInfo(String activityInfo);

    boolean convert(String clueId, String createBy, Tran tran);
}
