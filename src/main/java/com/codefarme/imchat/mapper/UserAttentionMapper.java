package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserAttention;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAttentionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAttention record);

    int insertSelective(UserAttention record);

    UserAttention selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAttention record);

    int updateByPrimaryKey(UserAttention record);

    List<UserAttention> selectByAccount(String account);

    List<UserAttention> selectFansByAccount(String account);

    Integer selectByAccountAndMyAccount(@Param("account") String account, @Param("attentionAccount") String myAccount);

    Integer deleteByAccountAndMyAccount(@Param("account")String account, @Param("attentionAccount")String attentionAccount);
}