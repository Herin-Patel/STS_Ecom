package com.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryServiceObj;

	@Autowired
	private ProductService productServiceObj;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	/*
	 * @GetMapping("/base") public String base() { return "base"; }
	 */

	@GetMapping("/products")
	public String products(Model pageModel) {
		List<Category> activeCategories = categoryServiceObj.getAllActiveCategory();
		List<Product> activeProducts = productServiceObj.getAllActiveProducts();

		pageModel.addAttribute("categories", activeCategories);
		pageModel.addAttribute("products", activeProducts);
		return "product";
	}

	@GetMapping("/product")
	public String product() {
		return "view_product";
	}
}
