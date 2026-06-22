package com.ecom.util;

public enum OrderStatus {
	IN_PROGRESS(1, "In Progess"),
	ORDER_RECEIVED(2, "Order Received"),
	PRODUCT_PACKED(3, "Product Packed"),
	OUT_FOR_DELIVERY(4, "Out for Delivery"),
	ORDER_DELIVERED(5, "Order Delivered");
	
	private Integer id;
	private String name;
	
	private OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	// All Getter Methods
	public Integer getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	// All Setter Methods
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
