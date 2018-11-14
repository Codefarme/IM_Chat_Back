package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicVideo;
import org.springframework.stereotype.Component;

@Component
public interface DynamicVideoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicVideo record);

    int insertSelective(DynamicVideo record);

    DynamicVideo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicVideo record);

    int updateByPrimaryKey(DynamicVideo record);
}