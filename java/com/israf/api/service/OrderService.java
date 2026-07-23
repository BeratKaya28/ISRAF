package com.israf.api.service;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.israf.api.dto.CardRequestDto;
import com.israf.api.dto.CardResponseDto;
import com.israf.api.dto.EarningsDto;
import com.israf.api.dto.EarningsResponseDto;
import com.israf.api.dto.OrderRequestDto;
import com.israf.api.dto.OrderResponseDto;
import com.israf.api.dto.PaymentResponseDto;
import com.israf.api.model.Basket;
import com.israf.api.model.BasketItem;
import com.israf.api.model.Inventory;
import com.israf.api.model.Notification;
import com.israf.api.model.Order;
import com.israf.api.model.OrderStatus;
import com.israf.api.model.Store;
import com.israf.api.model.User;
import com.israf.api.repository.BasketRepository;
import com.israf.api.repository.NotificationRepository;
import com.israf.api.repository.OrderRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final InventoryService inventoryService;
	private final UserService userService;
	private final NotificationService notificationService;
	private final MockPaymentService paymentService;
	private final BasketRepository basketRepository;
	
	
	
	public OrderService(OrderRepository orderRepository,
			InventoryService inventoryService,
			UserService userService,
			NotificationService notificationService,
			MockPaymentService paymentService,
			BasketRepository basketRepository
			) {
		this.notificationService=notificationService;
		this.orderRepository = orderRepository;
		this.inventoryService = inventoryService;
		this.userService = userService;
		this.paymentService = paymentService;
		this.basketRepository = basketRepository;
	}
	
	@Transactional
	public OrderResponseDto createOrder(OrderRequestDto dto) {
		User user = userService.getCurrentUser();
		
		PaymentResponseDto paymentResponse = paymentService.processPayment(dto);
		
		if(!paymentResponse.isSuccess()) {
			throw new RuntimeException("Payment rejected");
		}
		
		inventoryService.reduceStock(dto.getInventoryId(), dto.getQuantity());
		
		Order order = new Order();
		order.setCustomer(user);
		order.setPickupCode(generatePickupCode());
		order.setStatus(OrderStatus.APPROVED);	
		order.setPaymentTransId(paymentResponse.getTransId());
		
		Order savedOrder = orderRepository.save(order);
		
		Notification notification = new Notification();
		notification.setUser(user);
		notification.setTitle("Odeme Basarili");
		notification.setMessage("Odemeniz alindi. Teslimat kodunuz: "+order.getPickupCode());
		notification.setType("ORDER_SUCCESS");
		notificationService.sendNotification(notification);
		
		Notification orderNotif = new Notification();
		orderNotif.setUser(order.getInventory().getStore().getOwner()); 
		orderNotif.setTitle("Yeni Sipariş! 🎉");
		orderNotif.setMessage("Harika! " + order.getInventory().getProduct().getName() + " adlı ürününüzden " + order.getQuantity() + " adet sipariş aldınız. Müşteri teslimat koduyla gelecektir.");
		orderNotif.setType("NEW_ORDER");
		notificationService.sendNotification(orderNotif);
		
		
		return mapToResponseDto(savedOrder);
	}
	
	private String generatePickupCode() {
		return UUID.randomUUID().toString().substring(0,6).toUpperCase();
	}
	
	
	@Transactional(readOnly = true)
	public List<OrderResponseDto> getMyOrders(){
		User user = userService.getCurrentUser();
		
		return orderRepository.findByCustomerOrderByOrderDateDesc(user).stream().map(this::mapToResponseDto).collect(Collectors.toList());
		
	}
	
	@Transactional
	public OrderResponseDto completeOrder(Long orderId, String enteredCode){
		User user = userService.getCurrentUser();

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		
		if(!order.getInventory().getStore().getOwner().getId().equals(user.getId())) {
			throw new RuntimeException("This order is not yours.");
		}
		
		if(!order.getPickupCode().equals(enteredCode)) {
			throw new RuntimeException("Hatalı teslimat kodu! Lütfen müşterinin ekranındaki kodu kontrol edin.");
		}
		
		
		if(order.getStatus() == OrderStatus.COMPLETED) {
			throw new RuntimeException("This order is completed");
		}
		
		if(order.getStatus() == OrderStatus.CANCELLED) {
			throw new RuntimeException("This order is cancelled.");
		}
		
		order.setStatus(OrderStatus.COMPLETED);
		Order updateOrder = orderRepository.save(order);
		
		Notification notification = new Notification();
		notification.setUser(order.getCustomer());
		notification.setTitle("Afiyet Olsun");
		notification.setMessage(order.getInventory().getProduct().getName() + " ürününüzü teslim aldınız. Bizi tercih ettiğiniz için teşekkürler!");
		notification.setType("ORDER_COMPLETED");
		notificationService.sendNotification(notification);
		
		return mapToResponseDto(updateOrder);
	}
	
	@Transactional(readOnly = true)
	public List<OrderResponseDto> getStoreOrders(Long storeId){
		User user = userService.getCurrentUser();
		
		boolean isOwner = user.getStores().stream().anyMatch(store -> store.getId().equals(storeId));
		
		if(!isOwner) {
			throw new RuntimeException("You cannot access this store.");
		}
		
		return orderRepository.findByInventory_Store_IdOrderByOrderDateDesc(storeId, org.springframework.data.domain.Pageable.unpaged())
				.getContent().stream()
				.map(this::mapToResponseDto)
				.collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public List<OrderResponseDto> getCompletedOrders(Long storeId){
		
		return orderRepository.findByInventory_Store_IdOrderByOrderDateDesc(storeId, org.springframework.data.domain.Pageable.unpaged())
				.getContent().stream()
				.filter(o -> o.getStatus() == OrderStatus.COMPLETED)
				.map(this::mapToResponseDto)
				.collect(Collectors.toList());
		
	}
	
	@Transactional
	public List<OrderResponseDto> completeBasketByCode(String enteredCode){
		User seller = userService.getCurrentUser();
		
		List<Order> orders = orderRepository.findByPickupCode(enteredCode);
		
		if(orders.isEmpty()) {
			throw new RuntimeException("Hatalı teslimat kodu! Lütfen müşterinin ekranındaki kodu kontrol edin.");
		}
		
		if(!orders.get(0).getInventory().getStore().getOwner().getId().equals(seller.getId())) {
			throw new RuntimeException("Bu siparişleri onaylama yetkiniz yok.");
		}
		
		boolean hasApprovedOrder = false;
		
		for(Order order : orders) {
			if(order.getStatus() == OrderStatus.APPROVED) {
				order.setStatus(OrderStatus.COMPLETED);
				hasApprovedOrder = true;
			}
		}
		
		if (!hasApprovedOrder) {
			throw new RuntimeException("Bu teslimat koduna ait bekleyen sipariş bulunmamaktadır.");
		}
		
		List<Order> updatedOrders = orderRepository.saveAll(orders);
		
		Notification notification = new Notification();
		notification.setUser(orders.get(0).getCustomer());
		notification.setTitle("Afiyet Olsun");
		notification.setMessage("Sepetinizdeki ürünleri teslim aldınız. Bizi tercih ettiğiniz için teşekkürler!");
		notification.setType("ORDER_COMPLETED");
		notificationService.sendNotification(notification);
		
		return updatedOrders.stream().map(this::mapToResponseDto).collect(Collectors.toList());
		
	}
	
	
	
	
	
	@Transactional
	public OrderResponseDto canceledOrder(Long orderId) {
		User user = userService.getCurrentUser();
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
		
		if(order.getStatus() != OrderStatus.APPROVED) {
			throw new RuntimeException("Only confirmed orders that have not yet been received can be cancelled.");
		}
		
		order.setStatus(OrderStatus.CANCELLED);
		
		Inventory inventory = order.getInventory();
		inventory.setStockCount(inventory.getStockCount() + order.getQuantity());
		
		Notification notification = new Notification();
		notification.setUser(order.getCustomer());
		notification.setTitle("Order cancellation");
		notification.setMessage(inventory.getProduct().getName()+" your order has been canceled,");
		notification.setType("ORDER_CANCELLED");
		notificationService.sendNotification(notification);
		
		return mapToResponseDto(orderRepository.save(order));
	}
	
	@Transactional
	public EarningsResponseDto getMyEarningsSummary() {
		User user = userService.getCurrentUser();
		BigDecimal totalOverallEarnings = BigDecimal.ZERO;
		Integer totalOverallOrder = 0;
		List<EarningsDto> storeEarningsList = new ArrayList<>();
		
		for(Store store : user.getStores()) {
			List<Order> orders = orderRepository.findByInventory_Store_IdOrderByOrderDateDesc(store.getId(), org.springframework.data.domain.Pageable.unpaged()).getContent();
			BigDecimal storeTotal = BigDecimal.ZERO;
			int storeOrderCount = 0;
			
			for(Order order : orders) {
				if(order.getStatus() != OrderStatus.CANCELLED) {
					storeTotal = storeTotal.add(order.getTotalAmount());
					storeOrderCount++;
				}
			}
			
			totalOverallEarnings = totalOverallEarnings.add(storeTotal);
			totalOverallOrder += storeOrderCount;
			
			if(store.isActive() != null && store.isActive()) {
				EarningsDto storeDto = new EarningsDto();
				storeDto.setStoreId(store.getId());
				storeDto.setStoreName(store.getStoreName());
				storeDto.setCity(store.getCity());
				storeDto.setDistrict(store.getDistrict());
				storeDto.setTotalEarnings(storeTotal);
				storeDto.setOrderCount(storeOrderCount);
				
				if (store.getStoreImageUrls() != null && !store.getStoreImageUrls().isEmpty()) {
					storeDto.setStoreImageUrl(store.getStoreImageUrls().get(0));
				}
				storeEarningsList.add(storeDto);	
			}	
		}
		
		EarningsResponseDto responseDto = new EarningsResponseDto();
		responseDto.setTotalOverallEarnings(totalOverallEarnings);
		responseDto.setTotalOverallOrders(totalOverallOrder);
		responseDto.setStoreEarnings(storeEarningsList);
		
		return responseDto;
	}
	
	
	@Transactional
	public CardResponseDto confirmBasket(CardRequestDto dto) {
		User user = userService.getCurrentUser();
		Basket basket = basketRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Basket not found"));
		
		if(basket.getItems().isEmpty()) {
			throw new RuntimeException("Sepetiniz boş");
		}
		
		for(BasketItem item : basket.getItems()) {
			if(item.getInventory().getStockCount() < item.getQuantity()) {
				throw new RuntimeException(item.getInventory().getProduct().getName()+" icin yeterli stok kalmadi!");
			}
		}
		
		OrderRequestDto orderDto = new OrderRequestDto();
		orderDto.setCardNumber(dto.getCardNumber());
		orderDto.setExpiryDate(dto.getExpiryDate());
		orderDto.setCvc(dto.getCvc());
		PaymentResponseDto paymentResponseDto = paymentService.processPayment(orderDto);
		
		if(!paymentResponseDto.isSuccess()) {
			throw new RuntimeException("Ödeme reddedildi");
		}
		
		Map<Long, List<BasketItem>> itemsByStore =new HashMap<>();
		
		for(BasketItem item : basket.getItems()) {
			Long storeId = item.getInventory().getStore().getId();
			itemsByStore.computeIfAbsent(storeId, k -> new ArrayList<>()).add(item);
		}
		
		List<String> generatedCodes = new ArrayList<>();
		
		for(Map.Entry<Long, List<BasketItem>> entry: itemsByStore.entrySet()) {
			List<BasketItem> storeItems = entry.getValue();
			User seller = storeItems.get(0).getInventory().getStore().getOwner();
			
			String storePickupCode = generatePickupCode();
	        generatedCodes.add(storePickupCode);
	        
	        for(BasketItem item : storeItems) {
	        	inventoryService.reduceStock(item.getInventory().getId(), item.getQuantity());
	            
	            Order order = new Order();
	            order.setCustomer(user);
	            order.setInventory(item.getInventory());
	            order.setQuantity(item.getQuantity());
	            
	            BigDecimal itemTotal = item.getInventory().getDiscountPrice().multiply(new BigDecimal(item.getQuantity()));
	            order.setTotalAmount(itemTotal);
	            
	            order.setPickupCode(storePickupCode); 
	            order.setStatus(OrderStatus.APPROVED);
	            order.setPaymentTransId(paymentResponseDto.getTransId());
	            
	            orderRepository.save(order);
	        }
	        Notification sellerNotification = new Notification();
	        sellerNotification.setUser(seller);
	        sellerNotification.setTitle("Yeni Sepet Siparişi! 🎉");
	        sellerNotification.setMessage("Harika! Dükkanınızdan yeni bir sepet siparişi aldınız. Sipariş bekleyenler kısmını kontrol ediniz!");
	        sellerNotification.setType("NEW_ORDER");
	        notificationService.sendNotification(sellerNotification);
			
		}
		basket.getItems().clear();
	    basket.setTotalAmount(BigDecimal.ZERO);
	    basketRepository.save(basket);
		
	    Notification notification = new Notification();
	    notification.setUser(user);
	    notification.setTitle("Siparişiniz Onaylandı!");
	    String allCodes = String.join(", ", generatedCodes);
	    notification.setMessage("Ödemeniz alındı. Siparişleriniz oluşturuldu. İlgili marketler için Teslimat Kodlarınız: " + allCodes + ". Ayrıca sesabım ekranına gelerek, siparişlerim sekmesinden aktif siparişlerinizi ve teslimat kodlarınızı görebilirsiniz.");
	    notification.setType("ORDER_SUCCESS");
	    notificationService.sendNotification(notification);
	    
	    CardResponseDto response = new CardResponseDto();
	    response.setPickupCode(generatedCodes.get(0)); 
	    response.setMessage("Ödeme başarılı. Teslimat kodlarınız siparişlerim sekmesinde görünmektedir.");
	    return response;
		
	}
	
	
	private OrderResponseDto mapToResponseDto(Order order) {
		OrderResponseDto dto = new OrderResponseDto();
		dto.setId(order.getId());
		dto.setProductName(order.getInventory().getProduct().getName());
		dto.setStoreName(order.getInventory().getStore().getStoreName());
		dto.setQuantity(order.getQuantity());
		dto.setTotalAmount(order.getTotalAmount());
		dto.setStatus(order.getStatus());
		dto.setPickupCode(order.getPickupCode());
		dto.setOrderDate(order.getOrderDate());
		
		if(order.getCustomer() != null) {
			dto.setCustomerName(order.getCustomer().getFirstName()+" "+order.getCustomer().getLastName());
			dto.setCustomerPhone(order.getCustomer().getPhoneNumber());
		}
		
		
		if(order.getInventory() != null) {
			if(order.getInventory().getProduct() != null) {
				dto.setProductName(order.getInventory().getProduct().getName());
			}
			
			if(order.getInventory().getStore() != null) {
				dto.setStoreName(order.getInventory().getStore().getStoreName());
				dto.setLatitude(order.getInventory().getStore().getLatitude());
				dto.setLongitude(order.getInventory().getStore().getLongitude());
			}
				
		}
		return dto;
	}
	
	
	
	
	
	
	
	
	
	
}
