package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.Flow;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowMapper {
    int deleteByPrimaryKey(String id);

    int insert(Flow record);

    int insertSelective(Flow record);

    Flow selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Flow record);

    int updateByPrimaryKey(Flow record);
}