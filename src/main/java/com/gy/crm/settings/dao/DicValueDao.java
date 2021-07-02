package com.gy.crm.settings.dao;

import com.gy.crm.settings.entity.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getThisTypeCodeList(String code);
}
