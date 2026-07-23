package com.israf.api.service;

import java.util.UUID;


import org.springframework.stereotype.Service;

import com.israf.api.dto.OrderRequestDto;
import com.israf.api.dto.PaymentResponseDto;

@Service
public class MockPaymentService {

	public PaymentResponseDto processPayment(OrderRequestDto dto) {
		
		String fakeTransactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		return new PaymentResponseDto(true, fakeTransactionId, "Payment successful");
	}
	
	
}
