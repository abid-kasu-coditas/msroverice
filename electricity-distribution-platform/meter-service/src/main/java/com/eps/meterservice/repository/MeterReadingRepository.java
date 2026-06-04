package com.eps.meterservice.repository;

import com.eps.meterservice.model.MeterReading;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    List<MeterReading> findByMeterAccountIdOrderByReadingDateDesc(Long meterAccountId);

    Optional<MeterReading> findTopByMeterAccountIdOrderByReadingDateDesc(Long meterAccountId);

    void deleteByMeterAccountId(Long meterAccountId);
}
