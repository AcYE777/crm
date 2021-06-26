package com.gy.crm.workbench.service;

import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Activity;

import java.util.List;

public interface ActivityService {
    List<User> queryUser();

    boolean save(Activity activity);
}
