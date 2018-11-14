package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.TokenProccessor;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.mapper.*;
import com.codefarme.imchat.oss.OSUtils;
import com.codefarme.imchat.pojo.*;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.service.UserService;
import com.codefarme.imchat.service.UserVipService;
import com.codefarme.imchat.utils.CommonUtils;
import com.codefarme.imchat.utils.DateUtil;
import com.codefarme.imchat.utils.SendMessage;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserSmsMsgMapper userSmsMsgMapper;


    @Autowired
    private UserAlbumMapper userAlbumMapper;

    @Autowired
    private UserBrowseMapper userBrowseMapper;

    @Autowired
    private UserAttentionMapper userAttentionMapper;

    @Autowired
    private UserLabelMapper userLabelMapper;

    @Autowired
    private UserAnswerMapper answerMapper;

    @Autowired
    private UserHumanQualityMapper qualityMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserReportMapper userReportMapper;
    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    @Autowired
    private UserVipService vipUSerService;

    /**
     * 手机号验证码登陆
     */
    @Transactional
    @Override
    public Result<Map<String, Object>> phoneSmsLogin(HttpServletRequest request) {
        String account = request.getParameter("account");
        String code = request.getParameter("code");
        if (account == null || code == null) {
            return new Result(1, "账号或验证码不能为空", null);
        }

        //短信表
        UserSmsMsg userSmsMsg = userSmsMsgMapper.getCodeByAccountAndCode(account, code);
        Integer state = verCode(userSmsMsg);


        switch (state) {
            case 1:
                Map<String, Object> map = new HashMap<>();

                String token = TokenProccessor.getInstance().makeToken();
                if (StringUtils.isEmpty(token)) {
                    return new Result(1, "注册失败", null);
                }

                if (!checkAccountExists(account)) {//新用户
                    Integer integer = userInfoMapper.accountRegist(account, token);

                    if (integer != 1) {
                        return new Result(1, "注册失败", null);
                    }

                    insertQuality(account);

                    insertLoginTime(account);
                    map.put("token", token);
                    return new Result(0, "注册成功", map);

                } else {//老用户
                    Integer integer = userInfoMapper.updateTokenByAccount(account, token);
                    if (integer != 1) {
                        return new Result<>(1, "登录失败", null);
                    }

                    //更新登录时间和token
                    insertLoginTime(account);
                    map.put("token", token);
                    return new Result<>(0, "登陆成功", map);
                }

            case -1:
                return new Result<>(1, "验证码超时,请重新获取", null);

            case 0:
                return new Result<>(1, "验证码错误,请重新输入", null);

            default:

                return new Result<>(1, "系统异常", null);
        }

    }

    private void insertQuality(String account) {
        UserHumanQuality quality = new UserHumanQuality();
        quality.setHumanQuality(60);
        quality.setAccount(account);
        quality.setUpdateTime(DateUtil.getCurrentTime());
        qualityMapper.insert(quality);
    }

    @Override
    public Result<Map<String, Object>> qqaccountlogin(HttpServletRequest request) {
        String openid = request.getParameter("openid");
        UserInfo user = userInfoMapper.wechatLogin(openid);
        Map<String, Object> map = new HashMap<>();
        if (user != null) {

            String token = TokenProccessor.getInstance().makeToken();
            if (StringUtils.isEmpty(token)) {
                return new Result<>(1, "登录失败", null);
            }

            Integer integer = userInfoMapper.updateTokenByAccount(user.getAccount(), token);
            if (integer != 1) {
                return new Result<>(1, "登录失败", null);
            }

            map.put("account", user.getAccount());
            insertLoginTime(user.getAccount());
            map.put("token", token);
            return new Result<>(0, "登陆成功", map);

        } else {
            return new Result<>(0, "1", null);
        }
    }

    @Transactional
    @Override
    public Result<Map<String, Object>> qqaccountregist(HttpServletRequest request) {
        String openid = request.getParameter("openid");
        String account = request.getParameter("account");//帐号
        String code = request.getParameter("code");//验证码

        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(account) || StringUtils.isEmpty(code)) {
            return new Result<>(1, "账号或验证码为空", null);
        }

        UserSmsMsg securitycode = userSmsMsgMapper.getCodeByAccountAndCode(account, code);

        Integer state = verCode(securitycode);
        switch (state) {
            case 1:

                String token = TokenProccessor.getInstance().makeToken();
                if (StringUtils.isEmpty(token)) {
                    return new Result<>(1, "系统异常", null);
                }


                UserInfo user = userInfoMapper.getUserByAccount(account);

                Map<String, Object> map = new HashMap<>();

                if (user == null) {//创建新用户绑定
                    user = new UserInfo();
                    user.setAccount(account);
                    user.setQqAccount(openid);
                    Integer integer = userInfoMapper.qqRegist(user);
                    if (integer != 1) {
                        return new Result<>(1, "系统异常", null);
                    }

                    insertQuality(account);
                    insertLoginTime(account);

                    Integer i = userInfoMapper.updateTokenByAccount(account, token);
                    if (i != 1) {
                        return new Result<>(1, "绑定失败", null);
                    }
                    map.put("token", token);
                    return new Result<>(0, "绑定成功", map);

                }


                if (StringUtils.isEmpty(user.getQqAccount())) {//此账号未绑定QQ，进行绑定操作
                    Integer integer = userInfoMapper.updateQQAccount(openid, account);
                    if (integer != 1) {
                        return new Result<>(1, "绑定失败", null);
                    }
                    insertLoginTime(account);

                    Integer i = userInfoMapper.updateTokenByAccount(account, token);
                    if (i != 1) {
                        return new Result<>(1, "绑定失败", null);
                    }

                    map.put("token", token);
                    return new Result<>(0, "绑定成功", map);
                }

                return new Result<>(1, "该手机号已经绑定了QQ号", null);


            case -1:
                return new Result<>(1, "验证码超时,请重新获取", null);

            case 0:
                return new Result<>(1, "验证码错误,请重新输入", null);

            default:

                return new Result<>(1, "系统异常", null);
        }
    }

    /**
     * 发送验证码
     */
    @Transactional
    public Map<String, Object> securityCode(String account) {


        Map<String, Object> map = new HashMap<>();

        UserSmsMsg securitycode = userSmsMsgMapper.getSMSMesage(account);
        if (securitycode != null) {
            int id = securitycode.getId();
            int todayCount = securitycode.getTodyCount();
            String dateCreated = (String) securitycode.getTime();
            //判断时间间隔
            long createSecon = DateUtil.getSecondByTime(dateCreated, "yyyyMMddHHmmss");  //转化为秒
            long nowSecon = System.currentTimeMillis() / 1000;
            long different = nowSecon - createSecon;
            if (different < 60) {
                map.put(ZxfConstans.DATA_TYPE, 4);
                map.put(ZxfConstans.DATA_MSM, "短信间隔时间小于60秒");

                return map;
            }
            //判断发送数量
            boolean isCurrentDay = false;
            try {
                isCurrentDay = DateUtil.isCurrentDay(dateCreated, "yyyyMMddHHmmss");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isCurrentDay) {
                if (todayCount >= 10) {
                    map.put(ZxfConstans.DATA_TYPE, 5);
                    map.put(ZxfConstans.DATA_MSM, "今日获取验证码次数，已达上限，请明天再获取");

                    return map;
                }
            }
            String number = CommonUtils.getnumberString(6);
            String content = "您好，您的验证码是" + number;
            String[] phoneNumbers = {account};

            Integer integer = SendMessage.sendSMS(number, phoneNumbers);
            System.err.println("发送短信的返回值" + integer);
            if (integer == 0) {
                String newDateCreated = DateUtil.getCurrentTimeS();
                //更新短信信息
                if (isCurrentDay) {  //今天已经发送过
                    securitycode.setTodyCount(todayCount + 1);
                    securitycode.setTime(newDateCreated);
                    securitycode.setSecurityCode(number);
                    //修改数据库
                    Integer integer2 = userSmsMsgMapper.updateByPrimaryKey(securitycode);
                    if (integer2 != 1) {
                        throw new ServiceException("保存失败请重新发送");
                    }
                } else {  //不是今天
                    securitycode.setTodyCount(1);
                    securitycode.setTime(newDateCreated);
                    securitycode.setSecurityCode(number);
                    //修改数据库
                    Integer integer2 = userSmsMsgMapper.updateByPrimaryKey(securitycode);
                    if (integer2 != 1) {
                        throw new ServiceException("保存失败请重新发送");
                    }
                }

            } else {
                throw new ServiceException("发送短信失败");
            }

        } else {
            String number = CommonUtils.getnumberString(6);

            String[] phoneNumbers = {account};
            Integer integer = SendMessage.sendSMS(number, phoneNumbers);
            if (integer == 0) {
                String newDateCreated = DateUtil.getCurrentTimeS();
                //将数据写入
                UserSmsMsg userSmsMsg = new UserSmsMsg();
                userSmsMsg.setAccount(account);
                userSmsMsg.setTodyCount(1);
                userSmsMsg.setTime(newDateCreated);
                userSmsMsg.setSecurityCode(number);
                userSmsMsg.setTimeLimit("5");
                //修改数据库
                Integer integer2 = userSmsMsgMapper.insert(userSmsMsg);
                if (integer2 != 1) {
                    throw new ServiceException("保存失败请重新发送");
                }
            } else {
                throw new ServiceException("发送短信失败");
            }

        }

        return null;
    }

    @Override
    public void editprofile(HttpServletRequest request) {

        String account = request.getParameter("account");
        String username = request.getParameter("username");
        String sex = request.getParameter("sex");
        String age = request.getParameter("age");
        String height = request.getParameter("height");
        String weight = request.getParameter("weight");
        String province = request.getParameter("province");
        String ethnicity = request.getParameter("ethnicity");
        String city = request.getParameter("city");
        String sign = request.getParameter("sign");

        UserInfo user = new UserInfo();
        user.setAccount(account);
        user.setUsername(username);
        user.setSex(sex);
        user.setAge(age);
        user.setHeight(height);
        user.setWeight(weight);
        user.setProvince(province);
        user.setCity(city);
        if (!StringUtils.isEmpty(ethnicity)) {//族群

            user.setEthnicity(Integer.valueOf(ethnicity));
        }
        user.setStatus(1);
        user.setSign(sign);
        uploadPic(request, user);
        Integer integer = userInfoMapper.updateUserByAccount(user);
        if (integer != 1) {
            throw new ServiceException("保存失败");
        }

    }

    @Override
    public Result<String> editHead(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "帐号不能为空", null);
        }

        UserInfo user = new UserInfo();
        user.setAccount(account);

        uploadPic(request, user);
        Integer integer = userInfoMapper.updateAvatarByAccount(user);
        if (integer != 1) {
            return new Result(1, "编辑失败", null);
        }


        return new Result(0, "编辑成功", OSUtils.getUrl(user.getAvatar()));
    }

    @Override
    public UserInfo viewprofile(HttpServletRequest request) {
        String account = request.getParameter("account");
        UserInfo user = userInfoMapper.getUserByAccount(account);
        if (user != null) {
            user.setToken(null);
            user.setQqAccount(null);
            user.setWechat(null);
            if (user.getAvatar() != null) {
                user.setAvatar(OSUtils.getUrl(user.getAvatar()));
            }
        }

        return user;
    }

    @Override
    public List<UserInfo> getlistuserbycity(String city, String offset, String count) {
        return null;
    }

    @Override
    public List<UserInfo> getContact(String[] accounts) {
        if (accounts.length == 0) {
            throw new ServiceException("非法请求");
        }

        List<UserInfo> contactList = userInfoMapper.getContact(accounts);

        //重新设置联系人头像地址
        for (UserInfo contact : contactList) {
            contact.setAvatar(OSUtils.getUrl(contact.getAvatar()));
        }

        return contactList;

    }

    @Override
    public Result selectUserStatus(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "账号为空", null);
        }
        Integer status = userInfoMapper.selectUserStatus(account);
        if (null == status || status == 0) {//未初始化
            return new Result<>(0, "1", null);
        }

        return new Result<>(0, "", null);
    }

    @Override
    public Result<List<UserAlbum>> photoAlbum(HttpServletRequest request) {
        String account = request.getParameter("account");

        if (account == null) {
            throw new ServiceException("账号不能为空");
        }

        //图片存储
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = null;
            try {
                multipartRequest = (MultipartHttpServletRequest) request;
            } catch (Exception e) {

            }
            List<MultipartFile> img = new LinkedList<MultipartFile>();
            img = multipartRequest.getFiles("picture");

            for (MultipartFile pictureImg : img) {

                String key = null;
                try {
                    if (null != pictureImg && pictureImg.getBytes().length > 0) {
                        key = OSUtils.upload(pictureImg.getBytes(), true, account);//七牛上传图片

                        UserAlbum userPhotoAlbum = new UserAlbum();
                        userPhotoAlbum.setAccount(account);
                        userPhotoAlbum.setPicurl(key);
                        userPhotoAlbum.setStatus(1);//1代表可以使用
                        Integer integer = userAlbumMapper.insertSelective(userPhotoAlbum);
                        if (integer != 1) {
                            throw new ServiceException("发布失败");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        //获取该账号下的相册
        List<UserAlbum> photoAlbumList = userAlbumMapper.selectByAccount(account);
        for (UserAlbum userPhotoAlbum : photoAlbumList) {
            userPhotoAlbum.setPicurl(OSUtils.getUrl(userPhotoAlbum.getPicurl()));
        }
        return new Result<>(0, "上传成功", photoAlbumList);
    }

    @Override
    public Result<List<UserAlbum>> getPhotoAlbum(HttpServletRequest request) {

        String account = request.getParameter("account");

        if (account == null) {
            return new Result<>(1, "", null);
        }


        //获取该账号下的相册
        List<UserAlbum> photoAlbumList = userAlbumMapper.selectByAccount(account);
        for (UserAlbum userPhotoAlbum : photoAlbumList) {
            userPhotoAlbum.setPicurl(OSUtils.getUrl(userPhotoAlbum.getPicurl()));
        }

        return new Result<>(0, "获取成功", photoAlbumList);
    }

    @Override
    public Result<List<UserAlbum>> deletePhotoAlbum(HttpServletRequest request) {

        String account = request.getParameter("account");
        String id = request.getParameter("id");//图片id

        if (account == null) {
            return new Result(1, "", null);
        }


        //获取该账号下的相册
        Integer i = userAlbumMapper.updateByKey(Integer.valueOf(id));

        return new Result(0, "删除成功", null);

    }

    @Override
    public Result updateUserExtInfo(HttpServletRequest request) {
        return null;
    }

    @Override
    public void test() {

    }

    @Override
    public Result<Map<String, Object>> checkToken(String account, String token) {
        Integer integer = userInfoMapper.checkToken(account, token);

        System.out.println("checkToken:" + integer);

        if (integer < 1) {
            return new Result(1, "", null);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("account", account);
        map.put("token", token);

        return new Result(0, "登录成功", map);
    }

    @Override
    public Result<UserInfo> getBaseInfo(HttpServletRequest request) {
        String browAccount = request.getParameter("account");
        String myAccount = request.getParameter("myAccount");
        if (StringUtils.isEmpty(browAccount)) {
            return new Result<>(1, "", null);
        }
        UserInfo user = userInfoMapper.getBaseInfo(browAccount);
        if (user != null) {
            user.setToken(null);
            if (user.getAvatar() != null) {
                user.setAvatar(OSUtils.getUrl(user.getAvatar()));
            }
            user.setAccount(browAccount);


            if (!StringUtils.isEmpty(myAccount) && !browAccount.equals(myAccount)) {//不是自己看自己，就增加浏览足迹


                //查询有没有一样的，有一样的就更新浏览时间
                UserBrowse userBrowse = userBrowseMapper.selectByAccountAndBrowseAccount(myAccount, browAccount);
                if (userBrowse != null) {
                    userBrowse.setBrowseTime(DateUtil.getCurrentTime());
                    userBrowseMapper.updateByPrimaryKeySelective(userBrowse);
                } else {
                    UserBrowse browse = new UserBrowse();
                    browse.setAccount(myAccount);
                    browse.setBrowseAccount(browAccount);
                    browse.setBrowseTime(DateUtil.getCurrentTime());
                    userBrowseMapper.insert(browse);
                }

            }

            return new Result<>(0, "ok", user);
        }
        return new Result<>(1, "", null);
    }

    @Override
    public Result updateSig(HttpServletRequest request) {
        String account = request.getParameter("account");
        String sig = request.getParameter("sig");
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(sig)) {
            return new Result<>(1, "", null);
        }

        Integer integer = userInfoMapper.updateSig(account, sig);
        if (integer != 1) {
            return new Result<>(1, "", null);
        }

        return new Result<>(0, "ok", null);
    }


    //我的浏览足迹
    @Override
    public Result<List<UserInfo>> browse(HttpServletRequest request) {

        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少参数", null);
        }


        Integer isHavePer = checkMatchPer(account);


        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<UserBrowse> browses = userBrowseMapper.selectByAccount(account);
        List<UserInfo> userInfos = new ArrayList<>();
        for (UserBrowse userBrowse : browses) {
            UserInfo userInfo = userInfoMapper.getBaseInfo(userBrowse.getBrowseAccount());
            String time = DateUtil.timeDifference(userBrowse.getBrowseTime());
            if (time == null) {
                time = "刚刚";
            }
            userInfo.setAvatar(OSUtils.getUrl(userInfo.getAvatar()));
            userInfo.setLoginTime(time);//这里的time相当于别人的时间
            userInfos.add(userInfo);
        }
        return new Result<>(0, isHavePer + "", userInfos);
    }


    //别人看我的记录
    @Override
    public Result<List<UserInfo>> browsed(HttpServletRequest request) {
        String account = request.getParameter("account");

        if (StringUtils.isEmpty(account)) {
            return new Result<>(1, "缺少参数", null);
        }

        Integer isHavePer = checkMatchPer(account);//返回给前端


        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<UserBrowse> browses = userBrowseMapper.selectByBrowseAccount(account);
        List<UserInfo> userInfos = new ArrayList<>();

        for (UserBrowse userBrowse : browses) {
            UserInfo userInfo = userInfoMapper.getBaseInfo(userBrowse.getAccount());
            String time = DateUtil.timeDifference(userBrowse.getBrowseTime());
            if (time == null) {
                time = "刚刚";
            }
            userInfo.setAvatar(OSUtils.getUrl(userInfo.getAvatar()));
            userInfo.setLoginTime(time);//这里的time相当于别人的时间
            userInfos.add(userInfo);
        }

        return new Result<>(0, isHavePer + "", userInfos);
    }


    @Override
    public Result go_attention(HttpServletRequest request) {
        String account = request.getParameter("account");//我将要关注的账号
        String myAccount = request.getParameter("myAccount");//我的账号

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(myAccount) || account.equals(myAccount)) {
            return new Result(1, "", null);
        }

        UserAttention attention = new UserAttention();
        attention.setAccount(myAccount);
        attention.setAttentionAccount(account);
        attention.setAttentionTime(DateUtil.getCurrentTime());

        userAttentionMapper.insert(attention);

        return new Result(0, "", null);
    }


    @Override
    public Result<List<UserInfo>> attention(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }
        List<UserAttention> attentions = userAttentionMapper.selectByAccount(account);
        List<UserInfo> userInfos = new ArrayList<>();
        getAttentionUserInfo(attentions, userInfos);
        return new Result(0, "", userInfos);
    }

    @Override
    public Result isAttention(HttpServletRequest request) {
        String account = request.getParameter("account");//我自己
        String attentionAccount = request.getParameter("attentionAccount");//关注的账号
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(attentionAccount)) {
            return new Result(1, "", null);
        }
        Integer num = userAttentionMapper.selectByAccountAndMyAccount(account, attentionAccount);
        if (num > 0) {
            return new Result(0, "true", null);
        }

        return new Result(0, "false", null);
    }

    @Override
    public Result cancelAttention(HttpServletRequest request) {
        String account = request.getParameter("account");//我自己
        String attentionAccount = request.getParameter("attentionAccount");//需要取消关注的账号
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(attentionAccount)) {
            return new Result(1, "", null);
        }
        userAttentionMapper.deleteByAccountAndMyAccount(account, attentionAccount);

        return new Result(0, "true", null);

    }

    @Override
    public Result<List<UserInfo>> fans(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }
        List<UserAttention> attentions = userAttentionMapper.selectFansByAccount(account);
        List<UserInfo> userInfos = new ArrayList<>();
        geFansUserInfo(attentions, userInfos);
        return new Result(0, "", userInfos);
    }

    private void getAttentionUserInfo(List<UserAttention> attentions, List<UserInfo> userInfos) {
        for (UserAttention userAttention : attentions) {
            UserInfo userInfo = userInfoMapper.getBaseInfo(userAttention.getAttentionAccount());
            String time = DateUtil.timeDifference(userAttention.getAttentionTime());
            if (time == null) {
                time = "刚刚";
            }
            userInfo.setAvatar(OSUtils.getUrl(userInfo.getAvatar()));
            userInfo.setLoginTime(time);//这里的time相当于别人的时间
            userInfos.add(userInfo);
        }
    }

    private void geFansUserInfo(List<UserAttention> attentions, List<UserInfo> userInfos) {
        for (UserAttention userAttention : attentions) {
            UserInfo userInfo = userInfoMapper.getBaseInfo(userAttention.getAccount());
            String time = DateUtil.timeDifference(userAttention.getAttentionTime());
            if (time == null) {
                time = "刚刚";
            }
            userInfo.setAvatar(OSUtils.getUrl(userInfo.getAvatar()));
            userInfo.setLoginTime(time);//这里的time相当于别人的时间
            userInfos.add(userInfo);
        }
    }


    @Override
    public Result label(HttpServletRequest request) {
        String account = request.getParameter("account");
        String labelJson = request.getParameter("label");

        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(labelJson)) {
            return new Result(1, "", null);
        }

        List<String> labelList = new Gson().fromJson(labelJson, new TypeToken<List<String>>() {
        }.getType());

        if (labelList != null || labelList.size() > 0) {
            String[] array = labelList.toArray(new String[labelList.size()]);
            String s = Arrays.toString(array);
            //
            UserLabel userLabel = userLabelMapper.selectByAccount(account);
            if (userLabel == null) {
                userLabel = new UserLabel();
                userLabel.setAccount(account);
                userLabel.setLabel(s);
                userLabel.setAddTime(DateUtil.getCurrentTime());

                userLabelMapper.insert(userLabel);

            } else {
                userLabel.setLabel(s);
                userLabel.setAddTime(DateUtil.getCurrentTime());
                userLabelMapper.updateByPrimaryKey(userLabel);
            }

            return new Result(0, "", null);
        }


        return new Result(1, "", null);

    }

    @Override
    public Result<UserLabel> getLabel(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }
        UserLabel userLabel = userLabelMapper.selectByAccount(account);


        return new Result(0, "", userLabel);
    }

    /*====================================用户问答======================================================*/

    //获取用户设置的问答
    @Override
    public Result<List<UserAnswer>> getAnswer(HttpServletRequest request) {
        String account = request.getParameter("account");
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }

        List<UserAnswer> answers = answerMapper.selectByAccount(account);
        return new Result(0, "", answers);
    }

    //更新用户设置的问答
    @Override
    public Result uploadAnswer(HttpServletRequest request) {
        String account = request.getParameter("account");
        String answersJson = request.getParameter("answers");
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(answersJson)) {
            return new Result(1, "", null);
        }

        List<UserAnswer> answers = new Gson().fromJson(answersJson, new TypeToken<List<UserAnswer>>() {
        }.getType());
        if (answers != null && !answers.isEmpty()) {
            for (UserAnswer answer : answers) {

                UserAnswer userAnswer = answerMapper.selectByAccountAndSysId(account, answer.getSysId());

                if (userAnswer != null) {//存在就更新
                    userAnswer.setAnswer(answer.getAnswer());
                    userAnswer.setQuestion(answer.getQuestion());
                    userAnswer.setSort(answer.getSort());
                    userAnswer.setAddtime(DateUtil.getCurrentTime());
                    answerMapper.updateByPrimaryKey(answer);

                } else {//不存在插入
                    answer.setAddtime(DateUtil.getCurrentTime());
                    answerMapper.insert(answer);
                }
            }
        }

        return new Result(0, "", null);
    }

    //获取人品分
    @Override
    public Result<UserHumanQuality> humanQuality(HttpServletRequest request) {
        String account = request.getParameter("account");

        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }

        UserHumanQuality userHumanQuality = qualityMapper.selectByAccount(account);

        return new Result(0, "", userHumanQuality);
    }


    //获取认证
    @Override
    public Result<UserAuth> auth(HttpServletRequest request) {
        String account = request.getParameter("account");

        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }

        UserAuth userAuth = userAuthMapper.selectByAccount(account);
        if (userAuth == null) {
            return new Result(0, "", null);
        }
        userAuth.setAuthAvatar(OSUtils.getUrl(userAuth.getAuthAvatar()));
        userAuth.setAuthVideo(OSUtils.getUrl(userAuth.getAuthVideo()));
        userAuth.setAuthVideoThumb(OSUtils.getUrl(userAuth.getAuthVideoThumb()));

        return new Result(0, "", userAuth);
    }

    //上传认证
    @Transactional
    @Override
    public Result goAuth(HttpServletRequest request) {
        String account = request.getParameter("account");

        if (StringUtils.isEmpty(account)) {
            return new Result(1, "", null);
        }


        //取文件
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = null;

            try {
                multipartRequest = (MultipartHttpServletRequest) request;
            } catch (Exception e) {

            }

            //查询当前用户的
            boolean isInsert = false;
            UserAuth userAuth = userAuthMapper.selectByAccount(account);
            if (userAuth == null) {
                isInsert = true;
                userAuth = new UserAuth();
            }

            //取图片文件
            List<MultipartFile> authAvatar = new LinkedList<MultipartFile>();
            authAvatar = multipartRequest.getFiles("authAvatar");
            if (authAvatar.size() > 0) {
                for (MultipartFile pictureImg : authAvatar) {

                    String key = null;
                    try {
                        if (null != pictureImg && pictureImg.getBytes().length > 0) {

                            key = OSUtils.upload(pictureImg.getBytes(), true, account);//阿里云

                            userAuth.setAccount(account);
                            userAuth.setAuthAvatar(key);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            //取认证视频文件
            List<MultipartFile> authVideo = new LinkedList<MultipartFile>();
            authVideo = multipartRequest.getFiles("authVideo");
            if (authVideo.size() > 0) {
                for (MultipartFile videoFile : authVideo) {

                    String key = null;
                    try {
                        if (null != authVideo && videoFile.getBytes().length > 0) {
                            key = OSUtils.upload(videoFile.getBytes(), false, account);

                            userAuth.setAuthVideo(key);
                            //找到视频7s处的内容，输出为jpg。 OSS视频截帧
                            userAuth.setAuthVideoThumb(key + "?x-oss-process=video/snapshot,t_7000,f_jpg,w_600,h_400,m_fast");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            userAuth.setAddTime(DateUtil.getCurrentTime());
            userAuth.setStatus(0);
            if (isInsert) {//不存在就插入 存在就更新
                userAuthMapper.insertSelective(userAuth);
            } else {
                userAuthMapper.updateByPrimaryKeySelective(userAuth);
            }
        }


        return new Result(0, "", null);
    }


    //举报
    @Override
    public Result report(HttpServletRequest request) {
        String reportJson = request.getParameter("report");
        UserReport userReport = new Gson().fromJson(reportJson, UserReport.class);
        userReport.setAddTime(DateUtil.getCurrentTime());
        userReport.setStatus(0);//待处理
        userReportMapper.insertSelective(userReport);
        return new Result(0, "", null);
    }

    //意见反馈

    @Override
    public Result feedback(HttpServletRequest request) {
        String feedbackJson = request.getParameter("feedback");
        UserFeedback userFeedback = new Gson().fromJson(feedbackJson, UserFeedback.class);
        userFeedback.setAddTime(DateUtil.getCurrentTime());
        userFeedback.setStatus(0);//待处理
        userFeedbackMapper.insertSelective(userFeedback);
        return new Result(0, "", null);
    }


    //检查手机号是否已经存在在平台上
    @Override
    public boolean checkAccountExists(String account) {
        return userInfoMapper.checkAccountExists(account) > 0;
    }

    //校验用户输入的验证码是否正确或超时
    private Integer verCode(UserSmsMsg userSmsMsg) {
        if (userSmsMsg != null) {
            String time = userSmsMsg.getTime();
            //判断时间间隔
            long createSecon = DateUtil.getSecondByTime(time, "yyyyMMddHHmmss");  //转化为秒
            long nowSecon = System.currentTimeMillis() / 1000;
            long different = nowSecon - createSecon;
            if (different > 60) {//验证码超时
                return -1;
            }
        } else {//验证码错误，从数据库获取不到对应账号的验证码

            return 0;
        }
        return 1;
    }

    //更新登录时间
    private void insertLoginTime(String account) {
        String loginTime = DateUtil.getCurrentTime();
        Integer integer = userInfoMapper.updateLoginTime(account, loginTime);

    }

    private void uploadPic(HttpServletRequest request, UserInfo user) {
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = null;
            try {
                multipartRequest = (MultipartHttpServletRequest) request;
            } catch (Exception e) {

            }
            List<MultipartFile> img = new LinkedList<MultipartFile>();
            img = multipartRequest.getFiles("headimgurl");
            System.out.println(img);
            for (MultipartFile pictureImg : img) {
                String key = null;
                try {
                    if (null != pictureImg && pictureImg.getBytes().length > 0) {
                        key = OSUtils.upload(pictureImg.getBytes(), true, user.getAccount());//OSS上传图片
                        user.setAvatar(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //检查用户是否有权限
    //检查用户是否是VIP
    private Integer checkMatchPer(String account) {
        //检查是不是VIP
        UserVip userVip = vipUSerService.getVipByAccount(account);
        if (userVip != null) {
            String fishtime = userVip.getFishtime();//VIP结束时间
            String end = DateUtil.getCurrentDate();//当前时间
            try {
                if (!DateUtil.compare(fishtime, end)) {
                    //会员没有过期
                    return ZxfConstans.IS_VIP;
                } else {
                    return ZxfConstans.NOT_VIP;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return ZxfConstans.NOT_VIP;
        } else {
            return ZxfConstans.NOT_VIP;
        }
    }
}
