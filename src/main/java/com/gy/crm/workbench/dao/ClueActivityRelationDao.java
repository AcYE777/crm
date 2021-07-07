package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationDao {
    int disAssociate(String tcarId);

    int associateAct(@Param("id")String id,@Param("clueId")String clueId,
                     @Param("aid")String aid);

    List<ClueActivityRelation> searchByClueId(String clueId);

    void deleteByClueId(String clueId);
}
