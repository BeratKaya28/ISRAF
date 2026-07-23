package com.israf.api.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.LoginRequestDto;
import com.israf.api.dto.LoginResponseDto;
import com.israf.api.dto.ResetPasswordRequestDto;
import com.israf.api.dto.SellerDto;
import com.israf.api.dto.UserRequestDto;
import com.israf.api.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

	private final UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register/seller")
	public ResponseEntity<?> registerSeller(@RequestBody SellerDto dto){
		return ResponseEntity.ok(userService.registerSeller(dto));
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam String email){
		userService.forgotPassword(email);
		return ResponseEntity.ok("Sifre sifirlama kodu gonderildi.");
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto dto){
		userService.resetPassword(dto.getEmail(), dto.getCode(), dto.getNewPassword());
	return ResponseEntity.ok("Sifreniz basariyla guncellendi");
	}
	
	
	
	@PostMapping("/register/customer")
	public ResponseEntity<?> registerCustomer(@RequestBody UserRequestDto dto){
		return ResponseEntity.ok(userService.registerCustomer(dto));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){		
		return ResponseEntity.ok(userService.login(dto));
	}
	
	@PostMapping("/verify-email")
	public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String code){
		boolean isVerified = userService.verifyEmail(email, code);
		
		if(isVerified) {
			return ResponseEntity.ok("Hesabiniz basariyla dogrulandi! Artik giris yapabilirsiniz!");
		}else {
			return ResponseEntity.badRequest().body("Hatali dogrulama kodu");
		}
		
	}
	
	@PostMapping("/admin-login")
	public ResponseEntity<?> adminLogin(@RequestBody LoginRequestDto dto){
		return ResponseEntity.ok(userService.adminManualLogin(dto));
	}
	
	@PostMapping("/google-mobile")
public ResponseEntity<?> googleMobileLogin(@RequestBody Map<String, String> googleData) {
		
		LoginResponseDto response = userService.handleGoogleLogin(googleData);
	
		if (response != null) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(201).body("Needs registration completion");
		}
	}
	
	
	
}
