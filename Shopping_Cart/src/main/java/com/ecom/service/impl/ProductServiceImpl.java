package com.ecom.service.impl;

import com.ecom.model.Product;
import com.ecom.service.ProductService;
import com.ecom.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepositoryObj;

	@Override
	public Product saveProduct(Product product) {
		return productRepositoryObj.save(product);
	}
}
