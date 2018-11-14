package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicCommentReply;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DynamicCommentReplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicCommentReply record);

    int insertSelective(DynamicCommentReply record);

    DynamicCommentReply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicCommentReply record);

    int updateByPrimaryKey(DynamicCommentReply record);



    List<DynamicCommentReply> getReplyBycidAndLimit(Integer id);

    List<DynamicCommentReply> getReplyBycid(Integer cid);
}