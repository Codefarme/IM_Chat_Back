package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserSmsMsg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserSmsMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserSmsMsg record);

    int insertSelective(UserSmsMsg record);

    UserSmsMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserSmsMsg record);

    int updateByPrimaryKey(UserSmsMsg record);


    UserSmsMsg getCodeByAccountAndCode(@Param("account") String account, @Param("code")String code);

    UserSmsMsg getSMSMesage(@Param("account")String account);
}