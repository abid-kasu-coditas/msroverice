package com.eps.meterservice.repository;

import com.eps.meterservice.model.MeterAccount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterAccountRepository extends JpaRepository<MeterAccount, UUID> {

    Optional<MeterAccount> findByMeterSerialNumber(String meterSerialNumber);

    boolean existsByMeterSerialNumber(String meterSerialNumber);

    boolean existsByMeterSerialNumberAndIdNot(String meterSerialNumber, UUID id);

    List<MeterAccount> findByConnectionId(UUID connectionId);
}
