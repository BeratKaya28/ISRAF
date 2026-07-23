package com.israf.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class BasketDto {

	private Long id;
	private BigDecimal totalAmount;
	private List<BasketItemDto> items;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public List<BasketItemDto> getItems() {
		return items;
	}
	public void setItems(List<BasketItemDto> items) {
		this.items = items;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
	
	
}
