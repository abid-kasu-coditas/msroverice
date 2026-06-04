package com.eps.notificationservice.mapper;

import com.eps.notificationservice.dto.NotificationDTO;
import com.eps.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setCustomerId(notification.getCustomerId());
        dto.setEventType(notification.getEventType() != null ? notification.getEventType().name() : null);
        dto.setMessage(notification.getMessage());
        dto.setEmailBody(notification.getEmailBody());
        dto.setSmsBody(notification.getSmsBody());
        dto.setEmailStatus(notification.getEmailStatus() != null ? notification.getEmailStatus().name() : null);
        dto.setSmsStatus(notification.getSmsStatus() != null ? notification.getSmsStatus().name() : null);
        dto.setSentAt(notification.getSentAt());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());

        return dto;
    }

    public Notification toModel(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setCustomerId(dto.getCustomerId());
        if (dto.getEventType() != null) {
            notification.setEventType(Notification.EventType.valueOf(dto.getEventType()));
        }
        notification.setMessage(dto.getMessage());
        notification.setEmailBody(dto.getEmailBody());
        notification.setSmsBody(dto.getSmsBody());
        if (dto.getEmailStatus() != null) {
            notification.setEmailStatus(Notification.NotificationStatus.valueOf(dto.getEmailStatus()));
        }
        if (dto.getSmsStatus() != null) {
            notification.setSmsStatus(Notification.NotificationStatus.valueOf(dto.getSmsStatus()));
        }
        notification.setSentAt(dto.getSentAt());
        notification.setCreatedAt(dto.getCreatedAt());
        notification.setUpdatedAt(dto.getUpdatedAt());

        return notification;
    }
}
