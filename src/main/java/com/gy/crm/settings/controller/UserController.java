package com.gy.crm.settings.controller;

import com.gy.crm.settings.entity.User;
import com.gy.crm.settings.service.UserService;
import com.gy.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {
    @Resource(name = "UserServiceImpl")
    private UserService userService;

    //处理首页登录请求
    @RequestMapping(value = "/login.do",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response){
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //先进行加密再和数据库进行比较
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();
        User user = userService.login(loginAct,loginPwd,ip);
        Map<String,Object> map = new HashMap<>();
        //能查出来user说明登录成功
        request.getSession().setAttribute("user",user);
        map.put("success",true);
        return map;
    }
}
