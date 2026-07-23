package com.israf.api.dto;

import java.math.BigDecimal;

public class AdminDashboardDto {

	private long totalUserCount;
	private long totalStoreCount;
	private long activeOrderCount;
	private BigDecimal totalTransactionVolume;
	
	public long getTotalUserCount() {
		return totalUserCount;
	}
	public void setTotalUserCount(long totalUserCount) {
		this.totalUserCount = totalUserCount;
	}
	public long getTotalStoreCount() {
		return totalStoreCount;
	}
	public void setTotalStoreCount(long totalStoreCount) {
		this.totalStoreCount = totalStoreCount;
	}
	public long getActiveOrderCount() {
		return activeOrderCount;
	}
	public void setActiveOrderCount(long activeOrderCount) {
		this.activeOrderCount = activeOrderCount;
	}
	public BigDecimal getTotalTransactionVolume() {
		return totalTransactionVolume;
	}
	public void setTotalTransactionVolume(BigDecimal totalTransactionVolume) {
		this.totalTransactionVolume = totalTransactionVolume;
	}
	
	
	
	
	
}
