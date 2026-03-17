package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.service.CategoryService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import org.springframework.util.ObjectUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryServiceObj;

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/loadAddProduct")
	public String loadAddProduct() {
		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category() {
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category currentCategory, @RequestParam("file") MultipartFile file,
			HttpSession session) {

		String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";

		currentCategory.setImageName(imageName);

		if (categoryServiceObj.existCategory(currentCategory.getName())) {
			session.setAttribute("errorMssg", "Category name already exists !");
		} else {
			Category savedCategory = categoryServiceObj.saveCategory(currentCategory);

			if (ObjectUtils.isEmpty(savedCategory)) {
				session.setAttribute("errorMssg", "Not saved ! Internal server error.");
			} else {
				session.setAttribute("successMssg", "Category saved successfully.");
			}
		}

		return "redirect:/admin/category";
	}
}
