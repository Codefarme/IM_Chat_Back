package com.codefarme.imchat.service;


import com.codefarme.imchat.pojo.UserVip;

public interface UserVipService {
	
	/**
	 * 查看vip用户通过账号
	 * @param account
	 * @return
	 */
	UserVip getVipByAccount(String account);


	int insert(UserVip record);

	int updateByPrimaryKeySelective(UserVip record);



	
}
