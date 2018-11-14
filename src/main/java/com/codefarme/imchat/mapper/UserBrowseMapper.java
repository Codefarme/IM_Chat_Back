package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserBrowse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBrowseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBrowse record);

    int insertSelective(UserBrowse record);

    UserBrowse selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBrowse record);

    int updateByPrimaryKey(UserBrowse record);

    List<UserBrowse> selectByAccount(String account);

    List<UserBrowse> selectByBrowseAccount(String account);

    UserBrowse selectByAccountAndBrowseAccount(@Param("myAccount") String myAccount, @Param("browAccount")String browAccount);
}