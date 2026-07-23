package com.israf.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InventoryRequestDto {

	private BigDecimal originalPrice;
	private Long productId;
	private Long storeId;
	private Integer stockCount;
	private LocalDateTime expirationDate;
	private BigDecimal discountRate;
	private List<String> productImagesUrls; 
	
	
	public List<String> getProductImagesUrls() {
		return productImagesUrls;
	}
	public void setProductImagesUrls(List<String> productImagesUrls) {
		this.productImagesUrls = productImagesUrls;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
	public Integer getStockCount() {
		return stockCount;
	}
	public void setStockCount(Integer stockCount) {
		this.stockCount = stockCount;
	}
	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	
	
}
