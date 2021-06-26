package com.gy.crm.workbench.controller;

import com.gy.crm.settings.entity.User;
import com.gy.crm.workbench.entity.Activity;
import com.gy.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
}
