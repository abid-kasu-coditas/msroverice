package com.eps.billingservice.mapper;

import com.eps.billingservice.dto.BillResponseDTO;
import com.eps.billingservice.model.Bill;

public class BillMapper {

    public static BillResponseDTO toDTO(Bill model) {
        if (model == null) {
            return null;
        }
        return new BillResponseDTO(
            model.getId(),
            model.getCustomerId(),
            model.getMeterId(),
            model.getBillNumber(),
            model.getBillDate(),
            model.getDueDate(),
            model.getUnitsConsumed(),
            model.getBaseAmount(),
            model.getTaxes(),
            model.getPenalties(),
            model.getDiscounts(),
            model.getTotalAmount(),
            model.getStatus(),
            model.getCreatedAt()
        );
    }
}
