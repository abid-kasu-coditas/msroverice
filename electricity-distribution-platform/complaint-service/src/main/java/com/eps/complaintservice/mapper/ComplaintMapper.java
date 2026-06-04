package com.eps.complaintservice.mapper;

import com.eps.complaintservice.dto.ComplaintRequestDTO;
import com.eps.complaintservice.dto.ComplaintResponseDTO;
import com.eps.complaintservice.model.Complaint;

public class ComplaintMapper {

    public static Complaint toModel(ComplaintRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Complaint(dto.getCustomerId(), dto.getCategory(), dto.getDescription(), dto.getStatus(),
            dto.getState(), dto.getDistrict(), dto.getCity());
    }

    public static ComplaintResponseDTO toDTO(Complaint model) {
        if (model == null) {
            return null;
        }
        return new ComplaintResponseDTO(
            model.getId(),
            model.getCustomerId(),
            model.getCategory(),
            model.getDescription(),
            model.getState(),
            model.getDistrict(),
            model.getCity(),
            model.getStatus(),
            model.getCreatedAt(),
            model.getAssignedTechnicianId(),
            model.getAssignedByUserId(),
            model.getAssignedAt(),
            model.getResolvedAt(),
            model.getResolution()
        );
    }
}
