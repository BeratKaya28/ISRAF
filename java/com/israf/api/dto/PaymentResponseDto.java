package com.israf.api.dto;

public class PaymentResponseDto {
	
	private boolean success;
	private String transId;
	private String message;
	
	public PaymentResponseDto() {
		
	}
	
	public PaymentResponseDto(boolean success, String transId, String message) {
		this.success = success;
		this.transId = transId;
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
