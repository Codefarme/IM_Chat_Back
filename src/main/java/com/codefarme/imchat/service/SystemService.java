package com.codefarme.imchat.service;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.pojo.SystemConfigAnswer;
import com.codefarme.imchat.pojo.SystemConfigGift;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SystemService {


    Result<List<SystemConfigAnswer>> selectAllConfigAnswer(HttpServletRequest request);

    Result<List<SystemConfigGift>> selectAllConfigGift(HttpServletRequest request);

    SystemConfigGift  selectConfigGiftByGid(Integer gid);
}
