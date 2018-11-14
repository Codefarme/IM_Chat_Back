package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.mapper.BottlesMapper;
import com.codefarme.imchat.mapper.UserAuthMapper;
import com.codefarme.imchat.mapper.UserInfoMapper;
import com.codefarme.imchat.service.MakeFriendSerice;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.service.UserService;
import com.codefarme.imchat.service.UserVipService;
import com.github.pagehelper.PageHelper;
import com.codefarme.imchat.oss.OSUtils;
import com.codefarme.imchat.pojo.Bottles;
import com.codefarme.imchat.pojo.UserInfo;
import com.codefarme.imchat.pojo.UserVip;
import com.codefarme.imchat.response.UserAuthResponse;
import com.codefarme.imchat.response.UserInfoResponse;
import com.codefarme.imchat.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

@Service("makeFriendService")
public class MakeFriendServiceImpl implements MakeFriendSerice {
    @Resource
    private BottlesMapper bottlesMapper;
    @Resource
    private UserInfoMapper userMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserVipService vipUSerService;


    @Autowired
    private UserAuthMapper userAuthMapper;


    /**
     * VIP才可以查看 里面的照片都是经过实名认证的
     *
     * @param request
     * @return
     */
    @Override
    public Result<List<UserAuthResponse>> onlineMatch(HttpServletRequest request) {
        String account = request.getParameter("account"); //通过账号更新用户的登录时间
        if (StringUtils.isEmpty(account)) {//如果账号为空
            return new Result<>(1, "", null);
        }

        //查看优质用户只需要是VIP即可
        Integer isHavePer = checkMatchPer(account);

        String sex = request.getParameter("sex");
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }
        //String time = request.getParameter("time");

        insertloginfishtime(account);//更新用户的登录时间


