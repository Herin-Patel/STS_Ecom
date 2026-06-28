package com.ecom.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Cart;
import com.ecom.model.OrderAddress;
import com.ecom.model.OrderRequest;
import com.ecom.model.ProductOrder;
import com.ecom.repository.CartRepository;
import com.ecom.repository.ProductOrderRepository;
import com.ecom.service.OrderService;
import com.ecom.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ProductOrderRepository orderRepositoryObj;

	@Autowired
	private CartRepository cartRepositoryObj;

	@Override
	public void saveOrder(Integer userId, OrderRequest orderRequest) {

		List<Cart> carts = cartRepositoryObj.findByUserId(userId);

		for (Cart cart : carts) {
			ProductOrder order = new ProductOrder();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());
			order.setQuantity(cart.getQuantity());
			order.setUserDtls(cart.getUser());
			order.setStatus(OrderStatus.IN_PROGRESS.getName());

			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			orderRepositoryObj.save(order);
		}
	}

	@Override
	public List<ProductOrder> getOrderByUser(Integer userId) {

		List<ProductOrder> userOrders = orderRepositoryObj.findByUserId(userId);

		return userOrders;
	}

	@Override
	public Boolean updateOrderStatus(Integer orderId, String orderStatus) {

		Optional<ProductOrder> productOrder = orderRepositoryObj.findById(orderId);

		if (productOrder.isPresent()) {
			ProductOrder productOrderPresent = productOrder.get();

			productOrderPresent.setStatus(orderStatus);
			orderRepositoryObj.save(productOrderPresent);

			return true;
		}

		return false;
	}
}
