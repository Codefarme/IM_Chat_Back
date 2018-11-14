package com.codefarme.imchat.service.impl;


import com.codefarme.imchat.mapper.UserVipMapper;
import com.codefarme.imchat.service.UserVipService;
import com.codefarme.imchat.pojo.UserVip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VipUserServiceImpl implements UserVipService {
    @Autowired
    private UserVipMapper userVipMapper;

    @Override
    public UserVip getVipByAccount(String account) {

        UserVip userVip = userVipMapper.getVipByAccount(account);
        return userVip;
    }

    @Override
    public int insert(UserVip record) {
        return userVipMapper.insert(record);
    }

    @Override
    public int updateByPrimaryKeySelective(UserVip record) {
        return userVipMapper.updateByPrimaryKeySelective(record);
    }




}
