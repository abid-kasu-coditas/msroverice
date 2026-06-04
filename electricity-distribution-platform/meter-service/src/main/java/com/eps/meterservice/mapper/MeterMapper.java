package com.eps.meterservice.mapper;

import com.eps.meterservice.dto.MeterAccountRequestDTO;
import com.eps.meterservice.dto.MeterAccountResponseDTO;
import com.eps.meterservice.dto.MeterReadingResponseDTO;
import com.eps.meterservice.model.MeterAccount;
import com.eps.meterservice.model.MeterReading;

public class MeterMapper {

    public static MeterAccount toModel(MeterAccountRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return new MeterAccount(
            dto.getConnectionId(),
            dto.getMeterSerialNumber(),
            dto.getMeterType(),
            dto.getInstallationDate(),
            dto.getStatus()
        );
    }

    public static MeterAccountResponseDTO toDTO(MeterAccount model) {
        if (model == null) {
            return null;
        }
        return new MeterAccountResponseDTO(
            model.getId(),
            model.getConnectionId(),
            model.getMeterSerialNumber(),
            model.getMeterType(),
            model.getInstallationDate(),
            model.getStatus(),
            model.getCreatedAt()
        );
    }

    public static MeterReadingResponseDTO toDTO(MeterReading model) {
        if (model == null) {
            return null;
        }
        return new MeterReadingResponseDTO(
            model.getId(),
            model.getMeterAccountId(),
            model.getCurrentReading(),
            model.getPreviousReading(),
            model.getReadingDate(),
            model.getUnitsConsumed(),
            model.getRecordedAt()
        );
    }
}
