package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.DynamicSound;
import org.springframework.stereotype.Component;

@Component
public interface DynamicSoundMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DynamicSound record);

    int insertSelective(DynamicSound record);

    DynamicSound selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DynamicSound record);

    int updateByPrimaryKey(DynamicSound record);

    DynamicSound getDynamicSoundBycid(String cid);
}