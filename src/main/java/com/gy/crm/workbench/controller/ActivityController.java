package com.gy.crm.workbench.controller;

import com.gy.crm.settings.entity.User;
import com.gy.crm.vo.PageListVo;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.ActivityRemark;
import com.gy.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Resource(name = "ActivityServiceImpl")
    private ActivityService as;

    @RequestMapping(value = "/queryUser.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> queryUser() {
        List<User> list = as.queryUser();
        return list;
    }

    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> save(Activity activity, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        //创建人为登录的用户
        activity.setCreateBy(user.getName());
        Map<String,Object> map = new HashMap<>();
        boolean success = as.save(activity);
        map.put("success",true);
        return map;
    }
    @RequestMapping(value = "/pageList.do",method = RequestMethod.GET)
    @ResponseBody
    public PageListVo pageList(Activity activity, HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("控制器");
        request.setCharacterEncoding("utf-8");
        Integer pageNo = Integer.valueOf(request.getParameter("pageNo"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
        Map<String,Object> map = new HashMap<>();
        Integer skipCount = (pageNo - 1) * pageSize;
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);
        map.put("name",activity.getName());
        map.put("owner",activity.getOwner());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());
        PageListVo<Activity> pageListVo = as.pageList(map);
        System.out.println(pageListVo.toString());
        return pageListVo;
    }
    @RequestMapping(value = "/deleteActivity.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> deleteActivity(HttpServletRequest request) {
        System.out.println("Controller层进行删除操作");
        String[] actId = request.getParameterValues("activityId");
        System.out.println(actId[0]);
        boolean success = as.deleteActivity(actId);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }

    @RequestMapping(value = "/queryUserAndActivity.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> queryUserAndActivity(HttpServletRequest request) {
        System.out.println("controller修改执行");
        String actid = request.getParameter("actid");
        Map<String,Object> map = as.queryUserAndActivity(actid);
        return map;
    }

    @RequestMapping("/updateActivity.do")
    @ResponseBody
    public Map<String,Object> updateActivity(Activity activity) {
        System.out.println("修改开始controller");
        Map<String,Object> map = as.updateActivity(activity);
        return map;
    }
    @RequestMapping("/detail.do")
    @ResponseBody
    public ModelAndView detail(String actid) {
        System.out.println("detail开始执行");
        ModelAndView mav = new ModelAndView();
        Activity activity = as.detail(actid);
        mav.addObject("activity",activity);
        mav.setViewName("forward:/workbench/activity/detail.jsp");
        return mav;
    }
    @RequestMapping(value = "/queryActivityRemarkInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryActivityRemarkInfo(String actid) {
        System.out.println("查询备注信息Controller");
        List<ActivityRemark> activityRemark = as.queryActivityRemarkInfo(actid);
        Map<String,Object> map = new HashMap<>();
        map.put("activityRemark",activityRemark);
        return map;
    }
    @RequestMapping(value = "/addRemarkInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addRemarkInfo(HttpServletRequest request) {
        System.out.println("添加备注Controller");
        String remarkInfo = request.getParameter("remarkInfo");
        String curActId = request.getParameter("curActId");
        User user = (User)request.getSession().getAttribute("user");
        boolean success = as.addRemarkInfo(remarkInfo,curActId,user);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }
    @RequestMapping(value = "/deleteRemarkInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> deleteRemarkInfo(String deleteActId) {
        System.out.println("删除备注信息Controller");
        boolean success = as.deleteActivityRemark(deleteActId);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }
    @RequestMapping(value = "/queryNoteContent.do",method = RequestMethod.POST)
    @ResponseBody
    public ActivityRemark queryNoteContent(String updateActId) {
        System.out.println("显示备注信息Controller");
        ActivityRemark activityRemark = as.queryNoteContent(updateActId);
        return activityRemark;
    }
    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String noteContent,String actRemarkId) {
        System.out.println("备注更新Controller");
        boolean success = as.update(noteContent,actRemarkId);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }
}
