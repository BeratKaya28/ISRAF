package com.israf.api.dto;

public class CardRequestDto {

	private String cardNumber;
	private String cardHolderNumber;
	private String expiryDate;
	private String cvc;
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardHolderNumber() {
		return cardHolderNumber;
	}
	public void setCardHolderNumber(String cardHolderNumber) {
		this.cardHolderNumber = cardHolderNumber;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCvc() {
		return cvc;
	}
	public void setCvc(String cvc) {
		this.cvc = cvc;
	}
	
	
	
	
	
}
