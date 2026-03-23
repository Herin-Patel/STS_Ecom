package com.ecom.service;

import com.ecom.model.Product;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
	public Product saveProduct(Product product);
	
	public List<Product> getAllProducts();
	
	public Boolean deleteProduct(Integer id);
	
	public Product getProductById(Integer id);
	
	public Product updateProduct(Product product, MultipartFile file);
}
