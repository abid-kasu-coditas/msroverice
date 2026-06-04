package com.eps.notificationservice.service;

import com.eps.notificationservice.model.Notification;
import com.eps.notificationservice.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Create and save a new notification
     */
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    /**
     * Get notification by ID
     */
    public Notification getNotificationById(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    /**
     * Get all notifications with pagination
     */
    public Page<Notification> getAllNotifications(Pageable pageable) {
        return notificationRepository.findAll(pageable);
    }

    /**
     * Get notifications by customer ID
     */
    public Page<Notification> getNotificationsByCustomerId(UUID customerId, Pageable pageable) {
        return notificationRepository.findByCustomerId(customerId, pageable);
    }

    /**
     * Get notifications by event type
     */
    public Page<Notification> getNotificationsByEventType(
            Notification.EventType eventType,
            Pageable pageable
    ) {
        return notificationRepository.findByEventType(eventType, pageable);
    }

    /**
     * Get notifications by email status
     */
    public Page<Notification> getNotificationsByEmailStatus(
            Notification.NotificationStatus status,
            Pageable pageable
    ) {
        return notificationRepository.findByEmailStatus(status, pageable);
    }

    /**
     * Get notifications by SMS status
     */
    public Page<Notification> getNotificationsBySmStatus(
            Notification.NotificationStatus status,
            Pageable pageable
    ) {
        return notificationRepository.findBySmsStatus(status, pageable);
    }

    /**
     * Send email notification
     */
    public Notification sendEmailNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            // Simulate sending email
            System.out.println("Sending email to customer: " + notification.getCustomerId());
            System.out.println("Email Body: " + notification.getEmailBody());
            
            notification.setEmailStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
        return null;
    }

    /**
     * Send SMS notification
     */
    public Notification sendSmsNotification(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            // Simulate sending SMS
            System.out.println("Sending SMS to customer: " + notification.getCustomerId());
            System.out.println("SMS Body: " + notification.getSmsBody());
            
            notification.setSmsStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setUpdatedAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
        return null;
    }

    /**
     * Retry failed notifications
     */
    public List<Notification> getFailedEmailNotifications() {
        return notificationRepository.findByEmailStatusAndCreatedAtBefore(
                Notification.NotificationStatus.FAILED,
                LocalDateTime.now().minusHours(1)
        );
    }

    /**
     * Get SMS notifications to retry
     */
    public List<Notification> getFailedSmsNotifications() {
        return notificationRepository.findBySmsStatusAndCreatedAtBefore(
                Notification.NotificationStatus.FAILED,
                LocalDateTime.now().minusHours(1)
        );
    }

    /**
     * Get notifications by date range
     */
    public List<Notification> getNotificationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return notificationRepository.findByCreatedAtBetween(startDate, endDate);
    }

    /**
     * Delete notification
     */
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }

    /**
     * Get count of pending email notifications
     */
    public long getPendingEmailNotificationCount() {
        return notificationRepository.countByEmailStatus(Notification.NotificationStatus.PENDING);
    }

    /**
     * Get count of pending SMS notifications
     */
    public long getPendingSmsNotificationCount() {
        return notificationRepository.countBySmsStatus(Notification.NotificationStatus.PENDING);
    }
}
