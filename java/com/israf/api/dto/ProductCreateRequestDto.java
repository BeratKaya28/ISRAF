package com.israf.api.dto;

import java.util.List;

public class ProductCreateRequestDto {

	private String name;
	private String description;
	private String brand;
	private Long categoryId;
	private List<String> productImagesUrls;
	
	
	public List<String> getProductImagesUrls() {
		return productImagesUrls;
	}
	public void setProductImagesUrls(List<String> productImagesUrls) {
		this.productImagesUrls = productImagesUrls;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
	
}
