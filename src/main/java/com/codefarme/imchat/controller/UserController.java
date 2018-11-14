package com.codefarme.imchat.controller;


import com.codefarme.imchat.limit.RequestLimit;
import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.pojo.*;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.service.UserService;
import com.codefarme.imchat.service.UserVipService;
import com.codefarme.imchat.tencent.GenerateSig;
import com.codefarme.imchat.utils.CommonUtils;
import com.codefarme.imchat.utils.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserVipService userVipService;


    @GetMapping("test")
    public Result test() {
        return new Result(0, "", null);
    }


    /*========================================登录业务 start=============================================================*/

    /**
     * 统一登录接口
     *
     * @param request
     * @return
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result<Map<String, Object>> login(HttpServletRequest request) {


        String loginType = request.getParameter("loginType").trim();


        Result<Map<String, Object>> result = null;

        //判断登录类型
        switch (loginType) {

            case "1"://token登陆
                result = tokenLogin(request);
                break;

            case "3"://手机号验证码登录
                result = phoneSmsLogin(request);

                break;

            case "4": //QQ登陆
                result = qqAccountLogin(request);
                break;

        }

        return result;
    }

    @RequestMapping(value = "/status", method = RequestMethod.POST)
    public Result<Map<String, Object>> checkStatus(HttpServletRequest request) {

        return userService.selectUserStatus(request);
    }

    /**
     * token登录请求
     *
     * @param request
     * @return
     */
    private Result<Map<String, Object>> tokenLogin(HttpServletRequest request) {

        String token = request.getParameter("token");
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(account)) {
            return new Result<>(1, "登录失败", null);
        }


        //验证token
        return userService.checkToken(account, token);

    }


    /**
     * 手机号验证码登录
     *
     * @return
     */
    public Result<Map<String, Object>> phoneSmsLogin(HttpServletRequest request) {

        return userService.phoneSmsLogin(request);
    }

    /**
     * qq登陆
     *
     * @return
     */
    public Result<Map<String, Object>> qqAccountLogin(HttpServletRequest request) {

        return userService.qqaccountlogin(request);
    }

    /**
     * qq注册  手机号没有被注册绑定
     *
     * @return
     */

    @RequestMapping(value = "/qq_regist", method = RequestMethod.POST)
    public Result<Map<String, Object>> qqaccountregist(HttpServletRequest request) {

        return userService.qqaccountregist(request);
    }

    /**
     * 发送验证码
     *
     * @return
     */
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    public Result<Map<String, Object>> code(HttpServletRequest request) {

        String account = request.getParameter("account");
        Result<Map<String, Object>> result;

        if (StringUtils.isEmpty(account)) {

            return new Result<>(1, "手机号不能为空", null);
        } else {
            Map<String, Object> map;
            try {
                map = userService.securityCode(account);
                result = new Result<>(0, "发送成功", map);

            } catch (Exception e) {
                return new Result<>(1, "发送短信异常", null);

            }
        }
        return result;
    }


    /*========================================登录业务 end=============================================================*/


    /**
     * UserSig生成
     *
     * @return
     */

    @RequestMapping(value = "/generate_sig", method = RequestMethod.POST)
    public Result<Map<String, Object>> generateSigByAccount(HttpServletRequest request) {

        Result<Map<String, Object>> rr;
        Map<String, Object> map;

        try {
            String account = request.getParameter("account");
            if (!StringUtils.isEmpty(account)) {
                map = GenerateSig.generateSigByAccount(account);
                map.put("account", request.getParameter("account"));
                rr = new Result<>(0, "ok", map);
            } else {
                rr = new Result<>(1, "account 为空", null);
            }

        } catch (Exception e) {
            rr = new Result<>(1, "", null);
        }
        return rr;
    }


    /*==============================================个人资料业务 start==================================*/
    //初始化个人资料
    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    public Result initProfile(HttpServletRequest request) {
        // 声明返回值
        Result result;
        if (request.getParameter("account") == null) {
            result = new Result(1, "帐号不能为空", null);
        } else {
            try {
                userService.editprofile(request);
                result = new Result(0, "编辑成功", null);
            } catch (ServiceException e) {
                result = new Result(1, "编辑失败", null);
            }
        }
        return result;
    }

    //查看个人资料
    @RequestMapping(value = "/view_profile", method = RequestMethod.POST)
    public Result<UserInfo> viewprofile(HttpServletRequest request) {
        // 声明返回值
        Result<UserInfo> rr;
        if (request.getParameter("account") == null) {
            rr = new Result<UserInfo>(1, "帐号不能为空", null);
        } else {
            try {
                UserInfo user = userService.viewprofile(request);
                rr = new Result<>(0, "查看成功", user);
            } catch (ServiceException e) {
                rr = new Result<>(1, "查询资料异常", null);
            }
        }
        return rr;
    }


    //查看别人的资料
    @RequestMapping("/base_info")
    @ResponseBody
    public Result<UserInfo> getBaseInfo(HttpServletRequest request) {
        // 声明返回值

        return userService.getBaseInfo(request);
    }

    @RequestMapping(value = "/edit_head", method = RequestMethod.POST)
    public Result<String> editHead(HttpServletRequest request) {

        return userService.editHead(request);
    }

    //修改个性签名
    @RequestMapping(value = "/update_sign", method = RequestMethod.POST)
    public Result updateSig(HttpServletRequest request) {

        return userService.updateSig(request);
    }


    // 用户上传相册接口

    @RequestMapping(value = "/upload_album", method = RequestMethod.POST)
    public Result<List<UserAlbum>> photoAlbum(HttpServletRequest request) {

        return userService.photoAlbum(request);
    }

    //用户获取相册接口
    @RequestMapping(value = "/get_album", method = RequestMethod.POST)
    public Result<List<UserAlbum>> getPhotoAlbum(HttpServletRequest request) {

        return userService.getPhotoAlbum(request);
    }

    // 用户删除单张图片接口接口

    @RequestMapping(value = "/delete_album", method = RequestMethod.POST)
    public Result deletePhotoAlbum(HttpServletRequest request) {

        return userService.deletePhotoAlbum(request);
    }


    @RequestMapping(value = "/vip_info", method = RequestMethod.POST)
    public Result<Map<String, Object>> getVipInfo(HttpServletRequest request) {
        Result<Map<String, Object>> rr;

        Map<String, Object> map = new HashMap<>();

        UserVip vipUser = userVipService.getVipByAccount(request.getParameter("account"));

        if (vipUser != null) {
            String fishtime = vipUser.getFishtime();
            String currentTime = DateUtil.getCurrentTime();
            long value = 0;
            try {
                value = DateUtil.getBetweenDate(fishtime, currentTime);
                if (value > 0) {//代表当前用户属于普通用户
                    rr = new Result<Map<String, Object>>(1, "", null);
                }else {//走这里才代表是VIP
                    map.put("fish_time", vipUser.getFishtime());
                    rr = new Result<Map<String, Object>>(0, "", map);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                rr = new Result<Map<String, Object>>(1, "", null);
            }

        } else {
            rr = new Result<Map<String, Object>>(1, "", null);
        }

        return rr;
    }


    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public Result<List<UserInfo>> getContact(HttpServletRequest request) {

        Result<List<UserInfo>> responseResult;

        String json = request.getParameter("contacts");


        List<String> contacts = new Gson().fromJson(json, new TypeToken<List<String>>() {
        }.getType());

        String[] strings = new String[contacts.size()];
        contacts.toArray(strings);

        List<UserInfo> contact = userService.getContact(strings);

        return new Result<>(0, "", contact);
    }


    //我浏览的人
    @RequestMapping(value = "/browse", method = RequestMethod.POST)
    public Result<List<UserInfo>> browse(HttpServletRequest request) {


        return userService.browse(request);
    }

    //别人看我的记录
    @RequestMapping(value = "/browsed", method = RequestMethod.POST)
    public Result<List<UserInfo>> browsed(HttpServletRequest request) {



        return userService.browsed(request);
    }

    /*===================================关注和粉丝 start ============================================*/
    //点击关注别人
    @RequestMapping(value = "/go_attention", method = RequestMethod.POST)
    public Result go_attention(HttpServletRequest request) {


        return userService.go_attention(request);
    }

    //查看关注
    @RequestMapping(value = "/attention", method = RequestMethod.POST)
    public Result<List<UserInfo>> attention(HttpServletRequest request) {


        return userService.attention(request);
    }


    //查看粉丝
    @RequestMapping(value = "/fans", method = RequestMethod.POST)
    public Result<List<UserInfo>> fans(HttpServletRequest request) {


        return userService.fans(request);
    }

    //是否关注了对方
    @RequestMapping(value = "/is_attention", method = RequestMethod.POST)
    public Result isAttention(HttpServletRequest request) {


        return userService.isAttention(request);
    }

    //取消关注
    @RequestMapping(value = "/cancel_attention", method = RequestMethod.POST)
    public Result cancelAttention(HttpServletRequest request) {


        return userService.cancelAttention(request);
    }

    /*===================================关注和粉丝 end ============================================*/
    /*===================================个人标签 start ============================================*/

    @RequestMapping(value = "/post_label", method = RequestMethod.POST)
    public Result postLabel(HttpServletRequest request) {


        return userService.label(request);
    }

    @RequestMapping(value = "/get_label", method = RequestMethod.POST)
    public Result<UserLabel> getLabel(HttpServletRequest request) {


        return userService.getLabel(request);
    }
    /*===================================个人标签 end ============================================*/


    /*===================================用户问答 start ============================================*/
    //获取当前用户的问答
    @RequestMapping(value = "/get_answer", method = RequestMethod.POST)
    public Result<List<UserAnswer>> getAnswer(HttpServletRequest request) {

        return userService.getAnswer(request);
    }

    //上传用户的问答
    @RequestMapping(value = "/upload_answer", method = RequestMethod.POST)
    public Result uploadAnswer(HttpServletRequest request) {

        return userService.uploadAnswer(request);
    }


    /**
     * 获取人品分
     * @param request
     * @return
     */
    @RequestMapping(value = "/human_quality", method = RequestMethod.POST)
    public Result<UserHumanQuality> humanQuality(HttpServletRequest request) {

        return userService.humanQuality(request);
    }
    /**
     * 获取当前用户的认证情况
     * @param request
     * @return
     */
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public Result<UserAuth> auth(HttpServletRequest request) {

        return userService.auth(request);
    }
    /**
     * 上传用户的认证信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/go_auth", method = RequestMethod.POST)
    public Result goAuth(HttpServletRequest request) {

        return userService.goAuth(request);
    }





    //意见反馈
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public Result report(HttpServletRequest request){
        return userService.report(request);
    }

    //举报
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public Result feedback(HttpServletRequest request){
        return userService.feedback(request);
    }

    //添加好友次数校验
    @RequestLimit(count = ZxfConstans.ADD_FRIEND_COUNT)
    @RequestMapping(value = "/add_friend", method = RequestMethod.POST)
    public Result add_Driend(HttpServletRequest request){
        if (!CommonUtils.checkPer(request)) {
            return new Result<>(0, "-1", null);//权限不足
        }
        return new Result<>(0, "可以添加", null);
    }



}
