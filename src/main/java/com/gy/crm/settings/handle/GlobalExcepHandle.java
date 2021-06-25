package com.gy.crm.settings.handle;

import com.gy.crm.settings.entity.User;
import com.gy.crm.settings.exception.loginException;
import com.gy.crm.utils.MD5Util;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExcepHandle {
    //进行处理异常@ExceptionHandle
    @ExceptionHandler(value = {loginException.class})
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request,
                                    HttpServletResponse response,Exception e){
        Map<String,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
    }
}
