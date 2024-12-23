package com.devguilherme.demo.devguilherme.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devguilherme.demo.devguilherme.dto.CategoryDTO;
import com.devguilherme.demo.devguilherme.entities.Category;
import com.devguilherme.demo.devguilherme.repositories.CategoryRepository;
import com.devguilherme.demo.devguilherme.service.exceptions.ResourceNotFoundException;
import com.devguilherme.demo.devguilherme.service.exceptions.DatabaseException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> category = repository.findById(id);
		Category obj = category.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new CategoryDTO(obj);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		return new CategoryDTO(repository.save(new Category(dto)));
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());

			return new CategoryDTO(repository.save(entity));
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
}
