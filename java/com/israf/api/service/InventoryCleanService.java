package com.israf.api.service;

import java.time.LocalDateTime;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.israf.api.repository.InventoryRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryCleanService {

	private final InventoryRepository inventoryRepository;
	
	public InventoryCleanService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}
	
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void deactiveExpiredInventories() {
		LocalDateTime now = LocalDateTime.now();
		
		int expiredCount = inventoryRepository.deactivateExpiredInventories(now);
		
		if(expiredCount > 0) {
            System.out.println(">>>>>>>>>[STOK TEMİZLİĞİ] Tarihi geçen " + expiredCount + " adet ürün pasife çekildi.");
        }
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
