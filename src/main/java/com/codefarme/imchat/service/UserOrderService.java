package com.codefarme.imchat.service;

import com.codefarme.imchat.pojo.UserOrder;

public interface UserOrderService {


    UserOrder selectUserByOutTrade(String out_trade_no);
}
