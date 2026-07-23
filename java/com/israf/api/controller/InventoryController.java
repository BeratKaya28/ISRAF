package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.InventoryRequestDto;
import com.israf.api.dto.InventoryResponseDto;
import com.israf.api.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins="*")
public class InventoryController {

	private final InventoryService inventoryService;
	
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<InventoryResponseDto> addInventory(@RequestBody InventoryRequestDto dto){
		return ResponseEntity.ok(inventoryService.addInventory(dto));
	}
	
	@GetMapping("/near")
	public ResponseEntity<List<InventoryResponseDto>> getNearProducts(){
		return ResponseEntity.ok(inventoryService.getNearProduct());
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<InventoryResponseDto> updateInventory(@PathVariable Long id, @RequestBody InventoryRequestDto dto){
		return ResponseEntity.ok(inventoryService.updateInventory(dto, id));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteInventory(@PathVariable Long id){
		inventoryService.deleteInventory(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/store/{storeId}")
	public ResponseEntity<List<InventoryResponseDto>> getStoreInventory(@PathVariable Long storeId){
		return ResponseEntity.ok(inventoryService.getStoreInventory(storeId));
	}
	
	@GetMapping("/my-inventory")
	public ResponseEntity<List<InventoryResponseDto>> getMyInventory(){
		return ResponseEntity.ok(inventoryService.getMyInventory());
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<InventoryResponseDto>> searchInventory(@RequestParam String keyword){
		return ResponseEntity.ok(inventoryService.searchNearProducts(keyword));
	}
	
	@GetMapping("/my-inventory/search")
	public ResponseEntity<List<InventoryResponseDto>> searchMyInventory(@RequestParam String keyword) {
	    return ResponseEntity.ok(inventoryService.searchMyInventory(keyword));
	}
	
	
	
}
