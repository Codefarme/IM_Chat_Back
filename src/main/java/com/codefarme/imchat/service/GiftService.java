package com.codefarme.imchat.service;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.pojo.UserLoveMoney;
import com.codefarme.imchat.response.UserBuyGiftResponse;
import com.codefarme.imchat.response.UserGiveGiftResponse;
import com.codefarme.imchat.response.UserHaveGiftResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface GiftService {
    Result<List<UserHaveGiftResponse>> haveGift(HttpServletRequest request);

    Result<List<UserGiveGiftResponse>> giveGift(HttpServletRequest request);

    //送出的礼物
    Result<List<UserGiveGiftResponse>> receivGift(HttpServletRequest request);

    Result toGiveGift(HttpServletRequest request);

    Result buyGift(HttpServletRequest request);





    //购买礼物支付成功后调用的更新礼物数量接口
    void upadeUserGift(String account, String id, Integer buyCounts, String orderNum);

    void upadeUserLoveMoney(String account, Integer buyCounts, String out_trade_no);

    Result<List<UserBuyGiftResponse>> buyGiftRecord(HttpServletRequest request);

    Result<UserLoveMoney> loveMoney(HttpServletRequest request);
}
