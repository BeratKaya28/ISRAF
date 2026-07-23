package com.israf.api.dto;

import java.util.List;

public class ProductResponseDto {

	private Long id;
	private String name;
	private String brand;
	private String description;
	private String categoryName;
	private List<String> productImagesUrls;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<String> getProductImagesUrls() {
		return productImagesUrls;
	}
	public void setProductImagesUrls(List<String> productImagesUrls) {
		this.productImagesUrls = productImagesUrls;
	}
	
	
	
	
	
	
}
