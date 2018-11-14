package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserReport;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserReport record);

    int insertSelective(UserReport record);

    UserReport selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserReport record);

    int updateByPrimaryKey(UserReport record);
}