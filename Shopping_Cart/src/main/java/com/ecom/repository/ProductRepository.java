package com.ecom.repository;

import com.ecom.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	public List<Product> findByIsActiveTrue();

	public List<Product> findByCategory(String category);

	public List<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(String keyword1,
			String keyword2);

	public Page<Product> findByIsActiveTrue(Pageable pageableObj);

	public Page<Product> findByCategory(Pageable pageableObj, String category);

	public Page<Product> findAll(Pageable pageableObj);

	public Page<Product> findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(Pageable pageableObj,
			String keyword1, String keyword2);

	public Page<Product> findByisActiveTrueAndTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(
			Pageable pageableObj, String keyword1, String keyword2);
}
