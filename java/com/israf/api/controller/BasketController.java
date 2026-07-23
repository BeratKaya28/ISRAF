package com.israf.api.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.BasketDto;
import com.israf.api.model.User;
import com.israf.api.service.BasketService;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

	@Autowired
	private BasketService basketService;

	
	@GetMapping("/my-basket")
	public ResponseEntity<BasketDto> getMyBasket(@AuthenticationPrincipal User user){
		return ResponseEntity.ok(basketService.getMyDto(user));
	}
	
	@PostMapping("/add")
	public ResponseEntity<BasketDto> addToBasket(
			@AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> request){
		Long inventoryId = Long.valueOf(request.get("inventoryId").toString());
        Integer quantity = Integer.valueOf(request.get("quantity").toString());
        return ResponseEntity.ok(basketService.addItemToBasket(user, inventoryId, quantity));
		
	}
	
	@PutMapping("/update-quantity")
	public ResponseEntity<BasketDto> updateQuantity(@AuthenticationPrincipal User user, @RequestBody Map<String, Object> request) {
        Long inventoryId = Long.valueOf(request.get("inventoryId").toString());
        Integer delta = Integer.valueOf(request.get("delta").toString()); 
        return ResponseEntity.ok(basketService.updateQuantity(user, inventoryId, delta));
    }	
	
}
