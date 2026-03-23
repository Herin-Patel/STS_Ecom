package com.ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int stock;
	private int discount;

	@Column(length = 500)
	private String title;
	@Column(length = 500)
	private String description;
	private String category;
	private String image;

	private Double price;
	private Double discountPrice;

	public int getId() {
		return this.id;
	}

	public int getStock() {
		return this.stock;
	}

	public int getDiscount() {
		return this.discount;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getCategory() {
		return this.category;
	}

	public String getImage() {
		return this.image;
	}

	public Double getPrice() {
		return this.price;
	}

	public Double getDiscountPrice() {
		return this.discountPrice;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}
}
