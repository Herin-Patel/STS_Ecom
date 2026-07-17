package com.ecom.service;

import java.util.List;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

public interface OrderService {
	public void saveOrder(Integer userId, OrderRequest orderRequest) throws Exception;

	public List<ProductOrder> getOrderByUser(Integer userId);

	public ProductOrder updateOrderStatus(Integer orderId, String orderStatus);

	public List<ProductOrder> getAllOrders();

	public ProductOrder getOrderByOrderId(String orderId);
}