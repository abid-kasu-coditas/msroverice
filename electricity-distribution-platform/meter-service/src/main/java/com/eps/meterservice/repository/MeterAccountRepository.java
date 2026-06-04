package com.eps.meterservice.repository;

import com.eps.meterservice.model.MeterAccount;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterAccountRepository extends JpaRepository<MeterAccount, Long> {

    Optional<MeterAccount> findByMeterSerialNumber(String meterSerialNumber);

    boolean existsByMeterSerialNumber(String meterSerialNumber);

    boolean existsByMeterSerialNumberAndIdNot(String meterSerialNumber, Long id);

    List<MeterAccount> findByConnectionId(Long connectionId);
}
