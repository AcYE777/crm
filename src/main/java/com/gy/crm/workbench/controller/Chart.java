package com.gy.crm.workbench.controller;

import com.gy.crm.workbench.service.ChartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/workbench/chart/transaction")
public class Chart {
    @Resource(name = "ChartServiceImpl")
    private ChartService chartService;

    @RequestMapping(value = "/showStage.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showStage() {
        System.out.println("showStageController执行");
        Map<String,Object> map = chartService.showStage();
        return map;
    }

}
