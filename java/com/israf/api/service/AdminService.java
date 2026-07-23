package com.israf.api.service;

import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.AdminDashboardDto;
import com.israf.api.dto.StoreDto;
import com.israf.api.dto.UserProfileDto;

import com.israf.api.model.Role;
import com.israf.api.model.User;
import com.israf.api.repository.OrderRepository;
import com.israf.api.repository.StoreRepository;
import com.israf.api.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final OrderRepository orderRepository;
	
	public AdminService(UserRepository userRepository, StoreRepository storeRepository, OrderRepository orderRepository) {
		this.storeRepository = storeRepository;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}
	
	@Transactional(readOnly = true)
	public List<UserProfileDto> getAllUsers(){
		return userRepository.findAll().stream().map(this::mapToResponseDto).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setActive(false);
		userRepository.save(user);
	}
	
	@Transactional(readOnly = true)
	public UserProfileDto getUserDetails(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		return mapToResponseDto(user);
	}
	
	@Transactional(readOnly = true)
	public AdminDashboardDto getDashboard() {
		AdminDashboardDto stats = new AdminDashboardDto();
		
		stats.setTotalUserCount(userRepository.count());
		stats.setTotalStoreCount(storeRepository.count());
		
		stats.setActiveOrderCount(orderRepository.count());
		
		BigDecimal totalRevenue = orderRepository.findTotalRevenue();
		stats.setTotalTransactionVolume(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
		
		return stats;
	}
	
	@Transactional
	public void toggleUserStatus(Long userId, boolean status) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
		user.setActive(status);
		userRepository.save(user);
		
	}
	
	@Transactional(readOnly = true)
	public List<UserProfileDto> getUsersByRole(Role role){
		return userRepository.findByRole(role).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
	}
	
	private UserProfileDto mapToResponseDto(User user) {
		UserProfileDto dto = new UserProfileDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCity(user.getCity());
        dto.setDistrict(user.getDistrict());
        dto.setAddress(user.getAddress());
        dto.setFormattedAddress(user.getFormattedAddress());
        dto.setLatitude(user.getLatitude());
        dto.setLongitude(user.getLongitude());
        dto.setActive(user.isActive());
        
        if (user.getRole() == Role.SELLER && user.getStores() != null) {
            List<StoreDto> storeDtos = user.getStores().stream().map(store -> {
                StoreDto sDto = new StoreDto();
                sDto.setId(store.getId());
                sDto.setStoreName(store.getStoreName());
                sDto.setStoreType(store.getStoreType());
                sDto.setFormattedAddress(store.getFormattedAddress());
                sDto.setStorePhoneNumber(store.getPhoneNumber());                
                sDto.setAddress(store.getAddress());
                sDto.setCity(store.getCity());
                sDto.setDistrict(store.getDistrict());
                sDto.setLatitude(store.getLatitude());
                sDto.setLongitude(store.getLongitude());
                return sDto;
            }).collect(Collectors.toList());
            dto.setStores(storeDtos);
        }
		
		return dto;
	}
	
}
