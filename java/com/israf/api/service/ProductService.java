package com.israf.api.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.ProductCreateRequestDto;
import com.israf.api.dto.ProductResponseDto;
import com.israf.api.model.Category;
import com.israf.api.model.Product;
import com.israf.api.repository.CategoryRepository;
import com.israf.api.repository.ProductRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
		this.productRepository = productRepository;
	}
	
	@Transactional
	public ProductResponseDto createProduct(ProductCreateRequestDto dto) {
		Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
		
		Product product = new Product();
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCategory(category);
		product.setProductImagesUrls(dto.getProductImagesUrls());
		product.setBrand(dto.getBrand());
		
		Product savedProduct = productRepository.save(product);
		
		return mapToResponseDto(savedProduct);
	}
	
	@Transactional(readOnly = true)
	public List<ProductResponseDto> getAllProducts(){
		return productRepository.findAll().stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	private ProductResponseDto mapToResponseDto(Product product) {
		ProductResponseDto dto = new ProductResponseDto();
		dto.setId(product.getId());
		dto.setBrand(product.getBrand());
		if(product.getCategory() != null) {
			dto.setCategoryName(product.getCategory().getName());
		}
		dto.setDescription(product.getName());
		dto.setProductImagesUrls(product.getProductImagesUrls());
		dto.setName(product.getName());
		
		return dto;
	}
	
}
