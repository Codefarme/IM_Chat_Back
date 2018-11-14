package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicView;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicViewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicView record);

    int insertSelective(DynamicView record);

    DynamicView selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicView record);

    int updateByPrimaryKey(DynamicView record);

    Integer selectCountByCid(Integer id);
}