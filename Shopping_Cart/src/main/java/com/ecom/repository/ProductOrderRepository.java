package com.ecom.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
	public List<ProductOrder> findByUserId(Integer userId);

	public ProductOrder findByOrderId(String orderId);

	public Page<ProductOrder> findAll(Pageable pageableObj);

	public Page<ProductOrder> findByOrderId(String orderId, Pageable pageableObj);
}
