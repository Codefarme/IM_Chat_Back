package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicShare;

public interface DynamicShareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicShare record);

    int insertSelective(DynamicShare record);

    DynamicShare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicShare record);

    int updateByPrimaryKey(DynamicShare record);
}