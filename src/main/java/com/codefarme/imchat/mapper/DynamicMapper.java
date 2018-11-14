package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.Dynamic;

import java.util.List;

public interface DynamicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dynamic record);

    int insertSelective(Dynamic record);

    Dynamic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dynamic record);

    int updateByPrimaryKey(Dynamic record);

    Integer deleteDynamic(String account, Integer valueOf);

    List<Dynamic> lookoneSelfDynamic(String account);

    List<Dynamic> selectTotalCount();

    List<Dynamic> selectDynamicByType(Integer type);
}