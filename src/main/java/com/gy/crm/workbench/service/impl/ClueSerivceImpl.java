package com.gy.crm.workbench.service.impl;

import com.gy.crm.settings.dao.DicTypeDao;
import com.gy.crm.settings.dao.DicValueDao;
import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.DicType;
import com.gy.crm.settings.entity.DicValue;
import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.workbench.dao.ActivityDao;
import com.gy.crm.workbench.dao.ClueActivityRelationDao;
import com.gy.crm.workbench.dao.ClueDao;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.Clue;
import com.gy.crm.workbench.service.ClueSerivce;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("ClueSerivceImpl")
public class ClueSerivceImpl implements ClueSerivce {
    @Resource(name = "dicTypeDao")
    private DicTypeDao dicTypeDao;
    @Resource(name = "dicValueDao")
    private DicValueDao dicValueDao;
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "clueDao")
    private ClueDao clueDao;
    @Resource(name = "activityDao")
    private ActivityDao activityDao;
    @Resource(name = "clueActivityRelationDao")
    private ClueActivityRelationDao clueActivityRelationDao;
    @Override
    public List<User> getOwner() {
        System.out.println("getOwnerService执行");
        List<User> list = userDao.queryUser();
        return list;
    }

    @Override
    public boolean addClue(Clue clue) {
        System.out.println("创建Service开始执行");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        clue.setId(id);
        clue.setCreateTime(createTime);
        int count = clueDao.addClue(clue);
        System.out.println("创建执行结束");
        return count == 1;
    }

    @Override
    public List<Clue> queryClue() {
        System.out.println("展示clueService");
        List<Clue> list = clueDao.queryClue();
        return list;
    }

    @Override
    public Clue detail(String clueId) {
        System.out.println("detailService执行");
        Clue clue = clueDao.detail(clueId);
        return clue;
    }

    @Override
    public List<Activity> showActivity(String clueId) {
        System.out.println("showService开始执行");
        List<Activity> activity = activityDao.showActivity(clueId);
        System.out.println("showService执行结束");
        return activity;
    }

    @Override
    public boolean disAssociate(String tcarId) {
        int count = clueActivityRelationDao.disAssociate(tcarId);
        System.out.println("解除结束");
        return count == 1;
    }

    @Override
    public List<Activity> queryActivity(Map<String, String> map) {
        System.out.println("查询活动Service");
        List<Activity> list = activityDao.queryActivity(map);
        System.out.println("查询活动执行结束");
        return list;
    }

    @Override
    public boolean associateAct(Map<String, Object> map1) {
        System.out.println("关联Service开始");
        boolean flag = true;
        int count = 0;
        //得到活动数组
        String[] s = (String[]) map1.get("activityIds");
        for(String aid : s) {
            count = clueActivityRelationDao.associateAct(UUIDUtil.getUUID(),(String)map1.get("clueId"),aid);
            if(count <= 0) {
                flag = false;
            }
        }
        System.out.println("关联Service结束");
        return flag;
    }

    @Override
    public List<Activity> searchActivityInfo(String activityInfo) {
        System.out.println("查询活动Service开始");
        List<Activity> list = activityDao.searchActivityInfo(activityInfo);
        System.out.println("查询活动Service结束");
        return list;
    }
}
