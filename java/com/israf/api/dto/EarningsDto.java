package com.israf.api.dto;

import java.math.BigDecimal;

public class EarningsDto {
	private Long storeId;
	private String storeName;
	private String district;
	private String city;
	private String storeImageUrl;
	private BigDecimal totalEarnings;
	private Integer orderCount;
	
	public Long getStoreId() {
		return storeId;
	}
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStoreImageUrl() {
		return storeImageUrl;
	}
	public void setStoreImageUrl(String storeImageUrl) {
		this.storeImageUrl = storeImageUrl;
	}
	public BigDecimal getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(BigDecimal totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
	public Integer getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	
	
	
	

}
