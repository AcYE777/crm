package com.gy.crm.settings.service.impl;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
    @Resource(name = "userDao")
    private UserDao userDao;
}
