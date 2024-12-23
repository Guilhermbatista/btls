package com.devguilherme.demo.devguilherme.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devguilherme.demo.devguilherme.dto.ProductDTO;
import com.devguilherme.demo.devguilherme.entities.Category;
import com.devguilherme.demo.devguilherme.entities.Product;
import com.devguilherme.demo.devguilherme.repositories.CategoryRepository;
import com.devguilherme.demo.devguilherme.repositories.ProductRepository;
import com.devguilherme.demo.devguilherme.service.ProductService;
import com.devguilherme.demo.devguilherme.service.exceptions.DatabaseException;
import com.devguilherme.demo.devguilherme.service.exceptions.ResourceNotFoundException;
import com.devguilherme.demo.devguilherme.tests.Factory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		product = Factory.createProduct();
		category = Factory.createCategory();
		page = new PageImpl<>(List.of(product));

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		// FindById
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		// Delete
		doNothing().when(repository).deleteById(existingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

		when(repository.getReferenceById(existingId)).thenReturn(product);
		when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

		when(repository.existsById(existingId)).thenReturn(true);
		
		when(repository.existsById(nonExistingId)).thenReturn(false);
		
		when(repository.existsById(dependentId)).thenReturn(true);
	}

	@Test
	public void findAllPageShouldReturnPage() {

		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAllPaged(pageable);

		Assertions.assertNotNull(result);
		verify(repository).findAll(pageable);
	}

	@Test
	public void findByIdShoulfReturnProductWhenIdExists() {
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
	}

	@Test
	public void findByIdShoulThrowResourceNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		verify(repository, times(1)).deleteById(dependentId);
	}

	@Test
	public void updateShouldReturnProductWhenIdExist() {
		ProductDTO productDTO = Factory.createProductDTO();
		Assertions.assertNotNull(service.update(existingId, productDTO));
	}

	@Test
	public void updateShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		ProductDTO dto = Factory.createProductDTO();
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, dto);
		});
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);

		});
	}

}
