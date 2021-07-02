package com.gy.crm.workbench.dao;

import org.apache.ibatis.annotations.Param;

public interface ClueActivityRelationDao {
    int disAssociate(String tcarId);

    int associateAct(@Param("id")String id,@Param("clueId")String clueId,
                     @Param("aid")String aid);
}
