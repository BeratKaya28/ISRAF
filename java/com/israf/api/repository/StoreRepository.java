package com.israf.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.israf.api.model.Store;
import com.israf.api.model.User;

public interface StoreRepository extends JpaRepository<Store, Long>{
 
	List<Store> findByDistrict(String district);
	List<Store> findByOwnerId(Long ownerId);
	
	@Query("SELECT DISTINCT s FROM Store s " +
		       "LEFT JOIN FETCH s.inventories i " +
		       "LEFT JOIN FETCH i.product p " +
		       "WHERE s.owner = :owner AND s.isActive = true AND " +
		       "(LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(s.district) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
		List<Store> searchMyStores(@Param("owner") User owner, @Param("keyword") String keyword);
}
