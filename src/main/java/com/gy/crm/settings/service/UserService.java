package com.gy.crm.settings.service;

import com.gy.crm.settings.entity.User;

public interface UserService {
    //通过用户名和密码和ip进行查询
    User login(String loginAct, String loginPwd, String ip);
}
