package com.israf.api.dto;

public class LoginResponseDto {
	private UserProfileDto user;
	private String token;
	
	public LoginResponseDto(UserProfileDto user, String token) {
		this.token = token;
		this.user = user;
	}

	public UserProfileDto getUser() {
		return user;
	}

	public void setUser(UserProfileDto user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
}
