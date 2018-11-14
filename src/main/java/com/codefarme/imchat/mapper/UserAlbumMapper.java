package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserAlbum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserAlbumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAlbum record);

    int insertSelective(UserAlbum record);

    UserAlbum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserAlbum record);

    int updateByPrimaryKey(UserAlbum record);

    List<UserAlbum> selectByAccount(String account);

    Integer updateByKey(Integer valueOf);
}