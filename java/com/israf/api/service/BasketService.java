package com.israf.api.service;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.israf.api.dto.BasketDto;
import com.israf.api.dto.BasketItemDto;
import com.israf.api.model.Basket;
import com.israf.api.model.BasketItem;
import com.israf.api.model.Inventory;
import com.israf.api.model.User;
import com.israf.api.repository.BasketRepository;
import com.israf.api.repository.InventoryRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class BasketService {

	private final BasketRepository basketRepository;
	private final InventoryRepository inventoryRepository;
	
	public BasketService(BasketRepository basketRepository, InventoryRepository inventoryRepository) {
		this.basketRepository = basketRepository;
		this.inventoryRepository = inventoryRepository;
	}
	
	@Transactional(readOnly = true)
	public BasketDto getMyDto(User user) {
		Basket basket = getOrCreateBasket(user);
		return convertToDto(basket);
	}
	
	
	@Transactional
	private Basket getOrCreateBasket(User user) {
		return basketRepository.findByUser(user).orElseGet(() -> {
			Basket basket = new Basket();
			basket.setUser(user);
			basket.setTotalAmount(BigDecimal.ZERO);
			return basketRepository.save(basket);
		});
	}
	
	private void recalculateTotal(Basket basket) {
		BigDecimal total = BigDecimal.ZERO;
		for (BasketItem item : basket.getItems()) {
			BigDecimal price = item.getInventory().getDiscountPrice();
			BigDecimal quantity = new BigDecimal(item.getQuantity());
			total = total.add(price.multiply(quantity));
		}
		basket.setTotalAmount(total);
	}
	
	private BasketDto convertToDto(Basket basket) {
		BasketDto dto = new BasketDto();
		dto.setId(basket.getId());
		dto.setTotalAmount(basket.getTotalAmount());
		
		List<BasketItemDto> itemDtos = basket.getItems().stream().map(item -> {
			BasketItemDto itemDto = new BasketItemDto();
			itemDto.setId(item.getId());
			itemDto.setInventoryId(item.getInventory().getId());
			itemDto.setProductName(item.getInventory().getProduct().getName());
			
			List<String> images = item.getInventory().getProduct().getProductImagesUrls();
			if(images != null && !images.isEmpty()) {
				itemDto.setImageUrl(images.get(0));
			}
			
			BigDecimal price = item.getInventory().getDiscountPrice();
			BigDecimal subTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
			
			itemDto.setPrice(price);
			itemDto.setQuantity(item.getQuantity());
			itemDto.setSubTotal(subTotal);
			
			return itemDto;
		}).collect(Collectors.toList());
		
		dto.setItems(itemDtos);
		return dto;
	}
	
	@Transactional
	public BasketDto updateQuantity(User user, Long inventoryId, int delta) {
		Basket basket = getOrCreateBasket(user);
		BasketItem item = basket.getItems().stream()
		.filter(i -> i.getInventory().getId().equals(inventoryId))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Ürün sepette bulunamadı"));
		
		int quantity = item.getQuantity()+delta;
	
		if(delta > 0) {
			Inventory inventory = item.getInventory();
			
			if (quantity > inventory.getStockCount()) {
	             throw new RuntimeException("Yetersiz stok! En fazla " + inventory.getStockCount() + " adet alabilirsiniz.");
	        }
		}
		
		if(quantity <= 0) {
			basket.getItems().remove(item);
		}else {
	        item.setQuantity(quantity);
	    }
		
		recalculateTotal(basket);
		return convertToDto(basketRepository.save(basket));
	}
	
	@Transactional
	public BasketDto addItemToBasket(User user, Long inventoryId, Integer quantity) {
		Basket basket = getOrCreateBasket(user);
		Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Product not found"));
		
		Optional <BasketItem> existingItem = basket.getItems().stream()
				.filter(item -> item.getInventory().getId().equals(inventoryId))
				.findFirst();
		
		int currentQuantityInBasket = existingItem.isPresent() ? existingItem.get().getQuantity() : 0;
	    int totalRequestedQuantity = currentQuantityInBasket + quantity;
		
	    if (totalRequestedQuantity > inventory.getStockCount()) {
	        throw new RuntimeException("Yetersiz stok! Bu üründen sepete en fazla " + inventory.getStockCount() + " adet ekleyebilirsiniz.");
	    }
	    
	    if (existingItem.isPresent()) {
	        BasketItem item = existingItem.get();
	        item.setQuantity(totalRequestedQuantity);
	    } else {
	        BasketItem newItem = new BasketItem();
	        newItem.setBasket(basket);
	        newItem.setInventory(inventory);
	        newItem.setQuantity(quantity);
	        basket.getItems().add(newItem);
	    }
	    
	    recalculateTotal(basket);
	    Basket savedBasket = basketRepository.save(basket);
	    return convertToDto(savedBasket);
	    
	}

	
	
	
	
	
}
