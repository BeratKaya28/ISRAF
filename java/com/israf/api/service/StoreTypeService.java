package com.israf.api.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.israf.api.dto.StoreTypeResponseDto;
import com.israf.api.model.StoreType;
import com.israf.api.repository.StoreTypeRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreTypeService {
	
	private StoreTypeRepository storeTypeRepository;
	
	public StoreTypeService(StoreTypeRepository storeTypeRepository) {
		this.storeTypeRepository = storeTypeRepository;
	}
	
	@Transactional
	public StoreTypeResponseDto createStoreType(String name) {
		if(storeTypeRepository.existsByName(name)) {
			throw new RuntimeException("This store type already exists");
		}
		
		StoreType storeType = new StoreType();
		storeType.setName(name);
		StoreType savedStoreType = storeTypeRepository.save(storeType);
		
		return mapToDto(savedStoreType);
	}
	
	@Transactional(readOnly = true)
	public List<StoreTypeResponseDto> getAllStoreTypes(){
		return storeTypeRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteStoreType(Long id) {
		storeTypeRepository.deleteById(id);
	}
	
	private StoreTypeResponseDto mapToDto(StoreType storeType) {
		StoreTypeResponseDto dto = new StoreTypeResponseDto();
		dto.setId(storeType.getId());
		dto.setName(storeType.getName());
		return dto;
	}
	

}
