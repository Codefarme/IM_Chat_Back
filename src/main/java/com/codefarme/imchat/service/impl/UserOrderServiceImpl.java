package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.mapper.UserOrderMapper;
import com.codefarme.imchat.pojo.UserOrder;
import com.codefarme.imchat.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrderServiceImpl implements UserOrderService {


    @Autowired
    private UserOrderMapper userOrderMapper;//用户和订单的关联表

    @Override
    public UserOrder selectUserByOutTrade(String out_trade_no) {
        return userOrderMapper.selectUserByOutTrade(out_trade_no);
    }
}
