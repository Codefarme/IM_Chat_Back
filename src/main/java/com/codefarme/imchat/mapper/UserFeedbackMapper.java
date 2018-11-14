package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserFeedback;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFeedbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFeedback record);

    int insertSelective(UserFeedback record);

    UserFeedback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFeedback record);

    int updateByPrimaryKey(UserFeedback record);
}