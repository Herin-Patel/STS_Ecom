package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryServiceObj;

	@Autowired
	private ProductService productServiceObj;

	@Autowired
	private UserService userServiceObj;

	@Autowired
	private CartService cartServiceObj;

	@Autowired
	private OrderService orderServiceObj;

	@Autowired
	private CommonUtil commonUtilObj;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

	/****************************************
	 * Handle Admin Profile
	 ****************************************/

	@ModelAttribute
	public void getAdminDetails(Principal p, Model pageModel) {
		if (p != null) {
			// String email = p.getName();
			// UserDtls userDtls = userServiceObj.getUserByEmail(email);

			UserDtls userDtls = commonUtilObj.getLoggedInUserDetails(p);

			pageModel.addAttribute("user", userDtls);
		}
	}

	/****************************************
	 * Add Product Section
	 ****************************************/

	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model pageModel) {
		List<Category> categories = categoryServiceObj.getAllCategory();

		pageModel.addAttribute("categories", categories);

		return "admin/add_product";
	}

	/****************************************
	 * Add Category Section
	 ****************************************/

	@GetMapping("/category")
	public String category(Model pageModel, @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
		// pageModel.addAttribute("categories", categoryServiceObj.getAllCategory());

		Page<Category> page = categoryServiceObj.getAllCategoryPagination(pageNumber, pageSize);
		List<Category> categoryList = page.getContent();

		pageModel.addAttribute("categories", categoryList);
		pageModel.addAttribute("categorySize", categoryList.size());
		pageModel.addAttribute("pageNumber", page.getNumber());
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());

		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category currentCategory, @RequestParam("file") MultipartFile file,
			HttpSession session) {

		try {

			String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";
			currentCategory.setImageName(imageName);

			if (categoryServiceObj.existCategory(currentCategory.getName())) {
				session.setAttribute("errorMssg", "Category name already exists !");
			} else {
				Category savedCategory = categoryServiceObj.saveCategory(currentCategory);

				if (ObjectUtils.isEmpty(savedCategory)) {
					session.setAttribute("errorMssg", "Not saved ! Internal server error.");
				} else {
					// File saveFile = new ClassPathResource("static/img").getFile();

					// Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
					// "category_img" + File.separator + file.getOriginalFilename());

					// Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					// Save image to folder
					String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/category_img/";
					// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

					File dir = new File(uploadDir);
					if (!dir.exists())
						dir.mkdirs();

					Path path = Paths.get(uploadDir + imageName);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(path);

					session.setAttribute("successMssg", "Category saved successfully.");
				}
			}
		} catch (Exception expObj) {
			System.out.println("Exception : ");
			expObj.printStackTrace();
			session.setAttribute("errorMssg", "Something went wrong.");
		}
		return "redirect:/admin/category";
	}

	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id, HttpSession session) {

		Boolean deleteCategory = categoryServiceObj.deleteCategory(id);

		if (deleteCategory) {
			session.setAttribute("successMssg", "Category deleted successfully");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server !");
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/loadEditCategory/{id}")
	public String loadEditCategory(@PathVariable int id, Model pageModel) {
		pageModel.addAttribute("category", categoryServiceObj.getCategoryById(id));

		return "admin/edit_category";
	}

	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) {
		try {
			Category oldCategory = categoryServiceObj.getCategoryById(category.getId());

			String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

			if (!ObjectUtils.isEmpty(category)) {
				oldCategory.setName(category.getName());
				oldCategory.setIsActive(category.getIsActive());
				oldCategory.setImageName(imageName);
			}

			Category updatedCategory = categoryServiceObj.saveCategory(oldCategory);

			if (!ObjectUtils.isEmpty(updatedCategory)) {

				if (!file.isEmpty()) {
					// File saveFile = new ClassPathResource("static/img").getFile();

					// Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
					// "category_img" + File.separator + file.getOriginalFilename());

					// Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					// Save image to folder
					String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/category_img/";
					// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

					File dir = new File(uploadDir);
					if (!dir.exists())
						dir.mkdirs();

					Path path = Paths.get(uploadDir + imageName);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					System.out.println(path);
				}

				session.setAttribute("successMssg", "Category updated successfully");
			} else {
				session.setAttribute("errorMssg", "Something went wrong on Server !");
			}

		} catch (Exception expObj) {
			System.out.println("Exception : ");
			expObj.printStackTrace();
			session.setAttribute("errorMssg", "Something went wrong.");
		}

		return "redirect:/admin/loadEditCategory/" + category.getId();
	}

	/****************************************
	 * View Product Section
	 ****************************************/

	@GetMapping("/products")
	public String loadViewProduct(Model pageModel, @RequestParam(defaultValue = "") String productName,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {

		/*
		 * List<Product> productList = null;
		 * 
		 * if (productName != null && productName.length() > 0) { productList =
		 * productServiceObj.searchProduct(productName); } else { productList =
		 * productServiceObj.getAllProducts(); }
		 * 
		 * if (ObjectUtils.isEmpty(productList)) { // What if no products are available
		 * with the User suggestion. Then how to // display no product available }
		 */

		Page<Product> page = productServiceObj.getAllProducts(pageNumber, pageSize, productName);
		List<Product> productList = page.getContent();
		pageModel.addAttribute("products", productList);
		pageModel.addAttribute("productSize", productList.size());
		pageModel.addAttribute("pageNumber", page.getNumber());
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());

		return "admin/products";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product currentProduct, @RequestParam("file") MultipartFile file,
			HttpSession session) {
		try {

			String imageName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : "default.jpg";

			currentProduct.setImage(imageName);
			currentProduct.setDiscount(0);
			currentProduct.setDiscountPrice(currentProduct.getPrice());

			Product savedProduct = productServiceObj.saveProduct(currentProduct);

			if (!ObjectUtils.isEmpty(savedProduct)) {

				// File saveFile = new ClassPathResource("static/img").getFile();

				// Path path = Paths.get(saveFile.getAbsolutePath() + File.separator +
				// "category_img" + File.separator + file.getOriginalFilename());

				// Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// Save image to folder
				String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/product_img/";
				// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

				File dir = new File(uploadDir);
				if (!dir.exists())
					dir.mkdirs();

				Path filePath = Paths.get(uploadDir + imageName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				System.out.println(filePath);

				session.setAttribute("successMssg", "Product saved successfully.");
			} else {
				session.setAttribute("errorMssg", "Something went wrong on the Server !");
			}

		} catch (Exception expObj) {
			System.out.println("Exception : ");
			expObj.printStackTrace();
			session.setAttribute("errorMssg", "Something went wrong.");
		}

		return "redirect:/admin/loadAddProduct";
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {
		Boolean deletedProduct = productServiceObj.deleteProduct(id);

		if (deletedProduct) {
			session.setAttribute("successMssg", "Product deleted successfully.");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on the Server !");
		}

		return "redirect:/admin/products";
	}

	@GetMapping("/editProduct/{id}")
	public String editProduct(@PathVariable int id, Model pageModel) {
		pageModel.addAttribute("product", productServiceObj.getProductById(id));
		pageModel.addAttribute("categories", categoryServiceObj.getAllCategory());
		return "admin/edit_product";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
			HttpSession session, Model pageModel) {

		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errorMssg", "Invalid discount !");
		} else {
			Product updatedProduct = productServiceObj.updateProduct(product, file);

			if (!ObjectUtils.isEmpty(updatedProduct)) {
				session.setAttribute("successMssg", "Product updated successfully.");
			} else {
				session.setAttribute("errorMssg", "Something went wrong on the Server !");
			}
		}

		return "redirect:/admin/editProduct/" + product.getId();
	}

	/****************************************
	 * Manage Orders Section
	 ****************************************/

	@GetMapping("/orders")
	public String getAllOrders(Model pageModel,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {

		/*
		 * List<ProductOrder> allOrders = orderServiceObj.getAllOrders();
		 * 
		 * pageModel.addAttribute("allOrders", allOrders);
		 * pageModel.addAttribute("orderSearched", false);
		 * 
		 */

		Page<ProductOrder> page = orderServiceObj.getAllOrders(pageNumber, pageSize);
		List<ProductOrder> orderList = page.getContent();
		pageModel.addAttribute("allOrders", orderList);
		pageModel.addAttribute("orderSize", orderList.size());
		pageModel.addAttribute("pageNumber", page.getNumber());
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());

		return "admin/orders";
	}

	@PostMapping("/update-order-status")
	public String updateOrderStatus(@RequestParam Integer orderId, @RequestParam Integer orderStatus,
			HttpSession session) {

		OrderStatus[] values = OrderStatus.values();
		String status = null;

		for (OrderStatus orderSt : values) {
			if (orderSt.getId().equals(orderStatus)) {
				status = orderSt.getName();
			}
		}
		// System.out.println("Values : " + values);

		ProductOrder updatedOrder = orderServiceObj.updateOrderStatus(orderId, status);

		try {
			commonUtilObj.sendMailForProductOrder(updatedOrder, status);
		} catch (Exception expObj) {
			expObj.printStackTrace();
			session.setAttribute("errorMssg", "Failed to send Order status on registered email");
		}

		if (ObjectUtils.isEmpty(updatedOrder)) {
			session.setAttribute("errorMssg", "Order status not updated. Something went wrong !");
		} else {
			session.setAttribute("successMssg", "Order status updated");
		}

		return "redirect:/admin/orders";
	}

	@GetMapping("/search-order")
	public String searchOrder(Model pageModel, HttpSession session, @RequestParam String orderId,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "2") Integer pageSize) {

		// Original logic
		/*
		 * if (orderId != null && orderId.length() > 0) { ProductOrder searchedOrder =
		 * orderServiceObj.getOrderByOrderId(orderId.trim());
		 * 
		 * if (ObjectUtils.isEmpty(searchedOrder)) { session.setAttribute("errorMssg",
		 * "Not order with such OrderId"); } else { pageModel.addAttribute("order",
		 * searchedOrder); }
		 * 
		 * pageModel.addAttribute("orderSearched", true); } else { List<ProductOrder>
		 * allOrders = orderServiceObj.getAllOrders();
		 * 
		 * pageModel.addAttribute("allOrders", allOrders);
		 * pageModel.addAttribute("orderSearched", false);
		 * 
		 * return "redirect:/admin/orders"; }
		 */

		// My approach for pagination
		/*
		 * if (orderId != null && orderId.length() > 0) { Page<ProductOrder> page =
		 * orderServiceObj.getOrderByOrderId(orderId.trim(), pageNumber, pageSize);
		 * List<ProductOrder> searchedOrderList = page.getContent();
		 * 
		 * if (ObjectUtils.isEmpty(searchedOrderList)) {
		 * session.setAttribute("errorMssg", "Not order with such OrderId");
		 * 
		 * pageModel.addAttribute("order", null); } else { // ProductOrder searchedOrder
		 * = (ProductOrder) searchedOrderList; ProductOrder searchedOrder =
		 * searchedOrderList.get(0);
		 * 
		 * pageModel.addAttribute("order", searchedOrder);
		 * 
		 * }
		 * 
		 * pageModel.addAttribute("orderSize", searchedOrderList.size());
		 * pageModel.addAttribute("pageNumber", page.getNumber());
		 * pageModel.addAttribute("pageSize", pageSize);
		 * pageModel.addAttribute("totalElements", page.getTotalElements());
		 * pageModel.addAttribute("totalPages", page.getTotalPages());
		 * pageModel.addAttribute("isFirst", page.isFirst());
		 * pageModel.addAttribute("isLast", page.isLast());
		 * 
		 * pageModel.addAttribute("orderSearched", true); } else {
		 * 
		 * Page<ProductOrder> page = orderServiceObj.getAllOrders(pageNumber, pageSize);
		 * List<ProductOrder> orderList = page.getContent();
		 * pageModel.addAttribute("allOrders", orderList);
		 * pageModel.addAttribute("orderSize", orderList.size());
		 * pageModel.addAttribute("pageNumber", page.getNumber());
		 * pageModel.addAttribute("pageSize", pageSize);
		 * pageModel.addAttribute("totalElements", page.getTotalElements());
		 * pageModel.addAttribute("totalPages", page.getTotalPages());
		 * pageModel.addAttribute("isFirst", page.isFirst());
		 * pageModel.addAttribute("isLast", page.isLast());
		 * 
		 * pageModel.addAttribute("orderSearched", false);
		 * 
		 * return "redirect:/admin/orders"; }
		 * 
		 * return "/admin/orders";
		 */

		// ChatGPT Logic
		if (orderId != null && !orderId.trim().isEmpty()) {

			Page<ProductOrder> page = orderServiceObj.getOrderByOrderId(orderId.trim(), pageNumber, pageSize);
			List<ProductOrder> searchedOrderList = page.getContent();

			// Pagination information
			pageModel.addAttribute("orderSize", searchedOrderList.size());
			pageModel.addAttribute("pageNumber", page.getNumber());
			pageModel.addAttribute("pageSize", pageSize);
			pageModel.addAttribute("totalElements", page.getTotalElements());
			pageModel.addAttribute("totalPages", page.getTotalPages());
			pageModel.addAttribute("isFirst", page.isFirst());
			pageModel.addAttribute("isLast", page.isLast());

			ProductOrder searchedOrder = orderServiceObj.getOrderByOrderId(orderId.trim());

			pageModel.addAttribute("orderSearched", true);

			if (ObjectUtils.isEmpty(searchedOrder)) {
				pageModel.addAttribute("order", null);

				session.setAttribute("errorMssg", "No order found with such OrderId");

			} else {
				// ProductOrder searchedOrder = searchedOrderList.get(0);
				pageModel.addAttribute("order", searchedOrder);
			}

			return "/admin/orders";
		}

		Page<ProductOrder> page = orderServiceObj.getAllOrders(pageNumber, pageSize);
		List<ProductOrder> orderList = page.getContent();

		pageModel.addAttribute("allOrders", orderList);
		pageModel.addAttribute("orderSize", orderList.size());
		pageModel.addAttribute("pageNumber", page.getNumber());
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());

		pageModel.addAttribute("orderSearched", false);

		return "/admin/orders";
	}

	/****************************************
	 * Manage Users Section
	 ****************************************/

	@GetMapping("/users")
	public String getAllUsers(Model pageModel, @RequestParam(name = "userType", defaultValue = "1") Integer userType,
			@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize) {
		/*
		 * List<UserDtls> allUsers = userServiceObj.getUsers("ROLE_USER");
		 * pageModel.addAttribute("users", allUsers);
		 */

		Page<UserDtls> page = null;
		if (userType == 1) {
			page = userServiceObj.getUsers("ROLE_USER", pageNumber, pageSize);
		} else if (userType == 2) {
			page = userServiceObj.getUsers("ROLE_ADMIN", pageNumber, pageSize);
		} else {
			// Need to provide an exceptional condition
		}

		List<UserDtls> allUsers = page.getContent();

		pageModel.addAttribute("users", allUsers);
		pageModel.addAttribute("userSize", allUsers.size());
		pageModel.addAttribute("pageNumber", pageNumber);
		pageModel.addAttribute("pageSize", pageSize);
		pageModel.addAttribute("totalElements", page.getTotalElements());
		pageModel.addAttribute("totalPages", page.getTotalPages());
		pageModel.addAttribute("isFirst", page.isFirst());
		pageModel.addAttribute("isLast", page.isLast());
		pageModel.addAttribute("userType", userType);

		return "admin/users";
	}

	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id,
			@RequestParam Integer userType, HttpSession session) {
		Boolean value = userServiceObj.updateAccountStatus(id, status);

		if (value) {
			session.setAttribute("successMssg", "Account status has been updated.");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server ! Account status not updated.");
		}

		return "redirect:/admin/users?userType=" + userType;
	}

	@ModelAttribute
	public void getUserDetails(Principal p, Model pageModel) {
		if (p != null) {
			/*
			 * String email = p.getName(); UserDtls userDtls =
			 * userServiceObj.getUserByEmail(email);
			 */

			UserDtls userDtls = commonUtilObj.getLoggedInUserDetails(p);
			Integer userCartCount = cartServiceObj.getUserCartCount(userDtls.getId());

			pageModel.addAttribute("user", userDtls);
			pageModel.addAttribute("userCartCount", userCartCount);
		}

		List<Category> allActiveCategory = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categorys", allActiveCategory);
	}

	/****************************************
	 * Manage Admins Section
	 ****************************************/

	@GetMapping("/add-admin")
	public String loadAddAdmin() {
		return "/admin/add_admin.html";
	}

	@PostMapping("/save-admin")
	public String saveAdmin(@ModelAttribute UserDtls adminUser, @RequestParam("img") MultipartFile file,
			HttpSession session) {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		adminUser.setProfileImage(imageName);

		UserDtls savedAdminUser = userServiceObj.saveAdminUser(adminUser);

		if (!ObjectUtils.isEmpty(savedAdminUser)) {
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
			session.setAttribute("successMssg", "Admin registered successfully");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server !");
		}

		return "redirect:/admin/add-admin";
	}

	@GetMapping("/profile")
	public String viewProfile() {
		return "/admin/profile";
	}

	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile imageFile,
			HttpSession session) {

		UserDtls updatedUserProfile = userServiceObj.updateUserProfile(user, imageFile);

		if (ObjectUtils.isEmpty(updatedUserProfile)) {
			session.setAttribute("errorMssg", "Profile not updated. Something went wrong !");
		} else {
			session.setAttribute("successMssg", "Profile updated successfully");
		}

		return "redirect:/admin/profile";
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
			HttpSession session) {

		UserDtls loggedInUserDetails = commonUtilObj.getLoggedInUserDetails(p);

		boolean passwordMatch = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

		if (passwordMatch) {
			String encodedPassword = passwordEncoder.encode(newPassword);
			loggedInUserDetails.setPassword(encodedPassword);

			UserDtls updatedUser = userServiceObj.updateUser(loggedInUserDetails);
			if (ObjectUtils.isEmpty(updatedUser)) {
				session.setAttribute("errorMssg", "Password not updated ! Something went wrong on Server.");
			} else {
				session.setAttribute("successMssg", "Password updated successfully.");
			}

		} else {
			session.setAttribute("errorMssg", "Current Password incorrect !");
		}

		return "redirect:/admin/profile";
	}

}
