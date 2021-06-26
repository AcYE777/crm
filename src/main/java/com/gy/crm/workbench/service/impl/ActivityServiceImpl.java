package com.gy.crm.workbench.service.impl;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.workbench.dao.ActivityDao;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.zip.DataFormatException;

@Service("ActivityServiceImpl")
public class ActivityServiceImpl implements ActivityService {
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "activityDao")
    private ActivityDao ad;
    @Override
    public List<User> queryUser() {
        return userDao.queryUser();
    }

    @Override
    public boolean save(Activity activity) {
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        int count = ad.save(activity);
        if(count > 0) {
            return true;
        }
        return false;
    }
}
