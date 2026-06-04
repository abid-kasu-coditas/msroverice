package com.eps.geographyservice.repository;

import com.eps.geographyservice.model.District;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Long> {

  List<District> findByStateId(Long stateId);
}
