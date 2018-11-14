package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    UserOrder selectUserByOutTrade(String out_trade_no);
}