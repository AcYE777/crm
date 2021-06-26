package com.gy.crm.filter;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilterPointDo implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器-------------");
        Object user = request.getSession().getAttribute("user");
        String path = request.getServletPath();
        if (null != user || "/settings/user/login.do".equals(path)) {
            return true;
        }
        return false;
    }
}
