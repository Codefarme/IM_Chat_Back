package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.SystemConfigAnswer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemConfigAnswer record);

    int insertSelective(SystemConfigAnswer record);

    SystemConfigAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemConfigAnswer record);

    int updateByPrimaryKey(SystemConfigAnswer record);

    List<SystemConfigAnswer> selectAllConfigAnswer();
}