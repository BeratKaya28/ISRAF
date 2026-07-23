package com.israf.api.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.CategoryResponseDto;
import com.israf.api.model.Category;
import com.israf.api.repository.CategoryRepository;

import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryService {

	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	
	@Transactional
	public CategoryResponseDto createCategory(String categoryName) {
		
		if(categoryRepository.existsByName(categoryName)) {
			throw new RuntimeException("This category already exists");
		}
		
		Category category = new Category();
		category.setName(categoryName);
		Category savedCategory = categoryRepository.save(category);
		
		
		return mapToResponseDto(savedCategory);
	}
	
	@Transactional
	public void deleteCategory(Long id) {
		if(!categoryRepository.existsById(id)) {
			throw new RuntimeException("Category not found.");
		}
		
		categoryRepository.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<CategoryResponseDto> getAllCategories(){
		return categoryRepository.findAll().stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	private CategoryResponseDto mapToResponseDto(Category category) {
		CategoryResponseDto dto = new CategoryResponseDto();
		dto.setId(category.getId());
		dto.setName(category.getName());
		return dto;
	}
	
	
}
