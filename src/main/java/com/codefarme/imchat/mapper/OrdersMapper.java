package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.Orders;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersMapper {
    int deleteByPrimaryKey(String id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);


    Orders selectUserByOutTrade(String out_trade_no);
}