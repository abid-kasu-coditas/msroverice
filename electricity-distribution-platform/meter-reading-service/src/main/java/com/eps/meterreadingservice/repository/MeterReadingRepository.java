package com.eps.meterreadingservice.repository;

import com.eps.meterreadingservice.model.MeterReading;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

  List<MeterReading> findByConnectionIdOrderByReadAtDesc(Long connectionId);
}
