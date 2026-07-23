package com.israf.api.dto;

import java.util.List;

public class SellerDto {

	private String username;
	private String email;
	private String password;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private String address;
	private String city;
	private String district;
	private Double latitude;
	private Double longitude;
	private String formattedAddress;
	private String profilePictureUrl;
	
	
	private String storeName;
	private String storeType;
	private String storePhoneNumber;
	private String storeAddress;
	private String storeCity;
	private String storeDistrict;
	private Double storeLatitude;
	private Double storeLongitude;
	private String storeFormattedAddress;
	private List<String> storeImageUrls;
	
	
	
	
	public List<String> getStoreImageUrls() {
		return storeImageUrls;
	}
	public void setStoreImageUrls(List<String> storeImgUrls) {
		this.storeImageUrls = storeImgUrls;
	}
	public String getProfilePictureUrl() {
		return profilePictureUrl;
	}
	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	public String getStoreDistrict() {
		return storeDistrict;
	}
	public void setStoreDistrict(String storeDistrict) {
		this.storeDistrict = storeDistrict;
	}
	public Double getStoreLatitude() {
		return storeLatitude;
	}
	public void setStoreLatitude(Double storeLatitude) {
		this.storeLatitude = storeLatitude;
	}
	public Double getStoreLongitude() {
		return storeLongitude;
	}
	public void setStoreLongitude(Double storeLongitude) {
		this.storeLongitude = storeLongitude;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getStorePhoneNumber() {
		return storePhoneNumber;
	}
	public void setStorePhoneNumber(String storePhoneNumber) {
		this.storePhoneNumber = storePhoneNumber;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public String getFormattedAddress() {
		return formattedAddress;
	}
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreFormattedAddress() {
		return storeFormattedAddress;
	}
	public void setStoreFormattedAddress(String storeFormattedAddress) {
		this.storeFormattedAddress = storeFormattedAddress;
	}
	
	
	
	
}
