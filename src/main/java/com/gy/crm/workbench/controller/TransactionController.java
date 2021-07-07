package com.gy.crm.workbench.controller;

import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.workbench.entity.Tran;
import com.gy.crm.workbench.entity.TranHistory;
import com.gy.crm.workbench.service.CustomerService;
import com.gy.crm.workbench.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("TransactionController")
@RequestMapping("/workbench/transaction")
public class TransactionController {

    @Resource(name = "TransactionServiceImpl")
    private TransactionService transactionService;
    @Resource(name = "CustomerServiceImpl")
    private CustomerService customerService;
    @RequestMapping(value = "/save.do",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView save() {
        System.out.println("saveController执行");
        List<User> ulist = transactionService.getOwnerList();
        ModelAndView mav = new ModelAndView();
        mav.addObject("ulist",ulist);
        mav.setViewName("forward:/workbench/transaction/save.jsp");
        return mav;
    }

    @RequestMapping(value = "/getCustomerName.do",method = RequestMethod.GET)
    @ResponseBody
    public List<String> getCustomerName(String customerName) {
        System.out.println("获取用户姓名Controller层");
        List<String> list = customerService.getCustomerName(customerName);
        return list;
    }
    @RequestMapping(value = "/saveTran.do",method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView saveTran(HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");
        String customerName = request.getParameter("customerName");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String stage  = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = user.getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        boolean flag = transactionService.saveTran(tran,customerName);
        ModelAndView mav = new ModelAndView();
        if(flag) {
            mav.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mav;
    }
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView detail(HttpServletRequest request) {
        String tid = request.getParameter("tid");
        Tran tran = transactionService.getTranById(tid);
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);
        ModelAndView mav = new ModelAndView();
        mav.addObject("tran",tran);
        mav.setViewName("forward:/workbench/transaction/detail.jsp");
        return mav;
    }
    @RequestMapping(value = "/show.do",method = RequestMethod.POST)
    @ResponseBody
    public List<TranHistory> show(HttpServletRequest request){
        String tid = request.getParameter("tid");
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        List<TranHistory> list = transactionService.getTranHistoryById(tid);
        for(TranHistory tranHistory : list) {
           String possibility = pMap.get(tranHistory.getStage());
           tranHistory.setPossibility(possibility);
        }
        return list;
    }
    @RequestMapping(value = "/changeStage.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> changeStage(HttpServletRequest request) {
        String id = request.getParameter("tid");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String stage = request.getParameter("stage");
        User user = (User)request.getSession().getAttribute("user");
        Map<String,String> pMap = (Map<String,String>)request.getServletContext().getAttribute("pMap");
        Tran tran1 = new Tran();
        tran1.setId(id);
        tran1.setMoney(money);
        tran1.setExpectedDate(expectedDate);
        tran1.setStage(stage);
        tran1.setCreateBy(user.getName());
        tran1.setEditBy(user.getName());
        tran1.setEditTime(DateTimeUtil.getSysTime());
        tran1.setPossibility(pMap.get(stage));
        Map<String,Object> map = transactionService.changeStage(tran1);
        return map;
    }
}
