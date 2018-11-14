package com.codefarme.imchat.service;


import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.pojo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UserService {


    //别人看我的足迹
    Result<List<UserInfo>> browsed(HttpServletRequest request);

    /**
     * 手机号是否重复
     *
     * @param phone
     * @return
     */
    boolean checkAccountExists(String phone);

    /**
     * 手机号验证码登陆
     *
     * @param request
     */
    Result<Map<String, Object>> phoneSmsLogin(HttpServletRequest request);

    /**
     * qq登陆
     *
     * @param request
     * @return
     */
    Result<Map<String, Object>> qqaccountlogin(HttpServletRequest request);

    /**
     * qq注册
     *
     * @param request
     * @return
     */
    Result<Map<String, Object>> qqaccountregist(HttpServletRequest request);


    /**
     * 发送验证码
     *
     * @param
     */
    Map<String, Object> securityCode(String account);

    /**
     * 编辑个人资料
     *
     * @param request
     */
    void editprofile(HttpServletRequest request);

    Result<String> editHead(HttpServletRequest request);

    /**
     * 通过账号查看个人信息
     *
     * @param request
     * @return
     */
    UserInfo viewprofile(HttpServletRequest request);

    /**
     * 附近的人
     *
     * @param city
     * @param offset
     * @param count
     * @return
     */
    List<UserInfo> getlistuserbycity(String city, String offset, String count);


    List<UserInfo> getContact(String[] accounts);


    //获取用户状态
    Result<Map<String, Object>> selectUserStatus(HttpServletRequest request);


    Result<List<UserAlbum>> photoAlbum(HttpServletRequest request);

    Result<List<UserAlbum>> getPhotoAlbum(HttpServletRequest request);

    Result<List<UserAlbum>> deletePhotoAlbum(HttpServletRequest request);

    Result updateUserExtInfo(HttpServletRequest request);

    void test();

    Result<Map<String, Object>> checkToken(String account, String token);

    Result<UserInfo> getBaseInfo(HttpServletRequest request);

    Result updateSig(HttpServletRequest request);


    Result<List<UserInfo>> browse(HttpServletRequest request);

    Result<List<UserInfo>> attention(HttpServletRequest request);

    Result isAttention(HttpServletRequest request);
    Result cancelAttention(HttpServletRequest request);
    Result<List<UserInfo>> fans(HttpServletRequest request);

    Result go_attention(HttpServletRequest request);


    Result label(HttpServletRequest request);

    Result<UserLabel> getLabel(HttpServletRequest request);


    Result<List<UserAnswer>> getAnswer(HttpServletRequest request);

    Result uploadAnswer(HttpServletRequest request);

    Result<UserHumanQuality> humanQuality(HttpServletRequest request);

    Result<UserAuth> auth(HttpServletRequest request);

    Result goAuth(HttpServletRequest request);

    Result report(HttpServletRequest request);

    Result feedback(HttpServletRequest request);
}
