package com.gy.crm.workbench.service.impl;

import com.gy.crm.settings.dao.UserDao;
import com.gy.crm.settings.entity.User;
import com.gy.crm.utils.DateTimeUtil;
import com.gy.crm.utils.UUIDUtil;
import com.gy.crm.workbench.dao.CustomerDao;
import com.gy.crm.workbench.dao.TranDao;
import com.gy.crm.workbench.dao.TranHistoryDao;
import com.gy.crm.workbench.entity.Customer;
import com.gy.crm.workbench.entity.Tran;
import com.gy.crm.workbench.entity.TranHistory;
import com.gy.crm.workbench.service.TransactionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("TransactionServiceImpl")
public class TransactionServiceImpl implements TransactionService {
    @Resource(name = "userDao")
    private UserDao userDao;
    @Resource(name = "customerDao")
    private CustomerDao customerDao;
    @Resource(name = "tranDao")
    private TranDao tranDao;
    @Resource(name = "tranHistoryDao")
    private TranHistoryDao tranHistoryDao;
    @Override
    public List<User> getOwnerList() {
        List<User> ulist = userDao.getOwnerList();
        return ulist;
    }

    @Override
    public boolean saveTran(Tran tran, String customerName) {
        System.out.println("saveTranService执行");
        boolean flag = true;
        //通过customerName进行模糊查询
        Customer customer = customerDao.searchHasCustomer(customerName);
        if(customer == null) {
            //需要新建一个客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(tran.getName());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());
            int count = customerDao.insertCustomer(customer);
            if (count != 1) {
                flag = false;
                System.out.println("新建客户失败");
            }
        }
        //程序到这说明一定有客户
        tran.setCustomerId(customer.getId());
        int count = tranDao.insert(tran);
        if(count != 1) {
            flag = false;
            System.out.println("新建交易失败");
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        int count1 = tranHistoryDao.insert(tranHistory);
        if(count1 != 1) {
            flag = false;
            System.out.println("新建交易历史失败");
        }
        return flag;
    }

    @Override
    public Tran getTranById(String tid) {
        System.out.println("getTranByIdService层执行");
        Tran tran = tranDao.getTranById(tid);
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryById(String tid) {
        System.out.println("getTranHistoryByIdService执行");
        List<TranHistory> list = tranHistoryDao.getTranHistoryById(tid);
        return list;
    }

    @Override
    public Map<String, Object> changeStage(Tran tran1) {
        System.out.println("changeStage开始执行");
        boolean success = true;
        int count = tranDao.changeStageById(tran1);
        if(count != 1) {
            System.out.println("修改交易信息失败");
            success = false;
        }
        Tran tran = tranDao.getTranById(tran1.getId());
        tran.setEditBy(tran1.getEditBy());
        tran.setEditTime(tran1.getEditTime());
        tran.setPossibility(tran1.getPossibility());
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        int count1 = tranHistoryDao.insert(tranHistory);
        if(count1 != 1) {
            success = false;
            System.out.println("插入交易历史失败");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("tran",tran);
        map.put("success",success);
        return map;
    }
}
