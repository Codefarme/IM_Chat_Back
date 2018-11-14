package com.codefarme.imchat.service;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.response.UserAuthResponse;
import com.codefarme.imchat.response.UserInfoResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MakeFriendSerice {



    /**
     * 在线匹配
     *
     * @param request
     * @return
     */
    Result<List<UserAuthResponse>> onlineMatch(HttpServletRequest request);


    /**
     * 扔瓶子
     *
     * @param request
     */
    void throwbottles(HttpServletRequest request);

    /**
     * 拾起一个瓶子
     *
     * @param request
     * @return
     */
    Map<String, Object> pickupbottles(HttpServletRequest request);

    
    //回复瓶子新版本
    void deletebottle(HttpServletRequest request);


    List<UserInfoResponse> match(HttpServletRequest request);
}
