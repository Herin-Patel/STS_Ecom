package com.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.model.Cart;
import com.ecom.model.Category;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.service.CartService;
import com.ecom.service.CategoryService;
import com.ecom.service.OrderService;
import com.ecom.service.UserService;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;

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

	@Autowired
	private OrderService orderServiceObj;

	@Autowired
	private CommonUtil commonUtilObj;

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
	public String orderPage(Principal p, Model pageModel) {
		UserDtls user = getLoggedInUserDetails(p);

		List<Cart> carts = cartServiceObj.getCartsByUser(user.getId());
		pageModel.addAttribute("carts", carts);

		if (carts.size() > 0) {
			Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;

			pageModel.addAttribute("orderPrice", orderPrice);
			pageModel.addAttribute("totalOrderPrice", totalOrderPrice);
		}

		return "/user/order";
	}

	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderRequest request, Principal p) throws Exception {
		// System.out.println(request);

		UserDtls currentUserDtls = getLoggedInUserDetails(p);

		orderServiceObj.saveOrder(currentUserDtls.getId(), request);

		return "redirect:/user/success";
	}

	@GetMapping("/success")
	public String loadSuccess() {
		return "/user/success";
	}

	@GetMapping("/user-orders")
	public String myOrder(Model pageModel, Principal p) {

		UserDtls loggedUser = getLoggedInUserDetails(p);

		List<ProductOrder> userOrders = orderServiceObj.getOrderByUser(loggedUser.getId());

		pageModel.addAttribute("userOrders", userOrders);

		return "/user/my_orders.html";
	}

	@GetMapping("/update-status")
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

		return "redirect:/user/user-orders";
	}
}
