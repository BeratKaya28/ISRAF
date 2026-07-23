package com.israf.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findByCategory_Name(String category);
}
