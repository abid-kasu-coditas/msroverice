package com.eps.notificationservice.controller;

import com.eps.notificationservice.dto.NotificationDTO;
import com.eps.notificationservice.mapper.NotificationMapper;
import com.eps.notificationservice.model.Notification;
import com.eps.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "APIs for managing notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Get all notifications with pagination
     */
    @GetMapping
    @Operation(summary = "Get all notifications", description = "Retrieve all notifications with pagination")
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getAllNotifications(pageable);
        Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get notification by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID", description = "Retrieve a specific notification")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable UUID id) {
        Notification notification = notificationService.getNotificationById(id);
        if (notification != null) {
            return ResponseEntity.ok(notificationMapper.toDTO(notification));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get notifications by customer ID
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get notifications by customer", description = "Retrieve all notifications for a customer")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByCustomerId(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getNotificationsByCustomerId(customerId, pageable);
        Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Get notifications by event type
     */
    @GetMapping("/event/{eventType}")
    @Operation(summary = "Get notifications by event type", description = "Retrieve notifications by event type")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByEventType(
            @PathVariable String eventType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Notification.EventType type = Notification.EventType.valueOf(eventType.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<Notification> notifications = notificationService.getNotificationsByEventType(type, pageable);
            Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get notifications by email status
     */
    @GetMapping("/status/email/{status}")
    @Operation(summary = "Get notifications by email status", description = "Retrieve notifications by email delivery status")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsByEmailStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Notification.NotificationStatus emailStatus = Notification.NotificationStatus.valueOf(status.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<Notification> notifications = notificationService.getNotificationsByEmailStatus(emailStatus, pageable);
            Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get notifications by SMS status
     */
    @GetMapping("/status/sms/{status}")
    @Operation(summary = "Get notifications by SMS status", description = "Retrieve notifications by SMS delivery status")
    public ResponseEntity<Page<NotificationDTO>> getNotificationsBySmStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Notification.NotificationStatus smsStatus = Notification.NotificationStatus.valueOf(status.toUpperCase());
            Pageable pageable = PageRequest.of(page, size);
            Page<Notification> notifications = notificationService.getNotificationsBySmStatus(smsStatus, pageable);
            Page<NotificationDTO> dtoPage = notifications.map(notificationMapper::toDTO);
            return ResponseEntity.ok(dtoPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Send email notification
     */
    @PostMapping("/{id}/send-email")
    @Operation(summary = "Send email notification", description = "Send email for a specific notification")
    public ResponseEntity<NotificationDTO> sendEmailNotification(@PathVariable UUID id) {
        Notification notification = notificationService.sendEmailNotification(id);
        if (notification != null) {
            return ResponseEntity.ok(notificationMapper.toDTO(notification));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Send SMS notification
     */
    @PostMapping("/{id}/send-sms")
    @Operation(summary = "Send SMS notification", description = "Send SMS for a specific notification")
    public ResponseEntity<NotificationDTO> sendSmsNotification(@PathVariable UUID id) {
        Notification notification = notificationService.sendSmsNotification(id);
        if (notification != null) {
            return ResponseEntity.ok(notificationMapper.toDTO(notification));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get metrics
     */
    @GetMapping("/metrics/summary")
    @Operation(summary = "Get notification metrics", description = "Get summary of notification statistics")
    public ResponseEntity<?> getMetrics() {
        return ResponseEntity.ok(Map.of(
                "pendingEmails", notificationService.getPendingEmailNotificationCount(),
                "pendingSms", notificationService.getPendingSmsNotificationCount()
        ));
    }

    /**
     * Get notifications by date range
     */
    @GetMapping("/search")
    @Operation(summary = "Search notifications by date range", description = "Search notifications between start and end dates")
    public ResponseEntity<List<NotificationDTO>> searchByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime start = LocalDateTime.parse(startDate, formatter);
            LocalDateTime end = LocalDateTime.parse(endDate, formatter);
            
            List<Notification> notifications = notificationService.getNotificationsByDateRange(start, end);
            List<NotificationDTO> dtoList = notifications.stream()
                    .map(notificationMapper::toDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete notification
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification", description = "Delete a specific notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
}
