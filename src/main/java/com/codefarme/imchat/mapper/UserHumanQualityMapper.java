package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserHumanQuality;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHumanQualityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserHumanQuality record);

    int insertSelective(UserHumanQuality record);

    UserHumanQuality selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserHumanQuality record);

    int updateByPrimaryKey(UserHumanQuality record);

    UserHumanQuality selectByAccount(String account);
}