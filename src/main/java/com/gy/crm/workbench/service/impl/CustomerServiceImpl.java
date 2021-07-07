package com.gy.crm.workbench.service.impl;

import com.gy.crm.workbench.dao.CustomerDao;
import com.gy.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.List;

@Service("CustomerServiceImpl")
public class CustomerServiceImpl implements CustomerService {
    @Resource(name = "customerDao")
    private CustomerDao customerDao;
    @Override
    public List<String> getCustomerName(String customerName) {
        List<String> list = customerDao.getCustomerName(customerName);
        return list;
    }
}
