package com.gy.crm.listener;

import com.gy.crm.settings.dao.DicTypeDao;
import com.gy.crm.settings.dao.DicValueDao;
import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.DicType;
import com.gy.crm.settings.entity.DicValue;
import com.gy.crm.workbench.service.ClueSerivce;
import com.gy.crm.workbench.service.impl.ClueSerivceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class ContextListener implements ServletContextListener {
    private DicTypeDao dicTypeDao;
    private DicValueDao dicValueDao;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("创建监听器开始");
        ServletContext application = servletContextEvent.getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
        dicTypeDao = wac.getBean(DicTypeDao.class);
        dicValueDao = wac.getBean(DicValueDao.class);
        List<DicType> list = dicTypeDao.getAllTypeCode();
        Map<String, List<DicValue>> map = new HashMap<>();
        for (DicType dicType : list) {
            String typeCode = dicType.getCode();
            List<DicValue> list1 = dicValueDao.getThisTypeCodeList(typeCode);
            map.put(dicType.getCode(), list1);
        }
        Set<String> set = map.keySet();
        for(String key:set) {
            //到时遍历list集合取数据
            application.setAttribute(key+"List",map.get(key));
        }
        System.out.println("application监听器结束");
        System.out.println("读取stageAndPossibility配置文件开始");
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Map<String,String> pMap = new HashMap<>();
        Enumeration<String> e = rb.getKeys();
        while(e.hasMoreElements()) {
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
        System.out.println("读取Stage2Possibility配置文件结束");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
