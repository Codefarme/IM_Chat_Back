package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserBuyGiftRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBuyGiftRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserBuyGiftRecord record);

    int insertSelective(UserBuyGiftRecord record);

    UserBuyGiftRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserBuyGiftRecord record);

    int updateByPrimaryKey(UserBuyGiftRecord record);
}