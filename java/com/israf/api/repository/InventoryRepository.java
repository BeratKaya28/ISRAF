package com.israf.api.repository;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.israf.api.model.Inventory;
import com.israf.api.model.User;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{
	
	Inventory findByStore_OwnerAndId(User owner, Long id);

	List<Inventory> findByStore_DistrictAndIsActiveTrue(String district);
	List<Inventory> findByProduct_Category_Name(String category);
	

	@Query("SELECT x FROM Inventory x "+
			"JOIN FETCH x.store s "+
			"JOIN FETCH x.product p "+
			"WHERE s.city = :city AND s.district = :district "+
			"AND x.isActive = true AND s.isActive = true "+ 
		    "AND x.stockCount > 0 AND x.expirationDate > CURRENT_TIMESTAMP")
	List<Inventory> findProductsByLocation(@Param("city") String city, @Param("district") String district);
	
	@Query("SELECT x FROM Inventory x WHERE x.store.id = :storeId AND x.isActive = true AND x.stockCount > :stockCount AND x.expirationDate > :now")
	List<Inventory> findActiveStoreInventory(@Param("storeId") Long storeId, @Param("stockCount") Integer stockCount, @Param("now") LocalDateTime now);
	
	List<Inventory> findByStore_Owner(User user);
	
	List<Inventory> findByStore_OwnerAndIsActiveTrueAndStore_IsActiveTrue(User user);
	List<Inventory> findByStore_IdAndIsActiveTrue(Long storeId);

	@Query("SELECT x FROM Inventory x " +
		       "JOIN FETCH x.store s " +
		       "JOIN FETCH x.product p " +
		       "WHERE s.city = :city AND s.district = :district " +
		       "AND x.isActive = true AND s.isActive = true " +
		       "AND x.stockCount > 0 AND x.expirationDate > CURRENT_TIMESTAMP " +
		       "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(s.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
		List<Inventory> searchProductsByLocationAndKeyword(
		       @Param("city") String city, 
		       @Param("district") String district, 
		       @Param("keyword") String keyword);

	
	
	
	@Query("SELECT i FROM Inventory i WHERE i.store.owner = :owner AND i.isActive = true AND " +
		       "(LOWER(i.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(i.product.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
		       "LOWER(i.store.storeName) LIKE LOWER(CONCAT('%', :keyword, '%')))")
		List<Inventory> searchMyInventory(@Param("owner") User owner, @Param("keyword") String keyword);
	
	@Modifying
    @Query("UPDATE Inventory i SET i.isActive = false WHERE i.isActive = true AND i.expirationDate < :now")
    int deactivateExpiredInventories(@Param("now") LocalDateTime now);
	
	
}

