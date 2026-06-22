package com.ecom.service;

import com.ecom.model.OrderRequest;

public interface OrderService {
	public void saveOrder(Integer userId, OrderRequest orderRequest);
}