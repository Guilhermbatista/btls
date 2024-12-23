package com.devguilherme.demo.devguilherme.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devguilherme.demo.devguilherme.dto.CategoryDTO;
import com.devguilherme.demo.devguilherme.service.CategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;

	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
		Page<CategoryDTO> list = service.findAll(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findbyId(@PathVariable Long id) {
		CategoryDTO cat = service.findById(id);
		return ResponseEntity.ok().body(cat);
	}

	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO name) {
		name = service.insert(name);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(name.getId()).toUri();
		return ResponseEntity.created(uri).body(name);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO name) {

		return ResponseEntity.ok().body(service.update(id, name));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
