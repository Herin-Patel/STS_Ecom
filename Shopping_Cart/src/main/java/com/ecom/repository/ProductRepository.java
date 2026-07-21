package com.ecom.repository;

import com.ecom.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByIsActiveTrue();

	List<Product> findByCategory(String category);

	List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String keyword1, String keyword2);

	Page<Product> findByIsActiveTrue(Pageable pageableObj);

	Page<Product> findByCategory(Pageable pageableObj, String category);

	Page<Product> findAll(Pageable pageableObj);

	Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(Pageable pageableObj, String keyword1,
			String keyword2);
}
