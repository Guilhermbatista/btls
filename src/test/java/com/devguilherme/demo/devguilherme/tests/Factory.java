package com.devguilherme.demo.devguilherme.tests;

import java.time.Instant;

import com.devguilherme.demo.devguilherme.dto.ProductDTO;
import com.devguilherme.demo.devguilherme.entities.Category;
import com.devguilherme.demo.devguilherme.entities.Product;

public class Factory {
	public static Product createProduct() {
		
		Product product = new Product(1L, "Smartphone", "High-end smartphone with 128GB storage", 3999.99,
				"https://example.com/image.jpg", Instant.now());
		
		product.getCategories().add(new Category(2L, "Eletronics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		
		return new ProductDTO(product, product.getCategories());
	}

	public static Category createCategory() {
		return new Category(2L, "Eletronics");
	}
}
