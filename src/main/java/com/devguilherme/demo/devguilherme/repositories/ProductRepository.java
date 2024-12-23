package com.devguilherme.demo.devguilherme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devguilherme.demo.devguilherme.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}