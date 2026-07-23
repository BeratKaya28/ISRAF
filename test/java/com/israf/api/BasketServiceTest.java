package com.israf.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.israf.api.dto.BasketDto;
import com.israf.api.model.Basket;
import com.israf.api.model.Inventory;
import com.israf.api.model.Product;
import com.israf.api.model.User;
import com.israf.api.repository.BasketRepository;
import com.israf.api.repository.InventoryRepository;
import com.israf.api.service.BasketService;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {
	
	@Mock
	private BasketRepository basketRepository;
	
	@Mock
	private InventoryRepository inventoryRepository;
	
	@InjectMocks
	private BasketService basketService;
	
	private User user;
	private Inventory inventory;
	private Basket basket;
	
	
	@BeforeEach
	void setUp() {
		user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Product product = new Product();
        product.setId(1L);
        product.setName("Domates");

        inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProduct(product);
        inventory.setDiscountPrice(BigDecimal.TEN);
        inventory.setStockCount(10);

        basket = new Basket();
        basket.setId(1L);
        basket.setUser(user);
        basket.setItems(new ArrayList<>());
        basket.setTotalAmount(BigDecimal.ZERO);
		
	}
	
	
	@Test
    void addItemToBasket_Success() {
        
        when(basketRepository.findByUser(user)).thenReturn(Optional.of(basket));
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);

        
        BasketDto result = basketService.addItemToBasket(user, 1L, 2);

        
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());
        assertEquals(0, BigDecimal.valueOf(20).compareTo(result.getTotalAmount()));
        verify(basketRepository, times(1)).save(any(Basket.class));
    }
	
	@Test
    void addItemToBasket_InsufficientStock_ThrowsException() {
        when(basketRepository.findByUser(user)).thenReturn(Optional.of(basket));
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventory));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            basketService.addItemToBasket(user, 1L, 15); 
        });

        assertTrue(exception.getMessage().contains("insufficient stock"));
        verify(basketRepository, never()).save(any(Basket.class));
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
}
