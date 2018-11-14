package com.codefarme.imchat.service;


import com.codefarme.imchat.pojo.Orders;
import com.codefarme.imchat.pojo.Product;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 订单操作 service
 * @author ibm
 *
 */
public interface OrdersService {


	
	/**
	 * 
	 * @Title: OrdersService.java
	 * @Package com.sihai.service
	 * @Description: 修改叮当状态，改为 支付成功，已付款; 同时新增支付流水
	 * Copyright: Copyright (c) 2017
	 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
	 * 
	 * @author sihai
	 * @date 2017年8月23日 下午9:04:35
	 * @version V1.0
	 */
	void updateOrderStatus(String orderId, String alpayFlowNum, String paidAmount);



	@Transactional(propagation= Propagation.REQUIRED)
	Map<String, Object> saveOrder(Product product, Orders order, String account) throws Exception;

	/**
	 * 获取订单
	 * @param orderId
	 * @return
	 */
	public Orders getOrderById(String orderId);

	Orders selectUserByOutTrade(String out_trade_no);
}
