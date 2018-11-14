package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserVip;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    UserVip getVipByAccount(String account);
}