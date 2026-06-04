package com.eps.connectionservice.mapper;

import com.eps.connectionservice.dto.ConnectionRequestDTO;
import com.eps.connectionservice.dto.ConnectionResponseDTO;
import com.eps.connectionservice.model.Connection;

public class ConnectionMapper {

    public static Connection toModel(ConnectionRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Connection(
            dto.getCustomerId(),
            dto.getConnectionNumber(),
            dto.getServiceAddress(),
            dto.getTariffPlan(),
            dto.getLoadCapacity(),
            dto.getStatus(),
            dto.getConnectionDate(),
            dto.getTerminationDate()
        );
    }

    public static ConnectionResponseDTO toDTO(Connection model) {
        if (model == null) {
            return null;
        }
        return new ConnectionResponseDTO(
            model.getId(),
            model.getCustomerId(),
            model.getConnectionNumber(),
            model.getServiceAddress(),
            model.getTariffPlan(),
            model.getLoadCapacity(),
            model.getStatus(),
            model.getConnectionDate(),
            model.getTerminationDate(),
            model.getCreatedAt()
        );
    }
}
