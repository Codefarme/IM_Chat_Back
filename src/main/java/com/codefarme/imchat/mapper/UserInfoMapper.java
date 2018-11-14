package com.codefarme.imchat.mapper;

import com.codefarme.imchat.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    

    Integer updateLoginTime(@Param("account")String account, @Param("loginTime")String loginTime);

    Integer checkAccountExists(String account);


    Integer accountRegist(@Param("account")String account, @Param("token")String token);

    Integer updateTokenByAccount(@Param("account")String account, @Param("token")String token);

    Integer selectUserStatus(String account);

    UserInfo wechatLogin(String openid);

    Integer updateUserByAccount(UserInfo user);

    UserInfo getUserByAccount(String account);

    Integer qqRegist(UserInfo user);

    Integer updateQQAccount(@Param("openid")String openid, @Param("account")String account);

    Integer updateSig(@Param("account")String account, @Param("sig")String sig);

    Integer updateAvatarByAccount(UserInfo user);

    UserInfo getBaseInfo(String account);

    List<UserInfo> getUserBySex(@Param("sex")String sex);

    List<UserInfo> selectUser();

    List<UserInfo> getContact(String[] accounts);

    Integer checkToken(@Param("account") String account, @Param("token") String token);
}