        List<UserAuthResponse> list;
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));


        sex="";//TODO 测试代码
        if (sex.equals("1")||sex.equals("2")) {
            list = userAuthMapper.selectAuthUserSex(sex);
        } else {//代表查看所有性别的用户
            list = userAuthMapper.selectAuthUser();
        }

        if(list==null){
            return new Result<>(0, "", null);
        }

        for (UserAuthResponse authResponse : list) {

            if (authResponse.getAccount().equals(account)) {//获取到的是自己直接下次循环
                continue;
            }

            //这里的loginfishtime 是用户的活跃时间
            String loginfishtime = authResponse.getLoginTime();
            String addtime = DateUtil.timeDifference(loginfishtime);
            if (addtime == null) {
                addtime = "刚刚";
            }
            authResponse.setLoginTime(addtime);
            authResponse.setAvatar(OSUtils.getUrl(authResponse.getAvatar()));//设置头像Url

            authResponse.setAuthAvatar(OSUtils.getUrl(authResponse.getAuthAvatar()));//设置认证头像Url
            authResponse.setAuthVideo(OSUtils.getUrl(authResponse.getAuthVideo()));
            authResponse.setAuthVideoThumb(OSUtils.getUrl(authResponse.getAuthVideoThumb()));


        }
        return new Result<>(0, isHavePer+"", list);
    }

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
                }else {
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


    /**
     * 在线匹配(列表)
     *
     * @param request
     * @return
     */
    @Override
    public List<UserInfoResponse> match(HttpServletRequest request) {

        List<UserInfo> list = new ArrayList<>();
        String account = request.getParameter("account"); //通过账号更新用户的登录时间
        String sex = request.getParameter("sex");

        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }

        insertloginfishtime(account);//更新用户的登录时间
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<UserInfo> users;
        if (sex.equals("1") || sex.equals("2")) {
            users = userMapper.getUserBySex(sex);
        } else {//代表查看所有性别的用户
            users = userMapper.selectUser();
        }

        List<UserInfoResponse> responses = new ArrayList<>();
        for (UserInfo user : users) {

            if (user.getAccount().equals(account)) {//获取到的是自己直接下次循环
                continue;
            }

            //这里的loginfishtime 是用户的活跃时间
            String loginfishtime = user.getLoginTime();
            String addtime = DateUtil.timeDifference(loginfishtime);
            if (addtime == null) {
                addtime = "刚刚";
            }
            user.setLoginTime(addtime);

            user.setAvatar(OSUtils.getUrl(user.getAvatar()));//设置头像Url
            user.setToken(null);

            UserInfoResponse userInfoResponse = new UserInfoResponse();
            userInfoResponse.userInfo = user;

            //查询用户是不是VIP
            Integer isHavePer = checkMatchPer(user.getAccount());
            if(isHavePer==ZxfConstans.NOT_VIP){
                userInfoResponse.isVip = false;
            }else {
                userInfoResponse.isVip = true;
            }

            responses.add(userInfoResponse);
        }
        return responses;
    }


    /**
     * 在线匹配
     */
    /*@Override
    @Deprecated
    public List<Object> authMatch(HttpServletRequest request) {
        List<Object> list = new ArrayList<>();
        String account = request.getParameter("account");
        String sex = request.getParameter("sex");
        String time = request.getParameter("time");
        String lookvip = request.getParameter("lookvip");//1所有的，2只看会员
        List<User> users = userMapper.getuserbysex(sex);
        for (User user : users) {
            if (time != null) {
                String loginfishtime = user.getLoginfishtime();
                //判断时间间隔
                long createSecon = DateUtil.getSecondByTime(loginfishtime, "yyyyMMddHHmmss");  //转化为秒
                long nowSecon = System.currentTimeMillis() / 1000;
                long different = nowSecon - createSecon;
                if (different < 60 * Integer.parseInt(time)) {
                    //时间满足
                    if ("2".equals(lookvip)) {
                        //只看会员
                        VipUser vipUser = vipUSerService.getvipuserbyaccount(user.getAccount());
                        if (vipUser != null) {
                            String fishtime = vipUser.getFishtime();
                            String end = DateUtil.getCurrentDate();
                            try {
                                if (!DateUtil.compare(fishtime, end)) {
                                    //会员没有过期
                                    list.add(user);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //看所有
                        list.add(user);
                    }
                }
            }

        }
        return list;
    }*/

    /**
     * 扔瓶子
     */
    @Override
    @Transactional
    public void throwbottles(HttpServletRequest request) {
        String account = request.getParameter("account");
        String text = request.getParameter("text");
        if (text == null) {
            throw new ServiceException("内容不能为空");
        }
        String addtime = DateUtil.getCurrentTime();
        Bottles driftBottle = new Bottles();
        driftBottle.setAccount(account);
        driftBottle.setAddtime(addtime);
        driftBottle.setBottletext(text);
        driftBottle.setStatus("0");
        Integer integer = bottlesMapper.insert(driftBottle);
        if (integer != 1) {
            throw new ServiceException("扔出瓶子失败");
        }
    }


    /**
     * 拾起一个瓶子
     */
    @Override
    @Transactional
    public Map<String, Object> pickupbottles(HttpServletRequest request) {
        String account = request.getParameter("account");


        List<Integer> list = bottlesMapper.getMyBottles(account);


        Integer sum = null;
        if (list.size() != 0) {
            sum = bottlesMapper.getBottlesNum(list);
        } else {
            sum = bottlesMapper.getBottlesNum0(list);
        }
        if (sum > 0) {//代表现在的瓶子当中 没有我的瓶子
            Integer integer = new Random().nextInt(sum);
            Bottles db;
            if (list.size() != 0) {
                db = bottlesMapper.getOneBottle(list, integer);//获取除了自己之外随机的一个瓶子
            } else {
                db = bottlesMapper.getOneBottle0(list, integer);
            }
            if (db == null) {
                throw new ServiceException("没有瓶子了");
            }


            Map<String, Object> map = new HashMap<>();
            map.put("id", db.getId());
            map.put("account", db.getAccount());
            map.put("bottletext", db.getBottletext());
            map.put("addtime", db.getAddtime());
            UserInfo user = userMapper.getUserByAccount(db.getAccount());
            map.put("headimg", OSUtils.getUrl(user.getAvatar()));
            map.put("username", user.getUsername());
            return map;
        } else {
            throw new ServiceException("没有瓶子了");
        }
    }


    /**
     * 回复瓶子 //TODO 把瓶子的状态修改为已回复
     */
    @Override
    public void deletebottle(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));

        bottlesMapper.deleteBottle(id);
        System.out.println("id:" + id + "	的瓶子被删除了");
    }

    /*
     * 登录的时间
     */
    private void insertloginfishtime(String account) {
        String loginfishtime = DateUtil.getCurrentTime();
        Integer integer = userMapper.updateLoginTime(account, loginfishtime);
        if (integer != 1) {
            throw new ServiceException("登录失败");
        }
    }


}
