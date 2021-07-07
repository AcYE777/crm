package com.gy.crm.workbench.controller;

import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.entity.Clue;
import com.gy.crm.workbench.entity.Tran;
import com.gy.crm.workbench.service.ClueSerivce;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {
    @Resource(name = "ClueSerivceImpl")
    private ClueSerivce cs;

    @RequestMapping(value = "/getOwner.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getOwner() {
        System.out.println("getOwnerController执行");
        List<User> list = cs.getOwner();
        return list;
    }
    @RequestMapping(value = "/addClue.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addClue(Clue clue, HttpServletRequest request) {
        System.out.println("创建Controller");
        User user = (User)request.getSession().getAttribute("user");
        clue.setCreateBy(user.getName());
        boolean success = cs.addClue(clue);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }
    @RequestMapping(value = "/queryClue.do",method = RequestMethod.POST)
    @ResponseBody
    public List<Clue> queryClue() {
        System.out.println("展示clueController");
        List<Clue> list = cs.queryClue();
        return list;
    }
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView detail(HttpServletRequest request) {
        System.out.println("detailController执行");
        String clueId = request.getParameter("clueId");
        Clue clue = cs.detail(clueId);
        ModelAndView mav = new ModelAndView();
        mav.addObject("clue",clue);
        mav.setViewName("forward:/workbench/clue/detail.jsp");
        return mav;

    }
    @RequestMapping(value = "/showActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public List<Activity> showActivity(HttpServletRequest request) {
        System.out.println("showController");
        String clueId = request.getParameter("clueId");
        List<Activity> activity = cs.showActivity(clueId);
        return activity;
    }
    @RequestMapping(value = "/disAssociate.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> disAssociate(String tcarId) {
        boolean success = cs.disAssociate(tcarId);
        Map<String,Object> map = new HashMap<>();
        map.put("success",success);
        return map;
    }
    @RequestMapping(value = "/queryActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public List<Activity> queryActivity(String activityInfo,String clueId) {
        System.out.println(activityInfo+"----"+clueId);
        Map<String,String> map = new HashMap<>();
        map.put("activityInfo",activityInfo);
        map.put("clueId",clueId);
        List<Activity> list = cs.queryActivity(map);
        return list;
    }
    @RequestMapping(value = "/associateAct.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> associateAct(HttpServletRequest request) {
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("clueId",clueId);
        map1.put("activityIds",activityIds);
        boolean success = cs.associateAct(map1);
        map.put("success",success);
        return map;
    }
    @RequestMapping(value = "/searchActivityInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public List<Activity> searchActivityInfo(String activityInfo) {
        System.out.println("查询活动Controller");
        List<Activity> list = cs.searchActivityInfo(activityInfo);
        return list;
    }
    @RequestMapping(value = "/convert.do",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ModelAndView convert(HttpServletRequest request) {
        System.out.println("convertController执行");
        ModelAndView mav = new ModelAndView();
        String clueId = request.getParameter("clueId");
        String hasTran = request.getParameter("hasTran");
        User user = (User)request.getSession().getAttribute("user");
        String createBy = user.getName();
        Tran tran = null;
        if("hasTran".equals(hasTran)) {
            //说明用户填写了表单
            tran = new Tran();
            tran.setMoney(request.getParameter("money"));
            tran.setName(request.getParameter("tradeName"));
            tran.setExpectedDate(request.getParameter("expectedDate"));
            tran.setStage(request.getParameter("stageName"));
            tran.setActivityId(request.getParameter("activityId"));
        }
        //将参数传递到Service层,clueId,createBy,tran对象
        boolean flag = cs.convert(clueId,createBy,tran);
        if(flag) {
            //进行重定向
            System.out.println("转换成功");
            mav.setViewName("redirect:"+"/workbench/clue/index.jsp");
        }
        return mav;
    }
}





























