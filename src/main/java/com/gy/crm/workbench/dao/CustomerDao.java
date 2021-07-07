package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.Customer;

import java.util.List;

public interface CustomerDao {
    Customer searchHasCustomer(String company);

    int insertCustomer(Customer customer);

    List<String> getCustomerName(String customerName);
}
