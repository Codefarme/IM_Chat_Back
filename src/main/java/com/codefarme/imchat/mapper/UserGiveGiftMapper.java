package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserGiveGift;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGiveGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGiveGift record);

    int insertSelective(UserGiveGift record);

    UserGiveGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserGiveGift record);

    int updateByPrimaryKey(UserGiveGift record);

    List<UserGiveGift> selectAllGiveGift(String account);

    List<UserGiveGift> selectAllReceiveGift(String account);
}