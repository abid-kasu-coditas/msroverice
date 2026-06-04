package com.eps.meterservice.repository;

import com.eps.meterservice.model.MeterType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterTypeRepository extends JpaRepository<MeterType, Long> {

    Optional<MeterType> findByIdAndActiveTrue(Long id);
}
