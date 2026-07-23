package com.israf.api.service;

import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.NotificationResponseDto;
import com.israf.api.model.Notification;
import com.israf.api.model.User;
import com.israf.api.repository.NotificationRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserService userService;
	
	public NotificationService(NotificationRepository notificationRepository, UserService userService) {
		this.notificationRepository = notificationRepository;
		this.userService = userService;
	}
	
	@Transactional
	public void sendNotification(Notification notification) {
		if(notification.getCreatedAt() == null) {
			notification.setCreatedAt(LocalDateTime.now());
		}
		notification.setRead(false);
		notificationRepository.save(notification);	
	}
	
	@Transactional(readOnly = true)
	public List<NotificationResponseDto> getNotification(){
		User user = userService.getCurrentUser();
		
		Pageable twenty = PageRequest.of(0, 20);

		List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user, twenty);
		return notifications.stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	@Transactional
	public void markAsRead(Long notificationId) {
		User user = userService.getCurrentUser();
		
		Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new RuntimeException("Notification not found"));
		
		if(!notification.getUser().getId().equals(user.getId())) {
			throw new RuntimeException("Unauthorized action");
		}
		
		notification.setRead(true);
		notificationRepository.save(notification);
	}
	
	@Transactional
	public void markAllAsRead() {
		User user = userService.getCurrentUser();
		List<Notification> notif = notificationRepository.findByUserAndIsReadFalse(user);
		
		notif.forEach(not -> not.setRead(true));
		notificationRepository.saveAll(notif);
		
	}
	
	public long getUnreadCount() {
		User user = userService.getCurrentUser();
		return notificationRepository.countByUserAndIsReadFalse(user);
	}
	
	private NotificationResponseDto mapToResponseDto(Notification notification) {
		NotificationResponseDto dto = new NotificationResponseDto();
		dto.setId(notification.getId());
		dto.setCreateAt(notification.getCreatedAt());
		dto.setMessage(notification.getMessage());
		dto.setRead(notification.isRead());
		dto.setTitle(notification.getTitle());
		dto.setType(notification.getType());
		return dto;
	}
	
	
	
}
