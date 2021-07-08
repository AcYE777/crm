package com.gy.crm.workbench.service.impl;

import com.gy.crm.workbench.dao.TranDao;
import com.gy.crm.workbench.service.ChartService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ChartServiceImpl")
public class ChartServiceImpl implements ChartService {
    @Resource(name = "tranDao")
    private TranDao tranDao;
    @Override
    public Map<String, Object> showStage() {
        int total = tranDao.getTotal();
        System.out.println("查询总记录条数成功");
        List<Map<Integer,String>> dataList = tranDao.getStageByGroup();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        System.out.println("分组查询成功");
        return map;
    }
}
