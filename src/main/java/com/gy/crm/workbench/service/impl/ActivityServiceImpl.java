package com.gy.crm.workbench.service.impl;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.vo.PageListVo;
import com.gy.crm.workbench.dao.ActivityDao;
import com.gy.crm.workbench.dao.ActivityRemarkDao;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.ActivityRemark;
import com.gy.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@Service("ActivityServiceImpl")
public class ActivityServiceImpl implements ActivityService {
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "activityDao")
    private ActivityDao ad;

    @Resource(name = "activityRemarkDao")
    private ActivityRemarkDao ar;
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

    @Override
    public PageListVo<Activity> pageList(Map<String, Object> map) {
        System.out.println("服务器");
        List<Activity> list = ad.queryByCondition(map);
        System.out.println("条件查询完毕");
        System.out.println(list.toString());
        //查询出这样条件下的总记录条数
        Integer total = ad.queryTotalThisCondition(map);
        System.out.println("查询条件记录完毕");
        PageListVo<Activity> pageListVo = new PageListVo<>();
        pageListVo.setTotal(total);
        pageListVo.setList(list);
        System.out.println("服务器执行完毕");
        return pageListVo;
    }

    @Override
    public boolean deleteActivity(String[] actId) {
        System.out.println("Service层进行删除操作");
        int totalRecord = ar.queryByActivityId(actId);
        System.out.println("查询备注表总记录条数"+totalRecord);
        int searchRecord = ar.deleteByActivity(actId);
        System.out.println("删除备注表信息" + searchRecord);
        if((totalRecord == searchRecord)) {
            //说明备注表和活动表删除成功,再删除活动表
            int count = ad.deleteById(actId);
            if(count > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> queryUserAndActivity(String actid) {
        Activity activity = ad.queryByActID(actid);
        List<User> list = userDao.queryUser();
        Map<String,Object> map = new HashMap<>();
        map.put("activity",activity);
        map.put("list",list);
        return map;
    }

    @Override
    public Map<String, Object> updateActivity(Activity activity) {
        System.out.println("修改开始controller");
        Map<String,Object> map = new HashMap<>();
        int count = ad.updateActivity(activity);
        if(count > 0) {
            map.put("success",true);
        }else {
            map.put("success",false);
        }
        return map;
    }

    @Override
    public Activity detail(String actid) {
        System.out.println("detail--Service层开始执行");
        Activity activity = ad.detail(actid);
        System.out.println("detail-Service层执行结束");
        return activity;
    }

    @Override
    public List<ActivityRemark> queryActivityRemarkInfo(String actid) {
        System.out.println("查询备注信息开始Service");
        List<ActivityRemark> activityRemark = ar.queryActivityRemarkInfo(actid);
        System.out.println("查询备注信息结束Service");
        return activityRemark;
    }

    @Override
    public boolean addRemarkInfo(String remarkInfo, String curActId,User user) {
        System.out.println("添加备注Service");
        ActivityRemark ar = new ActivityRemark();
        String id = UUIDUtil.getUUID();
        String curTime = DateTimeUtil.getSysTime();
        ar.setId(id);
        ar.setNoteContent(remarkInfo);
        ar.setCreateTime(curTime);
        ar.setCreateBy(user.getName());
        ar.setEditFlag("0");
        ar.setActivityId(curActId);
        int count = this.ar.addRemarkInfo(ar);
        System.out.println("添加备注Service成功");
        return count == 1;
    }

    @Override
    public boolean deleteActivityRemark(String deleteActId) {
        System.out.println("删除备注信息Service");
        int count = this.ar.deleteActivityRemark(deleteActId);
        System.out.println("删除备注信息Service结束");
        return count == 1;
    }

    @Override
    public ActivityRemark queryNoteContent(String updateActId) {
        System.out.println("显示备注信息Service");
        ActivityRemark activityRemark = ar.queryNoteContent(updateActId);
        System.out.println("显示备注信息Service结束");
        return activityRemark;
    }

    @Override
    public boolean update(String noteContent,String actRemarkId) {
        System.out.println("更新备注信息Service");
        int count = ar.update(noteContent,actRemarkId);
        System.out.println("更新备注信息Service结束");
        return count == 1;
    }
}


