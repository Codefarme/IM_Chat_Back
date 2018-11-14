package com.codefarme.imchat.controller;


import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.response.DynamicCommentResponse;
import com.codefarme.imchat.service.DynamicService;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.limit.RequestLimit;
import com.codefarme.imchat.response.CommentReplyData;
import com.codefarme.imchat.response.ReplyDetailBean;
import com.codefarme.imchat.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/dynamic")
public class DynamicController {


    @Autowired
    private DynamicService dynamicService;


    /**
     * 发布动态
     *
     * @return
     */
    @RequestLimit(count = ZxfConstans.PUBLISH)
    @RequestMapping(value = "/publish_dynamic", method = RequestMethod.POST)
    public Result publish(HttpServletRequest request) {

        if (!CommonUtils.checkPer(request)) {
            return new Result(-1, "没权限", null);
        }

        Result<Void> rr;
        if (request.getParameter("account") == null) {
            rr = new Result(1, "帐号不能为空", null);
        } else {
            try {
                dynamicService.publish(request);
                rr = new Result(0, "发布成功", null);
            } catch (ServiceException e) {
                rr = new Result(1, "", null);
            }
        }
        return rr;
    }

    /**
     * 删除动态
     *
     * @return
     */
    @RequestMapping(value = "/delete_dynamic", method = RequestMethod.POST)
    public Result deleteDynamic(HttpServletRequest request) {

        return dynamicService.deleteDynamic(request);
    }

    /**
     * 查看广场
     *
     * @return
     */

    @RequestMapping(value = "/look_square", method = RequestMethod.POST)
    public Result<List<Map<String, Object>>> lookplazadynamic(HttpServletRequest request) {
        // 声明返回值
        Result<List<Map<String, Object>>> rr;
        if (request.getParameter("account") == null) {
            rr = new Result<>(1, "帐号不能为空", null);
        } else {
            try {
                List<Map<String, Object>> map = dynamicService.lookplaza(request);
                rr = new Result<>(0, "查看成功", map);
            } catch (ServiceException e) {
                rr = new Result<>(1, "", null);
            }
        }
        return rr;
    }

    /**
     * 获取自己关注的人的动态
     */
    @RequestMapping(value = "/look_attention_square", method = RequestMethod.POST)
    public Result<List<Map<String, Object>>> look_attention_square(HttpServletRequest request) {
        // 声明返回值
        Result<List<Map<String, Object>>> result;

        try {
            List<Map<String, Object>> map = dynamicService.look_attention_square(request);
            result = new Result<>(0, "查看成功", map);
        } catch (ServiceException e) {
            result = new Result<>(1, "", null);
        }

        return result;
    }

    /**
     * 查看帖子详情
     *
     * @return
     */

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Result<CommentReplyData> lookplazaDetail(HttpServletRequest request) {

        return dynamicService.lookplazaDetail(request);
    }

    /**
     * 查看广场的某一个帖子
     *
     * @return
     */

    @RequestMapping(value = "/one_dynamic", method = RequestMethod.POST)
    public Result<Map<String, Object>> lookOneDynamic(HttpServletRequest request) {

        return dynamicService.lookOneDynamic(request);
    }



    /**
     * 查看自己的动态
     *
     * @return
     */

    @RequestMapping(value = "/self_dynamic", method = RequestMethod.POST)
    public Result<List<Map<String, Object>>> lookoneselfdynamic(HttpServletRequest request) {
        // 声明返回值

        Result<List<Map<String, Object>>> rr;
        if (request.getParameter("account") == null) {
            rr = new Result<>(1, "帐号不能为空", null);
        } else {
            try {
                List<Map<String, Object>> map = dynamicService.lookoneSelfDynamic(request);
                rr = new Result<>(0, "查看成功", map);
            } catch (ServiceException e) {
                rr = new Result<>(1, "", null);
            }
        }
        return rr;
    }



    //查看评论详情
    @RequestMapping(value = "/comment_detail", method = RequestMethod.POST)
    public Result<List<ReplyDetailBean>> commentDetail(HttpServletRequest request) {
        // 声明返回值

        return dynamicService.commentDetail(request);
    }


    //点赞
    @RequestMapping(value = "/dynamic_praise", method = RequestMethod.POST)
    public Result dynamiclike(HttpServletRequest request) {
        // 声明返回值

        Result rr;
        if (request.getParameter("account") == null) {
            rr = new Result(1, "帐号不能为空", null);
        } else {
            try {
                dynamicService.dynamiclike(request);
                rr = new Result(0, "点赞成功", null);
            } catch (ServiceException e) {
                rr = new Result(1, "", null);
            }
        }
        return rr;
    }

    //获取自己点赞其他人的记录
    @RequestMapping(value = "/get_self_praise", method = RequestMethod.POST)
    public Result getSelfPraise(HttpServletRequest request) {
        // 声明返回值

        return dynamicService.getSelfPraise(request);
    }

    //获取别人点赞我的记录
    @RequestMapping(value = "/get_praise", method = RequestMethod.POST)
    public Result getPraise(HttpServletRequest request) {
        // 声明返回值

        return dynamicService.getPraise(request);
    }

    /**
     * 评论
     *
     * @return
     */

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Result dynamiccomment(HttpServletRequest request) {
        // 声明返回值

        Result result;
        if (request.getParameter("account") == null) {
            result = new Result(1, "帐号不能为空", null);
        } else {
            try {
                dynamicService.dynamiccomment(request);
                result = new Result(0, "发表成功", null);
            } catch (ServiceException e) {
                result = new Result(1, e.getMessage(), null);
            }
        }
        return result;
    }

    /**
     * 回复
     *
     * @return
     */

    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public Result reply(HttpServletRequest request) {

        return dynamicService.dynamicReply(request);
    }

    /**
     * 获取别人评论自己的记录
     *
     * @return
     */

    @RequestMapping(value = "/get_comment", method = RequestMethod.POST)
    public Result<List<DynamicCommentResponse>> getComment(HttpServletRequest request) {
        // 声明返回值


        return dynamicService.getComment(request);
    }



}
