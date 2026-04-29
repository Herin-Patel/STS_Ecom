package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ecom.model.Category;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userServiceObj;
	
	@Autowired
	private CategoryService categoryServiceObj;

	@GetMapping("/")
	public String home() {
		return "user/home";
	}

	@ModelAttribute
	public void getUserDetails(Principal p, Model pageModel) {
		if (p != null) {
			String email = p.getName();

			UserDtls userDtls = userServiceObj.getUserByEmail(email);

			pageModel.addAttribute("user", userDtls);
		}
		
		List<Category> allActiveCategory = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categorys", allActiveCategory);
	}
}
