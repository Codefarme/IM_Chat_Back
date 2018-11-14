package com.codefarme.imchat.controller;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.response.UserInfoResponse;
import com.codefarme.imchat.service.MakeFriendSerice;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.limit.RequestLimit;
import com.codefarme.imchat.response.UserAuthResponse;
import com.codefarme.imchat.utils.CommonUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/match")
public class MakeFriendController {
    @Resource
    private MakeFriendSerice makeFriendService;


    //在线匹配
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<List<UserInfoResponse>> match(HttpServletRequest request) {

        if (!CommonUtils.checkPer(request)) {
            return new Result<>(1, "-1", null);
        }

        Result<List<UserInfoResponse>> rr;

        if (request.getParameter("account") == null) {
            return new Result(1, "", null);
        } else {
            try {
                List<UserInfoResponse> list = makeFriendService.match(request);
                rr = new Result(0, "查看成功", list);
            } catch (ServiceException e) {
                rr = new Result(1, "", null);
            }
        }
        return rr;
    }

    //优质推荐 进来的用户必须是VIP然后 获取到的用户是需要认证通过的
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public Result<List<UserAuthResponse>> authMatch(HttpServletRequest request) {


        return makeFriendService.onlineMatch(request);
    }


    //扔瓶子
    @RequestMapping(value = "/throw_bottles", method = RequestMethod.POST)
    public Result throwbottles(HttpServletRequest request) {
        // 声明返回值

        if (!CommonUtils.checkPer(request)) {
            return new Result<>(1, "-1", null);
        }

        Result<Void> rr;
        if (request.getParameter("account") == null) {
            rr = new Result<>(1, "帐号不能为空", null);
        } else {
            try {
                makeFriendService.throwbottles(request);
                rr = new Result<>(0, "扔出一个瓶子", null);
            } catch (ServiceException e) {
                rr = new Result<Void>(1, "", null);
            }
        }
        return rr;
    }


    //捡起一个瓶子
    @RequestLimit(count = ZxfConstans.PICKUPBOTTLES)
    @RequestMapping(value = "/pick_bottles", method = RequestMethod.POST)
    public Result<Map<String, Object>> pickupbottles(HttpServletRequest request) {
        // 声明返回值
        // 声明返回值
        if (!CommonUtils.checkPer(request)) {
            return new Result<>(0, "-1", null);
        }

        Result<Map<String, Object>> rr;
        if (request.getParameter("account") == null) {
            rr = new Result<>(1, "帐号不能为空", null);
        } else {
            try {
                Map<String, Object> dBottle = makeFriendService.pickupbottles(request);
                rr = new Result<>(0, "捡起一个瓶子", dBottle);
            } catch (ServiceException e) {
                rr = new Result<>(1, "", null);
            }
        }
        return rr;
    }


    /**
     * 回复瓶子 新版本
     *
     * @return
     */

    @RequestMapping(value = "/delete_bottle", method = RequestMethod.POST)
    public Result deletebottle(HttpServletRequest request) {

        Result rr;

        try {
            makeFriendService.deletebottle(request);
            rr = new Result<>(0, "回复成功", null);
        } catch (ServiceException e) {
            rr = new Result<>(1, "", null);
        }

        return rr;
    }
}
