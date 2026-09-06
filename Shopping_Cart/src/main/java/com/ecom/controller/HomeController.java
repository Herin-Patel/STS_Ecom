package com.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CategoryService categoryServiceObj;

	@Autowired
	private ProductService productServiceObj;

	@Autowired
	private UserService userServiceObj;

	@Autowired
	private CommonUtil commonUtilObj;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private CartService cartServiceObj;

	@ModelAttribute
	public void getUserDetails(Principal p, Model pageModel) {
		if (p != null) {
			/*
			 * String userEmail = p.getName(); UserDtls userDtls =
			 * userServiceObj.getUserByEmail(userEmail);
			 */
			UserDtls userDtls = commonUtilObj.getLoggedInUserDetails(p);

			pageModel.addAttribute("user", userDtls);

			Integer userCartCount = cartServiceObj.getUserCartCount(userDtls.getId());
			pageModel.addAttribute("userCartCount", userCartCount);
		}

		List<Category> allActiveCategory = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categorys", allActiveCategory);
	}

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
	public String products(Model pageModel, @RequestParam(value = "category", defaultValue = "") String category,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize,
			@RequestParam(name = "productSearch", defaultValue = "") String productSearch) {

		System.out.println("category = " + category);
		List<Category> activeCategories = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categories", activeCategories);
		pageModel.addAttribute("paramValue", category);

		// List<Product> activeProducts =
		// productServiceObj.getAllActiveProducts(category);
		// pageModel.addAttribute("products", activeProducts);

		Page<Product> page = null;

		if (StringUtils.isEmpty(productSearch)) {
			page = productServiceObj.getAllActiveProductPagination(pageNumber, pageSize, category);
		} else {
			page = productServiceObj.searchActiveProductPagination(pageNumber, pageSize, productSearch);
		}

		List<Product> productList = page.getContent();
		pageModel.addAttribute("products", productList);
		pageModel.addAttribute("productSize", productList.size());
		pageModel.addAttribute("pageNumber", page.getNumber());
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());

		return "product";
	}

	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model pageModel) {
		Product productById = productServiceObj.getProductById(id);
		pageModel.addAttribute("product", productById);
		return "view_product";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file,
			HttpSession session) {

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
				try {
					Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			session.setAttribute("successMssg", "Registered successfully");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server !");
		}

		return "redirect:/register";
	}

	// Forgot Password Code
	@GetMapping("/forgot-password")
	public String showForgotPassword() {
		return "forgot_password.html";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(@RequestParam String email, HttpSession session, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {

		UserDtls userByEmail = userServiceObj.getUserByEmail(email);

		if (ObjectUtils.isEmpty(userByEmail)) {
			session.setAttribute("errorMssg", "Invalid Email !");
		} else {

			String resetToken = UUID.randomUUID().toString();
			userServiceObj.updateUserResetToken(email, resetToken);

			// Generate URL : http://localhost:8080/reset-password?token=sfgdhbhdfsgfdgr
			String url = CommonUtil.generateUrl(request) + "/reset-password?token=" + resetToken;

			Boolean mailSent = commonUtilObj.sendMail(url, email);

			if (mailSent) {
				session.setAttribute("successMssg", "Please check your email. Password reset link is sent.");
			} else {
				session.setAttribute("errorMssg", "Something went wrong on Server. Email not sent !");
			}
		}

		return "redirect:/forgot-password";
	}

	@GetMapping("/reset-password")
	public String showResetPassword(@RequestParam String token, HttpSession session, Model pageModel) {

		// Token validation
		UserDtls userByToken = userServiceObj.getUserByToken(token);

		if (userByToken == null) {
			pageModel.addAttribute("mssg", "Your link is invalid or expired !");
			return "message";
		}

		pageModel.addAttribute("token", token);
		return "reset_password";
	}

	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
			Model pageModel) {

		// Token validation
		UserDtls userByToken = userServiceObj.getUserByToken(token);

		if (userByToken == null) {
			pageModel.addAttribute("errorMssg", "Your link is invalid or expired !");
			return "message";
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null); // Once the user resets the password, set the token to null
			userServiceObj.updateUser(userByToken);

			// session.setAttribute("successMssg", "Password changed successfully.");
			pageModel.addAttribute("mssg", "Password changed successfully.");
			return "message";
		}
	}

	@GetMapping("/search-product")
	public String searchProduct(@RequestParam String ch, Model pageModel) {

		List<Product> searchedProduct = productServiceObj.searchProduct(ch);
		List<Category> activeCategories = categoryServiceObj.getAllActiveCategory();

		if (ObjectUtils.isEmpty(searchedProduct)) {
			// What if no products are available with the User suggestion. Then how to
			// display no product available
		}

		pageModel.addAttribute("categories", activeCategories);
		pageModel.addAttribute("products", searchedProduct);

		return "product";
	}
}
