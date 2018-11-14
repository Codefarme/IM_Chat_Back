package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicPraise;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicPraiseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicPraise record);

    int insertSelective(DynamicPraise record);

    DynamicPraise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicPraise record);

    int updateByPrimaryKey(DynamicPraise record);



    Integer getDynamicLikeCount(String cid);

    List<DynamicPraise> getDynamicLike(String cid);

    List<DynamicPraise> selectByAccount(String account);

    List<DynamicPraise> selectByDynamicAccount(String account);
}

