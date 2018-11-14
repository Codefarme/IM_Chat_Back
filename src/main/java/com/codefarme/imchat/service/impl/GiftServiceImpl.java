package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.mapper.*;
import com.codefarme.imchat.oss.OSUtils;
import com.codefarme.imchat.pojo.*;
import com.codefarme.imchat.response.UserBuyGiftResponse;
import com.codefarme.imchat.response.UserGiveGiftResponse;
import com.codefarme.imchat.response.UserHaveGiftResponse;
import com.codefarme.imchat.service.GiftService;
import com.codefarme.imchat.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class GiftServiceImpl implements GiftService {

    @Autowired
    UserHaveGiftMapper haveGiftMapper;

    @Autowired
    UserGiveGiftMapper giveGiftMapper;

    @Autowired
    SystemConfigGiftMapper configGiftMapper;

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserBuyGiftRecordMapper buyGiftRecordMapper;
    @Autowired
    UserLoveMoneyMapper loveMoneyMapper;


    @Autowired
    private Sid sid;

    //我拥有的礼物
    @Override
    public Result<List<UserHaveGiftResponse>> haveGift(HttpServletRequest request) {

        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少必传参数", null);
        }

        List<UserHaveGiftResponse> haveGiftResponses = new ArrayList<>();
        List<UserHaveGift> haveGiftList = haveGiftMapper.selectAllHaveGift(account);
        for (UserHaveGift haveGift : haveGiftList) {

            SystemConfigGift configGift = configGiftMapper.selectByPrimaryKey(haveGift.getGid());

            UserHaveGiftResponse haveGiftResponse = new UserHaveGiftResponse();
            haveGiftResponse.userHaveGift = haveGift;
            haveGiftResponse.haveGiftUrl = OSUtils.getUrl(configGift.getUrl());
            haveGiftResponse.haveGiftName = configGift.getName();
            haveGiftResponse.haveGiftPrice = String.valueOf(configGift.getPrice());

            haveGiftResponses.add(haveGiftResponse);
        }


        return new Result<>(0, "", haveGiftResponses);
    }


    //送出的礼物
    @Override
    public Result<List<UserGiveGiftResponse>> giveGift(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少必传参数", null);
        }

        List<UserGiveGiftResponse> giveGiftResponses = new ArrayList<>();

        List<UserGiveGift> giveGiftList = giveGiftMapper.selectAllGiveGift(account);
        for (UserGiveGift giveGift : giveGiftList) {
            SystemConfigGift configGift = configGiftMapper.selectByPrimaryKey(giveGift.getGid());
            UserInfo user = userInfoMapper.getUserByAccount(giveGift.getGiveAccount());

            UserGiveGiftResponse response = new UserGiveGiftResponse();
            String time = DateUtil.timeDifference(giveGift.getAddTime());
            if (StringUtils.isEmpty(time)) {
                time = "刚刚";
            }

            giveGift.setAddTime(time);
            response.userGiveGift = giveGift;
            response.giveGiftUrl = OSUtils.getUrl(configGift.getUrl());
            response.avatar = OSUtils.getUrl(user.getAvatar());
            response.userName = user.getUsername();
            giveGiftResponses.add(response);
        }

        return new Result<>(0, "", giveGiftResponses);
    }

    //收到的礼物
    @Override
    public Result<List<UserGiveGiftResponse>> receivGift(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少必传参数", null);
        }

        List<UserGiveGiftResponse> giveGiftResponses = new ArrayList<>();

        List<UserGiveGift> giveGiftList = giveGiftMapper.selectAllReceiveGift(account);
        for (UserGiveGift giveGift : giveGiftList) {
            SystemConfigGift configGift = configGiftMapper.selectByPrimaryKey(giveGift.getGid());
            UserInfo user = userInfoMapper.getUserByAccount(giveGift.getAccount());//这里和发出去的礼物获取的账号不同

            UserGiveGiftResponse response = new UserGiveGiftResponse();
            String time = DateUtil.timeDifference(giveGift.getAddTime());
            if (StringUtils.isEmpty(time)) {
                time = "刚刚";
            }

            giveGift.setAddTime(time);
            response.userGiveGift = giveGift;
            response.giveGiftUrl = OSUtils.getUrl(configGift.getUrl());
            response.avatar = OSUtils.getUrl(user.getAvatar());
            response.userName = user.getUsername();
            giveGiftResponses.add(response);
        }

        return new Result<>(0, "", giveGiftResponses);
    }

    //送礼
    @Transactional
    @Override
    public Result toGiveGift(HttpServletRequest request) {
        String account = request.getParameter("account");
        String giveAccount = request.getParameter("giveAccount");//接收礼物的账号
        String id = request.getParameter("id");//要赠送的礼物id
        String num = request.getParameter("num");//赠送的数量
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(giveAccount) || StringUtils.isEmpty(id) || StringUtils.isEmpty(num)) {
            return new Result<>(1, "缺少必传参数", null);
        }
        if (Integer.valueOf(num) <= 0) {
            return new Result<>(1, "非法请求", null);
        }

        //1. 自己的礼物数量减去
        UserHaveGift haveGift = haveGiftMapper.selectHaveGiftByIDAndAccount(account, Integer.valueOf(id));
        if (haveGift == null) {
            return new Result<>(1, "当前用户没有该礼物", null);
        } else {
            if (Integer.valueOf(num) > haveGift.getNum()) {
                return new Result<>(1, "当前用户礼物数量不足", null);
            }
            haveGift.setNum(haveGift.getNum() - Integer.valueOf(num));
            haveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.updateByPrimaryKeySelective(haveGift);
        }


        //2. 对方的礼物数量加
        UserHaveGift haveGiveGift = haveGiftMapper.selectHaveGiftByIDAndAccount(giveAccount, Integer.valueOf(id));
        if (haveGiveGift == null) {
            haveGiveGift = new UserHaveGift();
            haveGiveGift.setAccount(giveAccount);
            haveGiveGift.setGid(Integer.valueOf(id));
            haveGiveGift.setNum(Integer.valueOf(num));
            haveGiveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.insertSelective(haveGift);
        } else {
            haveGiveGift.setNum(haveGift.getNum() + Integer.valueOf(num));
            haveGiveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.updateByPrimaryKeySelective(haveGift);
        }


        return new Result<>(0, "赠送成功", null);
    }

    //购买礼物【这个是使用虚拟货币购买礼物接口】
    @Transactional
    @Override
    public Result buyGift(HttpServletRequest request) {
        String account = request.getParameter("account");
        String id = request.getParameter("id");//礼物id
        String num = request.getParameter("num");//礼物购买数量
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(id) || StringUtils.isEmpty(num)) {
            return new Result(1, "缺少必传参数", null);
        }


        //获取当前礼物的价格
        SystemConfigGift gift = configGiftMapper.selectByPrimaryKey(Integer.valueOf(id));
        Integer amount = gift.getPrice() * Integer.valueOf(num);//应付金额


        //获取自己的爱币余额
        UserLoveMoney money = loveMoneyMapper.selectByAccount(account);
        Integer balance = money.getLoveMoney();//我拥有的余额
        if (balance <= 0 || balance < amount) { //如果小于0的话或者购买的金额大于自己拥有的爱币金额就无法购买
            return new Result(1, "余额不足无法购买", null);
        }

        money.setLoveMoney(balance - amount);//付款后的余额

        loveMoneyMapper.updateByPrimaryKeySelective(money);//更新余额

        //更新用户拥有的礼物

        //4. 把用户拥有的礼物中寻找有没有该礼物，没有的话插入，有的话更新数量
        UserHaveGift haveGift = haveGiftMapper.selectHaveGiftByIDAndAccount(account, Integer.valueOf(id));
        if (haveGift == null) {
            haveGift = new UserHaveGift();
            haveGift.setAccount(account);
            haveGift.setGid(Integer.valueOf(id));
            haveGift.setNum(Integer.valueOf(num));
            haveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.insertSelective(haveGift);
        } else {
            haveGift.setNum(haveGift.getNum() + Integer.valueOf(num));
            haveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.updateByPrimaryKeySelective(haveGift);
        }


        //生成用户购买记录
        UserBuyGiftRecord record = new UserBuyGiftRecord();
        record.setAccount(account);
        record.setGid(Integer.valueOf(id));
        record.setNum(Integer.valueOf(num));//礼物的购买数量
        record.setOrderNum(sid.nextShort());//生成一个虚拟订单购买的唯一号

        buyGiftRecordMapper.insertSelective(record);

        return new Result(0, "购买成功", null);//前台要更新自己拥有的礼物数据，爱币和礼物购买记录
    }


    //TODO 因为用户购买礼物记录表和订单表在使用爱币购买礼物的时候不做关联所以需要增加字段
    @Override
    public Result<List<UserBuyGiftResponse>> buyGiftRecord(HttpServletRequest request) {
        //1. 获取对应账号所有的购买记录

        return null;
    }

    //获取指定用户的爱币余额【一般应该限制只能获取自己的】
    @Override
    public Result<UserLoveMoney> loveMoney(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "缺少必传参数", null);
        }
        UserLoveMoney money = loveMoneyMapper.selectByAccount(account);
        return new Result(0, "", money);
    }


    //购买爱币支付成功后调用的更新爱币数量接口
    @Override
    public void upadeUserLoveMoney(String account, Integer buyCounts, String out_trade_no) {
        UserLoveMoney money = loveMoneyMapper.selectByAccount(account);
        if (money == null) {
            money = new UserLoveMoney();
            money.setAccount(account);
            money.setLoveMoney(buyCounts);//等于爱币数量
            money.setUpdateTime(DateUtil.getCurrentTime());
            loveMoneyMapper.insertSelective(money);
        } else {
            money.setLoveMoney(money.getLoveMoney() + buyCounts);
            money.setUpdateTime(DateUtil.getCurrentTime());
            loveMoneyMapper.updateByPrimaryKeySelective(money);
        }

    }


    //购买礼物支付成功后调用的更新礼物数量接口  [暂时只能通过爱币购买礼物]
    @Override
    public void upadeUserGift(String account, String id, Integer buyCounts, String orderNum) {
       /* //3. 把对应的礼物ID和对应的购买数量增加到用户购买礼物记录表
        UserBuyGiftRecord record = new UserBuyGiftRecord();
        record.setAccount(account);
        record.setGid(Integer.valueOf(id));
        record.setOrderNum(orderNum);
        buyGiftRecordMapper.insertSelective(record);

        //4. 把用户拥有的礼物中寻找有没有该礼物，没有的话插入，有的话更新数量
        UserHaveGift haveGift = haveGiftMapper.selectHaveGiftByIDAndAccount(account, Integer.valueOf(id));
        if (haveGift == null) {
            haveGift = new UserHaveGift();
            haveGift.setAccount(account);
            haveGift.setGid(Integer.valueOf(id));
            haveGift.setNum(buyCounts);
            haveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.insertSelective(haveGift);
        } else {
            haveGift.setNum(haveGift.getNum() + buyCounts);
            haveGift.setAddTime(DateUtil.getCurrentTime());
            haveGiftMapper.updateByPrimaryKeySelective(haveGift);
        }*/


    }
}
