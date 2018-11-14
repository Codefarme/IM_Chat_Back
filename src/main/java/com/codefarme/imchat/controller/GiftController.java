package com.codefarme.imchat.controller;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.service.GiftService;
import com.codefarme.imchat.pojo.UserLoveMoney;
import com.codefarme.imchat.response.UserBuyGiftResponse;
import com.codefarme.imchat.response.UserGiveGiftResponse;
import com.codefarme.imchat.response.UserHaveGiftResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping("/gift")
public class GiftController {

    @Autowired
    GiftService giftService;

    //拥有的礼物
    @RequestMapping(value = "/have_gift", method = RequestMethod.POST)
    public Result<List<UserHaveGiftResponse>> haveGift(HttpServletRequest request) {


        return giftService.haveGift(request);
    }

    //送出去的礼物
    @RequestMapping(value = "/give_gift", method = RequestMethod.POST)
    public Result<List<UserGiveGiftResponse>> giveGift(HttpServletRequest request) {


        return giftService.giveGift(request);
    }

    //收到的礼物
    @RequestMapping(value = "/receive_gift", method = RequestMethod.POST)
    public Result<List<UserGiveGiftResponse>> receivGift(HttpServletRequest request) {


        return giftService.receivGift(request);
    }




    //购买过的礼物
    @RequestMapping(value = "/buy_gift_record", method = RequestMethod.POST)
    public Result<List<UserBuyGiftResponse>> buyGiftRecord(HttpServletRequest request) {


        return giftService.buyGiftRecord(request);
    }
    //我的爱币
    @RequestMapping(value = "/love_money", method = RequestMethod.POST)
    public Result<UserLoveMoney> loveMoney(HttpServletRequest request) {

        return giftService.loveMoney(request);
    }






    //赠送礼物
    @RequestMapping(value = "/to_give_gift", method = RequestMethod.POST)
    public Result toGiveGift(HttpServletRequest request) {


        return giftService.toGiveGift(request);
    }

    //购买礼物(爱币购买)
    @RequestMapping(value = "/buy_gift", method = RequestMethod.POST)
    public Result buyGift(HttpServletRequest request) {


        return giftService.buyGift(request);
    }
}
