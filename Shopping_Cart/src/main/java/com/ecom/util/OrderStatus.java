package com.ecom.util;

public enum OrderStatus {
	IN_PROGRESS(1, "In Progess"),
	ORDER_RECEIVED(2, "Order Received"),
	PRODUCT_PACKED(3, "Product Packed"),
	OUT_FOR_DELIVERY(4, "Out for Delivery"),
	ORDER_DELIVERED(5, "Order Delivered");
	
	
	private Integer id;
	private String name;
}
