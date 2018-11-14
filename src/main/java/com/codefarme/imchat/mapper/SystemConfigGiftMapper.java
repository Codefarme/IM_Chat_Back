package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.SystemConfigGift;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SystemConfigGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemConfigGift record);

    int insertSelective(SystemConfigGift record);

    SystemConfigGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemConfigGift record);

    int updateByPrimaryKey(SystemConfigGift record);

    List<SystemConfigGift> selectAllConfigGift();
}