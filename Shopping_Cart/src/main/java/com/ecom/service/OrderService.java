package com.ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;

public interface OrderService {
	public void saveOrder(Integer userId, OrderRequest orderRequest) throws Exception;

	public List<ProductOrder> getAllOrders();

	public List<ProductOrder> getOrderByUser(Integer userId);

	public ProductOrder getOrderByOrderId(String orderId);

	public ProductOrder updateOrderStatus(Integer orderId, String orderStatus);

	public Page<ProductOrder> getAllOrders(Integer pageNumber, Integer pageSize);

	public Page<ProductOrder> getOrderByOrderId(String orderId, Integer pageNumber, Integer pageSize);

}