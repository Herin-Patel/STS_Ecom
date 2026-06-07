package com.ecom.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private UserDtls user;

	@ManyToOne
	private Product product;

	private Integer quantity;

	@Transient
	private Double totalPrice;

	// --------------------------
	// All getter methods
	// --------------------------

	public int getId() {
		return this.id;
	}

	public UserDtls getUser() {
		return this.user;
	}

	public Product getProduct() {
		return this.product;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

	// --------------------------
	// All setter methods
	// --------------------------

	public void setId(int id) {
		this.id = id;
	}

	public void setUser(UserDtls user) {
		this.user = user;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
