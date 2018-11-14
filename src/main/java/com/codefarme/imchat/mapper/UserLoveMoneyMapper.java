package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserLoveMoney;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoveMoneyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLoveMoney record);

    int insertSelective(UserLoveMoney record);

    UserLoveMoney selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLoveMoney record);

    int updateByPrimaryKey(UserLoveMoney record);

    UserLoveMoney selectByAccount(String account);
}