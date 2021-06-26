package com.gy.crm.settings.dao;

import com.gy.crm.settings.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    //通过用户名和密码和ip进行查询
    User login(@Param("loginAct") String loginAct,
               @Param("loginPwd")String loginPwd);

    List<User> queryUser();
}
