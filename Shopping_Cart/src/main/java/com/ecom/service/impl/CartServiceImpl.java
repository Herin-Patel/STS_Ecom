package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.ecom.model.Cart;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepositoryObj;

	@Autowired
	private UserRepository userRepositoryObj;

	@Autowired
	private ProductRepository productRepositoryObj;

	@Override
	public Cart saveCart(Integer productId, Integer userId) {

		UserDtls user = userRepositoryObj.findById(userId).get();
		Product product = productRepositoryObj.findById(productId).get();

		Cart cartStatus = cartRepositoryObj.findByProductIdAndUserId(productId, userId);

		Cart newCartObj = null;

		if (ObjectUtils.isEmpty(cartStatus)) {
			newCartObj = new Cart();
			newCartObj.setProduct(product);
			newCartObj.setUser(user);
			newCartObj.setQuantity(1);
			newCartObj.setTotalPrice(1 * product.getDiscountPrice());
		} else {
			newCartObj = cartStatus;
			newCartObj.setQuantity(newCartObj.getQuantity() + 1);
			newCartObj.setTotalPrice(newCartObj.getQuantity() * newCartObj.getProduct().getDiscountPrice());
		}

		Cart savedCart = cartRepositoryObj.save(newCartObj);

		return savedCart;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {

		List<Cart> userCart = cartRepositoryObj.findByUserId(userId);
		List<Cart> updatedCart = new ArrayList<>();

		Double totalOrderPrice = 0.0;

		for (Cart cartObj : userCart) {
			Double totalPrice = (cartObj.getProduct().getDiscountPrice() * cartObj.getQuantity());
			cartObj.setTotalPrice(totalPrice);

			totalOrderPrice += totalPrice;
			cartObj.setTotalOrderPrice(totalOrderPrice);
			updatedCart.add(cartObj);
		}

		return updatedCart;
	}

	@Override
	public Integer getUserCartCount(Integer userId) {
		Integer countByUserId = cartRepositoryObj.countByUserId(userId);
		return countByUserId;
	}

	@Override
	public Boolean updateQuantity(String value, Integer cartId) {

		// Exception handling
		if (value.isEmpty() || cartId == null) {
			return false;
		}
		
		Cart cartObj = cartRepositoryObj.findById(cartId);

		if (ObjectUtils.isEmpty(cartObj)) {
			return false;
		}
		
		if (value.equals("increase")) {
			cartObj.setQuantity(cartObj.getQuantity()+1);
		} else if (value.equals("decrease")) {
			cartObj.setQuantity(cartObj.getQuantity()-1);
		} 
		cartRepositoryObj.save(cartObj);
		
		return true;
	}
}
