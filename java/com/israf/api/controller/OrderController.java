package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.CardRequestDto;
import com.israf.api.dto.CardResponseDto;
import com.israf.api.dto.EarningsResponseDto;
import com.israf.api.dto.OrderRequestDto;
import com.israf.api.dto.OrderResponseDto;
import com.israf.api.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto dto){
		return ResponseEntity.ok(orderService.createOrder(dto));
	}
	
	@GetMapping("/my-orders")
	public ResponseEntity<List<OrderResponseDto>> getMyOrders(){
		return ResponseEntity.ok(orderService.getMyOrders());
	}
	
	@PutMapping("/{orderId}/complete")
	public ResponseEntity<OrderResponseDto> completeOrder(@PathVariable Long orderId, @RequestParam String code) {
	    return ResponseEntity.ok(orderService.completeOrder(orderId, code));
	}
	
	@GetMapping("/store/{storeId}")
	public ResponseEntity<List<OrderResponseDto>> getStoreOrders(@PathVariable Long storeId){
		return ResponseEntity.ok(orderService.getStoreOrders(storeId));
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId){
		return ResponseEntity.ok(orderService.canceledOrder(orderId));
	}
	
	@GetMapping("/my-earnings")
	public ResponseEntity<EarningsResponseDto> getMyEarningsSummary(){
		return ResponseEntity.ok(orderService.getMyEarningsSummary());
	}
	
	@PostMapping("/checkout")
	public ResponseEntity<CardResponseDto> checkoutBasket(@RequestBody CardRequestDto dto) {
	    return ResponseEntity.ok(orderService.confirmBasket(dto));
	}
	
	@GetMapping("/history/{storeId}")
	public ResponseEntity<List<OrderResponseDto>> getHistory(@PathVariable Long storeId){
	    return ResponseEntity.ok(orderService.getCompletedOrders(storeId));
	}
	
	@PutMapping("/complete-basket")
	public ResponseEntity<List<OrderResponseDto>> completeBasket(@RequestParam String code){
		return ResponseEntity.ok(orderService.completeBasketByCode(code));
	}
	
	
	
	
}
