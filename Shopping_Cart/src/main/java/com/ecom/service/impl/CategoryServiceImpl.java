package com.ecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;

import org.springframework.data.domain.Pageable;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepositoryObj;

	@Override
	public Category saveCategory(Category category) {
		return categoryRepositoryObj.save(category);
	}

	@Override
	public Boolean existCategory(String name) {
		return categoryRepositoryObj.existsByName(name);
	}

	@Override
	public List<Category> getAllCategory() {
		return categoryRepositoryObj.findAll();
	}

	@Override
	public Boolean deleteCategory(int id) {
		Category category = categoryRepositoryObj.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(category)) {
			categoryRepositoryObj.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategoryById(int id) {
		Category category = categoryRepositoryObj.findById(id).orElse(null);
		return category;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> activeCategories = categoryRepositoryObj.findByIsActiveTrue();
		return activeCategories;
	}

	@Override
	public Page<Category> getAllCategoryPagination(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Category> pageProduct = categoryRepositoryObj.findAll(pageable);
		return pageProduct;
	}
}