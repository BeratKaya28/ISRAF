package com.israf.api.repository;


import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.israf.api.model.Order;
import com.israf.api.model.User;

public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findByCustomerOrderByOrderDateDesc(User user);
	List<Order> findByPickupCode(String pickupCode);
	
	Page<Order> findByInventory_Store_IdOrderByOrderDateDesc(Long storeId, Pageable pageable);
	
	@Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status != 'CANCELLED'")
	BigDecimal findTotalRevenue();
	
	
	
}
