package com.gy.crm.settings.controller;

import com.gy.crm.settings.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource(name = "UserServiceImpl")
    private UserService userService;

}
