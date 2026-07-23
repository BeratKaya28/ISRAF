package com.israf.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.israf.api.dto.ProductCreateRequestDto;
import com.israf.api.dto.ProductResponseDto;
import com.israf.api.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

	private final ProductService productService;
	
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping
	public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateRequestDto dto){
		return ResponseEntity.ok(productService.createProduct(dto));
	}
	
	@GetMapping
	public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
		return ResponseEntity.ok(productService.getAllProducts());
	}
	
}

