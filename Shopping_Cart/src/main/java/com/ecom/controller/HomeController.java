package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryServiceObj;

	@Autowired
	private ProductService productServiceObj;

	@Autowired
	private UserService userServiceObj;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/signin")
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
	public String products(Model pageModel, @RequestParam(value = "category", defaultValue = "") String category) {
		System.out.println("category = " + category);
		List<Category> activeCategories = categoryServiceObj.getAllActiveCategory();
		List<Product> activeProducts = productServiceObj.getAllActiveProducts(category);

		pageModel.addAttribute("categories", activeCategories);
		pageModel.addAttribute("products", activeProducts);
		pageModel.addAttribute("paramValue", category);
		return "product";
	}

	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model pageModel) {
		Product productById = productServiceObj.getProductById(id);
		pageModel.addAttribute("product", productById);
		return "view_product";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImage(imageName);

		UserDtls savedUser = userServiceObj.saveUser(user);

		if (!ObjectUtils.isEmpty(savedUser)) {
			if (!file.isEmpty()) {
				/*
				 * File saveFile = new ClassPathResource("static/img").getFile();
				 * 
				 * Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
				 * "profile_img" + File.separator + file.getOriginalFilename());
				 * 
				 * // System.out.println(path); Files.copy(file.getInputStream(), path,
				 * StandardCopyOption.REPLACE_EXISTING);
				 */

				// Save image to folder
				String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/profile_img/";
				// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

				File dir = new File(uploadDir);
				if (!dir.exists())
					dir.mkdirs();

				Path filePath = Paths.get(uploadDir + imageName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("successMssg", "Registered successfully");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server !");
		}

		return "redirect:/register";
	}

	@ModelAttribute
	public void getUserDetails(Principal p, Model pageModel) {
		if (p != null) {
			String userEmail = p.getName();

			UserDtls userDtls = userServiceObj.getUserByEmail(userEmail);

			pageModel.addAttribute("user", userDtls);
		}
	}
}
