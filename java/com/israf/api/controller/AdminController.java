package com.israf.api.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.AdminDashboardDto;
import com.israf.api.dto.UserProfileDto;
import com.israf.api.model.Role;
import com.israf.api.service.AdminService;
import com.israf.api.service.CategoryService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping("/stats")
	public ResponseEntity<AdminDashboardDto> getStats(){
		return ResponseEntity.ok(adminService.getDashboard());
	}
	
	@GetMapping("/users")
    public ResponseEntity<List<UserProfileDto>> getUsersByRole(@RequestParam(required = false) Role role) {
        if (role != null) {
            return ResponseEntity.ok(adminService.getUsersByRole(role));
        }
        return ResponseEntity.ok(adminService.getAllUsers());
    }
	
	@PutMapping("/users/{userId}/status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long userId, @RequestParam boolean status) {
        adminService.toggleUserStatus(userId, status);
        return ResponseEntity.ok("Kullanıcı durumu güncellendi.");
    }
	
	
	
}
