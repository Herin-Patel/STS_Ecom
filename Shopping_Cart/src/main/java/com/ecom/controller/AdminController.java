package com.ecom.controller;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@GetMapping("/")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/loadAddProduct")
	public String loadAddProduct(Model pageModel) {
		List<Category> categories = categoryServiceObj.getAllCategory();

		pageModel.addAttribute("categories", categories);

		return "admin/add_product";
	}

	@GetMapping("/category")
	public String category(Model pageModel) {
		pageModel.addAttribute("categorys", categoryServiceObj.getAllCategory());
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

	@GetMapping("/products")
	public String loadViewProduct(Model pageModel) {
		pageModel.addAttribute("products", productServiceObj.getAllProducts());
		return "admin/products";
	}

	@GetMapping("/deleteProduct/{id}")
	public String loadViewProduct(@PathVariable int id, HttpSession session) {
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

	@ModelAttribute
	public void getUserDetails(Principal p, Model pageModel) {
		if (p != null) {
			String email = p.getName();
			UserDtls userDtls = userServiceObj.getUserByEmail(email);

			pageModel.addAttribute("user", userDtls);

			Integer userCartCount = cartServiceObj.getUserCartCount(userDtls.getId());
			pageModel.addAttribute("userCartCount", userCartCount);
		}

		List<Category> allActiveCategory = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categorys", allActiveCategory);
	}

	@GetMapping("/users")
	public String getAllUsers(Model pageModel) {
		List<UserDtls> allUsers = userServiceObj.getUsers("ROLE_USER");
		pageModel.addAttribute("users", allUsers);
		return "admin/users";
	}

	@GetMapping("/updateStatus")
	public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer id, HttpSession session) {
		Boolean value = userServiceObj.updateAccountStatus(id, status);

		if (value) {
			session.setAttribute("successMssg", "Account status has been updated.");
		} else {
			session.setAttribute("errorMssg", "Something went wrong on Server ! Account status not updated.");
		}

		return "redirect:/admin/users";
	}
}
