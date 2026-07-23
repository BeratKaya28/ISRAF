package com.israf.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InventoryResponseDto {
	
	private Long id;
	private String productName;
	private String brand;
	private String storeName;
	private String category;
	private Integer stockCount;
	private BigDecimal originalPrice;
	private BigDecimal discountPrice;
	private Double latitude;
	private Double longitude;
	private LocalDateTime expirationDate;
	private BigDecimal discountRate;
	private Integer daysRemaining;
	private List<String> productImagesUrls;
	private String description;
	private String storeAddress;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public List<String> getProductImagesUrls() {
		return productImagesUrls;
	}
	public void setProductImagesUrls(List<String> productImagesUrls) {
		this.productImagesUrls = productImagesUrls;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Integer getDaysRemaining() {
		return daysRemaining;
	}
	public void setDaysRemaining(Integer daysRemaining) {
		this.daysRemaining = daysRemaining;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	
	
	

}
