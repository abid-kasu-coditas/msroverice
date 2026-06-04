package com.eps.geographyservice.repository;

import com.eps.geographyservice.model.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

  List<City> findByDistrictId(Long districtId);
}
