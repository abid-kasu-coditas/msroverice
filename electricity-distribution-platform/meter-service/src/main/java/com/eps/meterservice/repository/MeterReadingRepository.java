package com.eps.meterservice.repository;

import com.eps.meterservice.model.MeterReading;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterReadingRepository extends JpaRepository<MeterReading, UUID> {

    List<MeterReading> findByMeterAccountIdOrderByReadingDateDesc(UUID meterAccountId);

    Optional<MeterReading> findTopByMeterAccountIdOrderByReadingDateDesc(UUID meterAccountId);

    void deleteByMeterAccountId(UUID meterAccountId);
}
