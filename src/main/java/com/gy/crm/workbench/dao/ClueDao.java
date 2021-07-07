package com.gy.crm.workbench.dao;

import com.gy.crm.workbench.entity.Clue;

import java.util.List;

public interface ClueDao {
    int addClue(Clue clue);

    List<Clue> queryClue();

    Clue detail(String clueId);

    Clue searchClueById(String clueId);

    int deleteByClueId(String clueId);
}
