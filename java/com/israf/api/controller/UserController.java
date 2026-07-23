package com.israf.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.SellerDto;
import com.israf.api.dto.UserProfileDto;
import com.israf.api.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

	private final UserService userService;
	
	public UserController(UserService userService) {
        this.userService = userService;
    }
	
	@GetMapping("/me")
	public ResponseEntity<UserProfileDto> getMyProfile(){
		return ResponseEntity.ok(userService.getMyProfileDto());
	}
	
	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestParam(required = false) String oldPassword, @RequestParam String newPassword){
		userService.changePassword(oldPassword, newPassword);
		return ResponseEntity.ok("Password changed");
	}
	
	@PutMapping("/update")
	public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto dto){
		return ResponseEntity.ok(userService.updateUserInfo(dto));
	}
	
	@PutMapping("/update-location")
	public ResponseEntity<?> updateLocation(@RequestParam Double longitude, @RequestParam Double latitude, @RequestParam(required=false) String address){
		userService.fastUpdateLocation(longitude, latitude, address);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/seller/{username}")
	public ResponseEntity<UserProfileDto> getSellerProfile(@PathVariable String username){
		return ResponseEntity.ok(userService.getSellerProfile(username));
	}
	
	@DeleteMapping("/delete-account")
	public ResponseEntity<?> deleteAccount(){
		userService.deleteAccount();
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/complete-profile")
	public ResponseEntity<UserProfileDto> completeProfile(@RequestBody SellerDto dto){
		return ResponseEntity.ok(userService.completeSellerProfile(dto));
	}
	
	@PutMapping("/change-username")
	public ResponseEntity<?> changeUsername(@RequestParam String newUsername){
		try {
			userService.changeUsername(newUsername);
			return ResponseEntity.ok("Kullanıcı adı başarıyla güncellendi.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/change-email")
	public ResponseEntity<?> changeEmail(@RequestParam String newEmail){
		try {
			userService.changeEmail(newEmail);
			return ResponseEntity.ok("E-posta adresi başarıyla güncellendi.");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
}
