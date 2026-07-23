package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.NotificationResponseDto;
import com.israf.api.model.Notification;
import com.israf.api.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

	private final NotificationService notificationService;
	
	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	@GetMapping("/unread-count")
	public ResponseEntity<Long> getUnreadCount(){
		return ResponseEntity.ok(notificationService.getUnreadCount());
	}
	
	@GetMapping
	public ResponseEntity<List<NotificationResponseDto>> getMyNotfications(){
		return ResponseEntity.ok(notificationService.getNotification());
	}
	
	@PutMapping("/{id}/read")
	public ResponseEntity<?> markAsRead(@PathVariable Long id){
		notificationService.markAsRead(id);
		return ResponseEntity.ok().build();
	}
	
	
}
