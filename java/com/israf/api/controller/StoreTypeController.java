package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.StoreTypeResponseDto;
import com.israf.api.service.StoreTypeService;

@RestController
@RequestMapping("/api/store-types")
@CrossOrigin(origins = "*")
public class StoreTypeController {

	private final StoreTypeService storeTypeService;
	
	public StoreTypeController(StoreTypeService storeTypeService) {
		this.storeTypeService = storeTypeService;
	}
	
	@PostMapping
	public ResponseEntity<StoreTypeResponseDto> createStoreType(@RequestBody String name){
		return ResponseEntity.ok(storeTypeService.createStoreType(name));
	}
	
	@GetMapping
	public ResponseEntity<List<StoreTypeResponseDto>> getAllStoreTypes(){
		return ResponseEntity.ok(storeTypeService.getAllStoreTypes());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteStoreType(@PathVariable Long id){
		storeTypeService.deleteStoreType(id);
		return ResponseEntity.ok().build();
	}
	
	
}
