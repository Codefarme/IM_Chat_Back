package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserAnswer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAnswer record);

    int insertSelective(UserAnswer record);

    UserAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAnswer record);

    int updateByPrimaryKey(UserAnswer record);

    List<UserAnswer> selectByAccount(String account);

    UserAnswer selectByAccountAndSysId(@Param("account") String account, @Param("sysId") Integer sysId);


}