package com.israf.api.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.israf.api.dto.StoreDto;
import com.israf.api.model.Inventory;
import com.israf.api.model.Store;
import com.israf.api.model.User;
import com.israf.api.repository.StoreRepository;
import com.israf.api.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
@Service
public class StoreService {

	private final StoreRepository storeRepository;
	private final UserService userService;
	
	public StoreService(StoreRepository storeRepository, UserService userService) {
		this.storeRepository = storeRepository;
		this.userService = userService;
	}
	
	@Transactional
	public StoreDto createStore(StoreDto dto) {
		User user = userService.getCurrentUser();
		
		Store store = new Store();
		store.setStoreName(dto.getStoreName());
		store.setAddress(dto.getAddress());
		store.setCity(dto.getCity());
		store.setDistrict(dto.getDistrict());
		store.setFormattedAddress(dto.getFormattedAddress());
		store.setStoreImageUrls(dto.getStoreImageUrls());
		store.setLatitude(dto.getLatitude());
		store.setLongitude(dto.getLongitude());
		store.setPhoneNumber(dto.getStorePhoneNumber());
		store.setStoreType(dto.getStoreType());
		store.setActive(true);
		store.setOwner(user);
		
		Store savedStore = storeRepository.save(store);
		
		return mapToStoreDto(savedStore);
	}

	@Transactional
	public StoreDto updateStore(Long storeId, StoreDto dto) {
		String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
		
		if(!store.getOwner().getUsername().equals(currentUsername)) {
			throw new RuntimeException("Unauthorized access.");
		}
		
		if(dto.getStoreType() != null) {
			store.setStoreType(dto.getStoreType());
		}
		if(dto.getStoreName() != null) {
			store.setStoreName(dto.getStoreName());
		}
		if(dto.getStorePhoneNumber() != null) {
			store.setPhoneNumber(dto.getStorePhoneNumber());
		}
		if(dto.getCity() != null) {
			store.setCity(dto.getCity());
		}
		if(dto.getDistrict() != null) {
			store.setDistrict(dto.getDistrict());
		}
		if(dto.getAddress() != null) {
			store.setAddress(dto.getAddress());
		}
		if(dto.getFormattedAddress() != null) {
			store.setFormattedAddress(dto.getFormattedAddress());
		}
		if(dto.getLongitude() != null) {
			store.setLongitude(dto.getLongitude());
		}
		if(dto.getLatitude() != null) {
			store.setLatitude(dto.getLatitude());
		}
		
		if(dto.getStoreImageUrls() != null) {
			store.setStoreImageUrls(dto.getStoreImageUrls());
		}
		
		
		return mapToStoreDto(storeRepository.save(store));
	}
	
	@Transactional
	public void deleteStore(Long storeId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found"));
		
		if(!store.getOwner().getUsername().equals(username)) {
			throw new RuntimeException("Unauthorized access.");
		}
		
		store.setActive(false);
		
		if(store.getInventories() != null) {
			for(Inventory i : store.getInventories()) {
				i.setActive(false);
			}
		}
		
		
		storeRepository.save(store);
	}
	
	@Transactional(readOnly = true)
	public List<StoreDto> getAllStores(){
		return storeRepository.findAll().stream().map(this::mapToStoreDto).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<StoreDto> searchMyStores(String keyword){
		User user = userService.getCurrentUser();
		
		return storeRepository.searchMyStores(user, keyword).stream()
				.map(this::mapToStoreDto)
				.collect(Collectors.toList());
	}
	
	
	
	public StoreDto mapToStoreDto(Store store) {
		StoreDto dto = new StoreDto();
		
		dto.setId(store.getId());
		dto.setStoreType(store.getStoreType());
		dto.setStoreName(store.getStoreName());
		dto.setStorePhoneNumber(store.getPhoneNumber());
		dto.setAddress(store.getAddress());
		dto.setFormattedAddress(store.getFormattedAddress());
		
		dto.setCity(store.getCity());
		dto.setLatitude(store.getLatitude());
		dto.setLongitude(store.getLongitude());
		dto.setDistrict(store.getDistrict());
		dto.setStoreImageUrls(store.getStoreImageUrls());
		
		return dto;
	}
	
	
}
