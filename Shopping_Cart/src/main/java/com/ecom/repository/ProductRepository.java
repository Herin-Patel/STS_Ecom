package com.ecom.repository;

import com.ecom.model.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByIsActiveTrue();
}
