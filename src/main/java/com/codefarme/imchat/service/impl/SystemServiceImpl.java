package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.mapper.SystemConfigAnswerMapper;
import com.codefarme.imchat.mapper.SystemConfigGiftMapper;
import com.codefarme.imchat.service.SystemService;
import com.codefarme.imchat.oss.OSUtils;
import com.codefarme.imchat.pojo.SystemConfigAnswer;
import com.codefarme.imchat.pojo.SystemConfigGift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    SystemConfigAnswerMapper answerMapper;

    @Autowired
    SystemConfigGiftMapper giftMapper;


    @Override
    public Result<List<SystemConfigAnswer>> selectAllConfigAnswer(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少必传参数", null);
        }

        List<SystemConfigAnswer> answerList = answerMapper.selectAllConfigAnswer();

        return new Result<>(0, "", answerList);
    }

    @Override
    public Result<List<SystemConfigGift>> selectAllConfigGift(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少必传参数", null);
        }

        List<SystemConfigGift> giftList = giftMapper.selectAllConfigGift();
        for (SystemConfigGift gift : giftList) {
            gift.setUrl(OSUtils.getUrl(gift.getUrl()));
        }

        return new Result<>(0, "", giftList);
    }

    @Override
    public SystemConfigGift  selectConfigGiftByGid(Integer gid) {

        return giftMapper.selectByPrimaryKey(gid);
    }
}
