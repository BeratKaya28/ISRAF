package com.israf.api.service;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.israf.api.repository.NotificationRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class NotificationCleanService {

	private final NotificationRepository notificationRepository;
	
	public NotificationCleanService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}
	
	
	@Scheduled(cron = "0 0 4 * * *")
	@Transactional
	public void cleanupNotifications() {
		notificationRepository.deleteExcessNotifications();
		
		System.out.println(">>>>>>>>>[TEMIZLIK] Eski Bildirimler Basariyla Temizlendi");
	
}
}
