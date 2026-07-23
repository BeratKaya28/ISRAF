package com.israf.api.service;

import java.math.BigDecimal;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.InventoryRequestDto;
import com.israf.api.dto.InventoryResponseDto;
import com.israf.api.model.Inventory;
import com.israf.api.model.Notification;
import com.israf.api.model.Product;
import com.israf.api.model.Role;
import com.israf.api.model.Store;
import com.israf.api.model.User;
import com.israf.api.repository.InventoryRepository;
import com.israf.api.repository.ProductRepository;
import com.israf.api.repository.StoreRepository;
import com.israf.api.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final UserService userService;
	private final NotificationService notificationService;
	
	private final StoreRepository storeRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	

	
	public InventoryService(InventoryRepository inventoryRepository, 
			UserService userService, 
			StoreRepository storeRepository,
			ProductRepository productRepository, 
			UserRepository userRepository,
			NotificationService notificationService) 
	{
		this.inventoryRepository=inventoryRepository;
		this.userService = userService;
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.notificationService = notificationService;
	}
	
	
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> getNearProduct(){
		User customer = userService.getCurrentUser();
		
		List<Inventory> inventories = inventoryRepository.findProductsByLocation(
				customer.getCity(),
				customer.getDistrict());
		
		return inventories.stream().map(this::mapToResponseDto).collect(Collectors.toList());
		
	}
	
	@Transactional
	public void reduceStock(Long inventoryId, Integer quantity) {
		Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Product not found"));
		
		if(inventory.getStockCount() < quantity) {
			throw new RuntimeException("Insufficient stock!");
		}
		
		inventory.setStockCount(inventory.getStockCount() - quantity);
		inventoryRepository.save(inventory);
		
	}
	
	
	@Transactional
	public InventoryResponseDto addInventory(InventoryRequestDto dto) {
		User seller = userService.getCurrentUser();
		
		Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
		
		Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new RuntimeException("Store not found"));
		
		if(!store.getOwner().getUsername().equals(seller.getUsername())) {
			throw new RuntimeException("You can only add products to your own store");
		}
		
		BigDecimal discountAmount = dto.getOriginalPrice().multiply(dto.getDiscountRate()).divide(new BigDecimal("100"));
		
		BigDecimal finalPrice = dto.getOriginalPrice().subtract(discountAmount);
		
		Inventory inventory = new Inventory();
		inventory.setProduct(product);
		inventory.setOriginalPrice(dto.getOriginalPrice());
		inventory.setStore(store);
		inventory.setDiscountRate(dto.getDiscountRate());
		inventory.setDiscountPrice(finalPrice);
		inventory.setActive(true);
		inventory.setExpirationDate(dto.getExpirationDate());
		inventory.setStockCount(dto.getStockCount());
		
		Inventory savedInventory = inventoryRepository.save(inventory);
		
		List<User> customers = userRepository.findByCityAndDistrictAndRoleAndIsActiveTrue(store.getCity(), store.getDistrict(), Role.CUSTOMER);
		
		for(User customer : customers) {
			Notification notification = new Notification();
			notification.setUser(customer);
			notification.setTitle("Yakininizda Yeni Bir Firsat! 🚀");
			notification.setMessage(store.getStoreName() + " adli isletme " + product.getName() + " urununu ekledi tukenmeden kap!");
			notification.setType("NEW_DEAL_NEARBY");
			notificationService.sendNotification(notification);
		}
		
		
		Notification notification = new Notification();
		notification.setUser(seller);
		notification.setTitle("Ürün Yayında 🟢");
		notification.setMessage(product.getName() + " adlı ürününüz " + inventory.getStockCount() + " adet stokla başarıyla yayına alındı.");
		notification.setType("PRODUCT_PUBLISHED");
		notificationService.sendNotification(notification);
		
		
		
		return mapToResponseDto(savedInventory);
	}
	
	@Transactional
	public InventoryResponseDto updateInventory(InventoryRequestDto dto, Long inventoryId) {
		User seller = userService.getCurrentUser();
		
		Inventory existingInventory = inventoryRepository.findByStore_OwnerAndId(seller, inventoryId);
		
		if(existingInventory == null) {
			throw new RuntimeException("Inventory not found or you are not owner.");
		}
		
		if(dto.getStockCount() != null) {
			existingInventory.setStockCount(dto.getStockCount());
		}
		if(dto.getExpirationDate() != null) {
			existingInventory.setExpirationDate(dto.getExpirationDate());
		}
		if(dto.getOriginalPrice() != null) {
			existingInventory.setOriginalPrice(dto.getOriginalPrice());
		}
		if(dto.getDiscountRate() != null) {
			existingInventory.setDiscountRate(dto.getDiscountRate());
		}
		
		if(dto.getOriginalPrice() != null || dto.getDiscountRate() != null) {
			BigDecimal originalPrice = existingInventory.getOriginalPrice();
			BigDecimal discountRate = existingInventory.getDiscountRate();

			BigDecimal discountAmount = originalPrice.multiply(discountRate).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
			BigDecimal finalPrice = originalPrice.subtract(discountAmount);
			existingInventory.setDiscountPrice(finalPrice);
		}
		
		if(dto.getProductImagesUrls() != null) {
			Product product = existingInventory.getProduct();
			product.setProductImagesUrls(dto.getProductImagesUrls());
			productRepository.save(product);	
		}
		
		
		
		Inventory updatedInventory = inventoryRepository.save(existingInventory);
		
		return mapToResponseDto(updatedInventory);
	}
	
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> getStoreInventory(Long storeId) {
		
		List<Inventory> inventories = inventoryRepository.findActiveStoreInventory(storeId, 0, LocalDateTime.now());
		
		return inventories.stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteInventory(Long inventoryId) {
		User user = userService.getCurrentUser();
		Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new RuntimeException("Inventory not found"));
		
		if(!inventory.getStore().getOwner().getId().equals(user.getId())) {
			throw new RuntimeException("You dont have this inventory.");
		}
		inventory.setActive(false);
		
		Notification deleteNotif = new Notification();
		deleteNotif.setUser(inventory.getStore().getOwner());
		deleteNotif.setTitle("İlan Kaldırıldı 🔴");
		deleteNotif.setMessage(inventory.getProduct().getName() + " adlı ilanınız yayından kaldırıldı.");
		deleteNotif.setType("PRODUCT_DELETED");
		notificationService.sendNotification(deleteNotif);
		inventoryRepository.save(inventory);
	}
	
	
	
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> getMyInventory(){
		User user = userService.getCurrentUser();
		List<Inventory> inventories = inventoryRepository.findByStore_OwnerAndIsActiveTrueAndStore_IsActiveTrue(user);
		return inventories.stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	
	
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> searchNearProducts(String keyword){
		User user = userService.getCurrentUser();
		
		List<Inventory> inventories = inventoryRepository.searchProductsByLocationAndKeyword(
				user.getCity(), user.getDistrict(), keyword);
		
		return inventories.stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	
	@Transactional(readOnly = true)
	public List<InventoryResponseDto> searchMyInventory(String keyword){
		User user = userService.getCurrentUser();
		
		List<Inventory> inventories = inventoryRepository.searchMyInventory(user, keyword);
		
		return inventories.stream().map(this::mapToResponseDto).collect(Collectors.toList());
		
	}
	
	
	
	private InventoryResponseDto mapToResponseDto(Inventory inventory) {
		InventoryResponseDto dto = new InventoryResponseDto();
		
		dto.setId(inventory.getId());
		dto.setOriginalPrice(inventory.getOriginalPrice());
		dto.setDiscountPrice(inventory.getDiscountPrice());
		dto.setDiscountRate(inventory.getDiscountRate());
		dto.setStockCount(inventory.getStockCount());
		dto.setExpirationDate(inventory.getExpirationDate());
		dto.setDescription(inventory.getProduct().getDescription());
	
		if(inventory.getStore() != null) {
			dto.setStoreName(inventory.getStore().getStoreName());
			dto.setLongitude(inventory.getStore().getLongitude());
			dto.setLatitude(inventory.getStore().getLatitude());
			dto.setStoreAddress(inventory.getStore().getFormattedAddress());
		}
		
		if(inventory.getProduct() != null) {
			dto.setBrand(inventory.getProduct().getBrand());
			dto.setProductName(inventory.getProduct().getName());
			
			if(inventory.getProduct().getCategory() != null) {
				dto.setCategory(inventory.getProduct().getCategory().getName());
			}
			dto.setProductImagesUrls(inventory.getProduct().getProductImagesUrls());
		}
		
		if(inventory.getExpirationDate() != null) {
			long days = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDateTime.now(), inventory.getExpirationDate());
			dto.setDaysRemaining((int)Math.max(0, days));

		}
		return dto;
	}
	
	
}
