package com.devguilherme.demo.devguilherme.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.devguilherme.demo.devguilherme.entities.Product;
import com.devguilherme.demo.devguilherme.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;

	private long exist = 1L;
	private long nonExistingID;
	private long countTotalProduct;

	@BeforeEach
	void setUp() throws Exception {
		exist = 1L;
		nonExistingID = 1000L;
		countTotalProduct = 25L;
	}

	@Test
	public void deleteShoudThrowEmptyResultDeleteObjectWhenIdExists() {

		repository.deleteById(exist);

		Optional<Product> result = repository.findById(exist);
		Assertions.assertFalse(result.isPresent());

	}

	@Test
	public void findByIdShoudReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> result = repository.findById(exist);
		Assertions.assertTrue(result.isPresent());

	}

	@Test
	public void findByIdShoudReturnEmptyOptionalWhenIdDoesNotExists() {
		Optional<Product> result = repository.findById(nonExistingID);
		Assertions.assertTrue(result.isEmpty());

	}

	@Test
	public void saveShoudPersistWithAutoincrementWhenIdIsNull() {

		Product product = Factory.createProduct();
		product.setId(null);

		product = repository.save(product);

		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProduct + 1, product.getId());
	}

}
