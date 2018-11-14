package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicComment;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DynamicCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicComment record);

    int insertSelective(DynamicComment record);

    DynamicComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicComment record);

    int updateByPrimaryKey(DynamicComment record);

    Integer getDynamicCommentCount(String cid);

    List<DynamicComment> getDynamicCommentByCid(String cid);

    List<DynamicComment> selectByDynaicAccount(String dynamicAccount);
}