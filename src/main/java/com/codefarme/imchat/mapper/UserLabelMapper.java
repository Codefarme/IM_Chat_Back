package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserLabel;
import org.springframework.stereotype.Repository;


@Repository
public interface UserLabelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserLabel record);

    int insertSelective(UserLabel record);

    UserLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserLabel record);

    int updateByPrimaryKey(UserLabel record);

    UserLabel selectByAccount(String account);
}