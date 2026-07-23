package com.israf.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.Category;

public interface CategoryRepository extends JpaRepository <Category, Long> {

	boolean existsByName(String name);
	
}
