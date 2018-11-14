package com.codefarme.imchat.service;



import com.codefarme.imchat.pojo.Product;

public interface ProductService {


	
	/**
	 * 根据产品ID查询产品详情
	 * @param productId
	 * @return
	 */
	public Product getProductById(String productId);
}
