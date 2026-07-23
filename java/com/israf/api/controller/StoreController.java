package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.StoreDto;
import com.israf.api.service.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

	private final StoreService storeService;
	
	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @RequestBody StoreDto dto){
		return ResponseEntity.ok(storeService.updateStore(id, dto));
	}
	
	@PostMapping
	public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto dto){
		return ResponseEntity.ok(storeService.createStore(dto));
	}
	
	@PutMapping("/delete/{id}")
	public ResponseEntity<?> deleteStore(@PathVariable Long id){
		storeService.deleteStore(id);
		return ResponseEntity.ok("Isletme arsivlendi");
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<StoreDto>> searchStores(@RequestParam String keyword){
		return ResponseEntity.ok(storeService.searchMyStores(keyword));
		
	}
	
	
	
}
