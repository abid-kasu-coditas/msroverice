package com.eps.notificationservice.repository;

import com.eps.notificationservice.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByCustomerId(UUID customerId, Pageable pageable);

    Page<Notification> findByEventType(Notification.EventType eventType, Pageable pageable);

    Page<Notification> findByEmailStatus(Notification.NotificationStatus status, Pageable pageable);

    Page<Notification> findBySmsStatus(Notification.NotificationStatus status, Pageable pageable);

    List<Notification> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Notification> findAll(Pageable pageable);

    long countByEmailStatus(Notification.NotificationStatus status);

    long countBySmsStatus(Notification.NotificationStatus status);

    List<Notification> findByEmailStatusAndCreatedAtBefore(
            Notification.NotificationStatus status,
            LocalDateTime dateTime
    );

    List<Notification> findBySmsStatusAndCreatedAtBefore(
            Notification.NotificationStatus status,
            LocalDateTime dateTime
    );
}
