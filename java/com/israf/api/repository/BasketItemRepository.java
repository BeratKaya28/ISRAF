package com.israf.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.israf.api.model.BasketItem;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long>{

}
