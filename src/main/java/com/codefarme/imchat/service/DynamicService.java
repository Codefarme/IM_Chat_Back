package com.codefarme.imchat.service;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.pojo.Dynamic;
import com.codefarme.imchat.response.CommentReplyData;
import com.codefarme.imchat.response.DynamicCommentResponse;
import com.codefarme.imchat.response.ReplyDetailBean;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface DynamicService {


    @Transactional
    void publish(HttpServletRequest request);

    Integer publishdynamicofwritten(Dynamic dynamic);

    /**
     * 查看自己的动态
     *
     * @param request
     * @return
     */
    List<Map<String, Object>> lookoneSelfDynamic(HttpServletRequest request);

    List<Map<String, Object>> lookplaza(HttpServletRequest request);

    Result<Map<String, Object>> lookOneDynamic(HttpServletRequest request);

    Result<CommentReplyData> lookplazaDetail(HttpServletRequest request);

    /**
     * 点赞
     *
     * @param request
     */
    void dynamiclike(HttpServletRequest request);

    /**
     * 评论（不包含送礼物）
     *
     * @param request
     */
    void dynamiccomment(HttpServletRequest request);


    Result dynamicReply(HttpServletRequest request);

    /*删除自己的帖子，把帖子的状态置为1*/
    Result deleteDynamic(HttpServletRequest request);

    List<Map<String, Object>> look_attention_square(HttpServletRequest request);

    Result getSelfPraise(HttpServletRequest request);

    Result getPraise(HttpServletRequest request);

    Result<List<DynamicCommentResponse>> getComment(HttpServletRequest request);

    Result<List<ReplyDetailBean>> commentDetail(HttpServletRequest request);
}
