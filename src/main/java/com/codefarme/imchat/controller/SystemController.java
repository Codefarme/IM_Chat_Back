package com.codefarme.imchat.controller;


import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.service.SystemService;
import com.codefarme.imchat.pojo.SystemConfigAnswer;
import com.codefarme.imchat.pojo.SystemConfigGift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    //获取系统配置的Answer
    @RequestMapping(value = "/config_answer", method = RequestMethod.POST)
    public Result<List<SystemConfigAnswer>> configAnswer(HttpServletRequest request) {

        return systemService.selectAllConfigAnswer(request);
    }

    //获取系统配置的礼物
    @RequestMapping(value = "/config_gift", method = RequestMethod.POST)
    public Result<List<SystemConfigGift>> configGift(HttpServletRequest request) {

        return systemService.selectAllConfigGift(request);
    }
}
