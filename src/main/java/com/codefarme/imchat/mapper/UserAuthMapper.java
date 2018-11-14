package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserAuth;
import com.codefarme.imchat.response.UserAuthResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuth record);

    int insertSelective(UserAuth record);

    UserAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAuth record);

    int updateByPrimaryKey(UserAuth record);

    List<UserAuthResponse> selectAuthUserSex(String sex);

    List<UserAuthResponse> selectAuthUser();

    UserAuth selectByAccount(String account);
}