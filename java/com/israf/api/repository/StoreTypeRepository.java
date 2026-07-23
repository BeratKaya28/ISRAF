package com.israf.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.StoreType;

public interface StoreTypeRepository extends JpaRepository<StoreType, Long>{

	boolean existsByName(String name);
	

	
}
