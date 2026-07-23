package com.israf.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class EarningsResponseDto {

	private BigDecimal totalOverallEarnings;
	private Integer totalOverallOrders;
	private List<EarningsDto> storeEarnings;
	
	public BigDecimal getTotalOverallEarnings() {
		return totalOverallEarnings;
	}
	public void setTotalOverallEarnings(BigDecimal totalOveralEarnins) {
		this.totalOverallEarnings = totalOveralEarnins;
	}
	public Integer getTotalOverallOrders() {
		return totalOverallOrders;
	}
	public void setTotalOverallOrders(Integer totalOverAllOrders) {
		this.totalOverallOrders = totalOverAllOrders;
	}
	public List<EarningsDto> getStoreEarnings() {
		return storeEarnings;
	}
	public void setStoreEarnings(List<EarningsDto> storeEarnings) {
		this.storeEarnings = storeEarnings;
	}
	
	
	
	
	
}
