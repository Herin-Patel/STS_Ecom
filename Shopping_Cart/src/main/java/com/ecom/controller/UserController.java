package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Cart;
import com.ecom.model.Category;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userServiceObj;

	@Autowired
	private CategoryService categoryServiceObj;

	@Autowired
	private CartService cartServiceObj;

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

			Integer userCartCount = cartServiceObj.getUserCartCount(userDtls.getId());
			pageModel.addAttribute("userCartCount", userCartCount);
		}

		List<Category> allActiveCategory = categoryServiceObj.getAllActiveCategory();
		pageModel.addAttribute("categorys", allActiveCategory);
	}

	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		Cart savedCart = cartServiceObj.saveCart(pid, uid);

		if (ObjectUtils.isEmpty(savedCart)) {
			session.setAttribute("errorMssg", "Failed to add product to Cart !");
		} else {
			session.setAttribute("successMssg", "Product addded to Cart successfully.");
		}
		return "redirect:/product/" + pid;
	}

	@GetMapping("/cart")
	public String loadCartPage(Principal p, Model pageModel) {
		UserDtls user = getLoggedInUserDetails(p);

		List<Cart> carts = cartServiceObj.getCartsByUser(user.getId());
		pageModel.addAttribute("carts", carts);

		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			pageModel.addAttribute("totalOrderPrice", totalOrderPrice);
		}

		return "/user/cart";
	}

	private UserDtls getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserDtls userDtls = userServiceObj.getUserByEmail(email);
		return userDtls;
	}

	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String value, @RequestParam Integer cartId, HttpSession session) {

		Boolean quantityUpdated = cartServiceObj.updateQuantity(value, cartId);

		if (quantityUpdated) {
			session.setAttribute("successMssg", "Cart quantiy updated successfully.");
		} else {
			session.setAttribute("errorMssg", "Error in updating Cart quantity.");
		}

		return "redirect:/user/cart";
	}

	@GetMapping("/orders")
	public String orderPage() {
		return "/user/order";
	}
}
