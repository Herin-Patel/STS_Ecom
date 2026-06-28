package com.ecom.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
/*
 * @Getter
 * 
 * @Setter
 */
@Entity
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer quantity;

	private String orderId;
	private String status;
	private String paymentType;

	private LocalDate orderDate;

	@ManyToOne
	private Product product;

	private Double price;

	@ManyToOne
	private UserDtls user;

	@OneToOne(cascade = CascadeType.ALL)
	private OrderAddress orderAddress;

	// All Getter Methods

	public int getId() {
		return this.id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public String getStatus() {
		return this.status;
	}

	public String getPaymentType() {
		return this.paymentType;
	}

	public LocalDate getOrderDate() {
		return this.orderDate;
	}

	public Product getProduct() {
		return this.product;
	}

	public Double getPrice() {
		return this.price;
	}

	public UserDtls getUserDtls() {
		return this.user;
	}

	public OrderAddress getOrderAddress() {
		return this.orderAddress;
	}

	// All Setter Methods

	public void setId(int id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setUserDtls(UserDtls user) {
		this.user = user;
	}

	public void setOrderAddress(OrderAddress orderAddress) {
		this.orderAddress = orderAddress;
	}
}
