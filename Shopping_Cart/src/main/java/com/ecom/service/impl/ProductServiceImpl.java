package com.ecom.service.impl;

import com.ecom.model.Product;
import com.ecom.service.ProductService;
import com.ecom.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepositoryObj;

	@Override
	public Product saveProduct(Product product) {
		return productRepositoryObj.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepositoryObj.findAll();
	}

	@Override
	public Boolean deleteProduct(Integer id) {
		Product product = productRepositoryObj.findById(id).orElse(null);

		if (!ObjectUtils.isEmpty(product)) {
			productRepositoryObj.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(Integer id) {
		Product product = productRepositoryObj.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile file) {

		Product oldProduct = getProductById(product.getId());

		String imageName = file.isEmpty() ? oldProduct.getImage() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(product)) {
			oldProduct.setTitle(product.getTitle());
			oldProduct.setDescription(product.getDescription());
			oldProduct.setCategory(product.getCategory());
			oldProduct.setPrice(product.getPrice());
			oldProduct.setStock(product.getStock());
			oldProduct.setImage(imageName);
			oldProduct.setIsActive(product.getIsActive());
			oldProduct.setDiscount(product.getDiscount());

			// Implementing logic of Discount
			Double discountDone = product.getPrice() * (product.getDiscount() / 100.0);
			Double discountedPrice = product.getPrice() - discountDone;
			oldProduct.setDiscountPrice(discountedPrice);
		}

		Product updatedProduct = productRepositoryObj.save(oldProduct);

		if (!ObjectUtils.isEmpty(updatedProduct)) {
			if (!file.isEmpty()) {
				try {
					// Save image to folder
					String uploadDir = "C:/Users/herry/Desktop/STS_Workspace/Shopping_Cart/src/main/resources/static/img/category_img/";
					// "C:\\Users\\herry\\Desktop\\STS_Workspace\\Shopping_Cart\\src\\main\\resources\\static\\img\\category_img"

					File dir = new File(uploadDir);
					if (!dir.exists())
						dir.mkdirs();

					Path path = Paths.get(uploadDir + imageName);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					System.out.println(path);
				} catch (Exception expObj) {
					System.out.println("Exception : ");
					expObj.printStackTrace();
				}
			}
			return updatedProduct;
		}

		return null;
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {
		List<Product> activeProducts = null;

		if (ObjectUtils.isEmpty(category)) {
			activeProducts = productRepositoryObj.findByIsActiveTrue();
		} else {
			activeProducts = productRepositoryObj.findByCategory(category);
		}

		return activeProducts;
	}

	@Override
	public List<Product> searchProduct(String ch) {

		List<Product> foundProducts = productRepositoryObj
				.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase(ch, ch);

		if (ObjectUtils.isEmpty(foundProducts)) {
			return null;
		}

		return foundProducts;
	}
}
