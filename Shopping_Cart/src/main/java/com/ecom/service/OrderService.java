package com.ecom.service;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

public interface OrderService {
	public ProductOrder saveOrder(Integer userId, OrderRequest orderRequest);
}