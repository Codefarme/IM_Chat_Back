package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserHaveGift;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserHaveGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserHaveGift record);

    int insertSelective(UserHaveGift record);

    UserHaveGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserHaveGift record);

    int updateByPrimaryKey(UserHaveGift record);

    List<UserHaveGift> selectAllHaveGift(String account);
    UserHaveGift selectHaveGiftByIDAndAccount(@Param("account") String account, @Param("gid")Integer gid);
}