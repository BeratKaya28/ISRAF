package com.israf.api.repository;


import java.util.List;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.israf.api.model.Notification;
import com.israf.api.model.User;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	List<Notification> findByUserAndIsReadFalse(User user);
	long countByUserAndIsReadFalse(User user);
	List<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	void deleteByUser(User user);
	List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);



	@Modifying
	@Query(value = "DELETE FROM notifications WHERE id IN (" +
	               "  SELECT id FROM (" +
	               "    SELECT id, ROW_NUMBER() OVER(PARTITION BY user_id ORDER BY created_at DESC) as rn " +
	               "    FROM notifications" +
	               "  3) t WHERE t.rn > 20" +
	               ")", nativeQuery = true)
	void deleteExcessNotifications();




}
