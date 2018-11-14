package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.Gift;

public interface GiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gift record);

    int insertSelective(Gift record);

    Gift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gift record);

    int updateByPrimaryKey(Gift record);
}