package com.codefarme.imchat.service.impl;


import com.codefarme.imchat.mapper.ProductMapper;
import com.codefarme.imchat.service.ProductService;
import com.codefarme.imchat.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductMapper productMapper;



	@Override
	public Product getProductById(String productId) {

		return productMapper.selectByPrimaryKey(Integer.valueOf(productId));
	}

}
