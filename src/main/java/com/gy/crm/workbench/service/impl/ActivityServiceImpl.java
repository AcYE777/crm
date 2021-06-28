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
}
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
class Solution {
    ListNode post = null;
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if(left == 1) {
            //若left==1,问题就变成了反转前n个节点了
            ListNode last = reverseN(head,right);
            return last;
        }
        //left不等于1那么
        head.next = reverseBetween(head.next,left - 1,right - 1);
        return head;
    }
    //反转前n个节点
    public ListNode reverseN(ListNode head,int n) {
        if(n == 1) {
            post = head.next;
            return head;
        }
        ListNode last = reverseN(head.next,n-1);
        head.next.next = head;
        head.next = post;
        return last;
    }
}

