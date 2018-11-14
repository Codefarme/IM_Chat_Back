package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicImg;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DynamicImgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicImg record);

    int insertSelective(DynamicImg record);

    DynamicImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicImg record);

    int updateByPrimaryKey(DynamicImg record);

    List<DynamicImg> getDynamicImgBycid(String cid);
}