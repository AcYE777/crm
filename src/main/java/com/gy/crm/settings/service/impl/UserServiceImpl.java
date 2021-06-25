package com.gy.crm.settings.service.impl;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.User;
import com.gy.crm.settings.exception.loginException;
import com.gy.crm.settings.service.UserService;
import com.gy.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
    @Resource(name = "userDao")
    private UserDao userDao;

    //通过用户名和密码和ip进行查询
    @Override
    public User login(String loginAct, String loginPwd, String ip) {
        User user = userDao.login(loginAct,loginPwd);
        if (user == null) {
            //说明没有查到
            throw new loginException("用户名或密码错误");
        }
        if(user.getExpireTime().compareTo(DateTimeUtil.getSysTime()) < 0) {
            throw new loginException("用户已失效");
        }
        if(!user.getAllowIps().contains(ip)) {
            throw new loginException("此ip不能访问");
        }
        return user;
    }
}
